package com.ujjwal.bankease.service.impl;

import com.ujjwal.bankease.dto.request.FundTransferRequest;
import com.ujjwal.bankease.dto.response.TransferResponse;
import com.ujjwal.bankease.entity.*;
import com.ujjwal.bankease.enums.*;
import com.ujjwal.bankease.exception.BusinessException;
import com.ujjwal.bankease.exception.ResourceNotFoundException;
import com.ujjwal.bankease.mapper.BankEaseMapper;
import com.ujjwal.bankease.repository.AuditLogRepository;
import com.ujjwal.bankease.repository.BankAccountRepository;
import com.ujjwal.bankease.repository.BeneficiaryRepository;
import com.ujjwal.bankease.repository.FundTransferRepository;
import com.ujjwal.bankease.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {
    private final BankAccountRepository accounts;
    private final BeneficiaryRepository beneficiaries;
    private final FundTransferRepository transfers;
    private final BaseService base;
    private final BankEaseMapper mapper;
    private final AuditLogRepository audit;

    @Override
    @Transactional
    public TransferResponse transfer(FundTransferRequest r) {
        User u = base.currentUser();
        BankAccount source = accounts.findByIdForUpdate(r.getSourceAccountId()).orElseThrow(() -> new ResourceNotFoundException("Source account not found"));
        if (!source.getUser().getId().equals(u.getId()))
            throw new BusinessException("You cannot transfer from another user's account");
        Beneficiary b = beneficiaries.findByIdAndUser(r.getBeneficiaryId(), u).orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found"));
        if (!b.isActive()) throw new BusinessException("Beneficiary is inactive");
        if (source.getStatus() != AccountStatus.ACTIVE) throw new BusinessException("Source account is not active");
        if (u.getKycStatus() != KycStatus.VERIFIED)
            throw new BusinessException("KYC verification is required before fund transfer");
        if (r.getAmount().compareTo(source.getBalance()) > 0)
            throw new BusinessException("Insufficient account balance");
        BigDecimal daily = transfers.sumSuccessfulTransfers(u, LocalDateTime.now().toLocalDate().atStartOfDay());
        if (daily.add(r.getAmount()).compareTo(source.getDailyTransferLimit()) > 0)
            throw new BusinessException("Daily transfer limit exceeded");
        validateMode(r.getMode(), r.getAmount());
        FundTransfer t = FundTransfer.builder().referenceNumber("TXN" + UUID.randomUUID().toString().replace("-", "").substring(0, 20).toUpperCase()).user(u).sourceAccount(source).beneficiary(b).destinationAccountNumber(b.getAccountNumber()).destinationIfsc(b.getIfscCode()).mode(r.getMode()).amount(r.getAmount()).remarks(r.getRemarks()).status(TransactionStatus.PENDING).build();
        source.setBalance(source.getBalance().subtract(r.getAmount()));
        accounts.save(source);
        accounts.findByAccountNumber(b.getAccountNumber()).ifPresent(destination -> {
            if (!destination.getId().equals(source.getId()) && destination.getStatus() == AccountStatus.ACTIVE) {
                destination.setBalance(destination.getBalance().add(r.getAmount()));
                accounts.save(destination);
            }
        });
        t.setStatus(TransactionStatus.SUCCESS);
        transfers.save(t);
        audit.save(AuditLog.builder().user(u).action(AuditAction.TRANSFER).description("Fund transfer " + t.getReferenceNumber() + " for " + r.getAmount()).build());
        return mapper.toTransfer(t);
    }

    private void validateMode(TransferMode mode, BigDecimal a) {
        if (mode == TransferMode.RTGS && a.compareTo(new BigDecimal("200000")) < 0)
            throw new BusinessException("RTGS minimum amount is ₹2,00,000");
        if (mode == TransferMode.IMPS && a.compareTo(new BigDecimal("500000")) > 0)
            throw new BusinessException("IMPS limit is ₹5,00,000 per transaction");
    }

    @Override
    public List<TransferResponse> history() {
        return transfers.findTop50ByUserOrderByCreatedAtDesc(base.currentUser()).stream().map(mapper::toTransfer).toList();
    }
}
