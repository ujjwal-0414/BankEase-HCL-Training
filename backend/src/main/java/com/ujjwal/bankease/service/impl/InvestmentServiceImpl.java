package com.ujjwal.bankease.service.impl;

import com.ujjwal.bankease.dto.request.InvestmentRequest;
import com.ujjwal.bankease.dto.response.InvestmentProductResponse;
import com.ujjwal.bankease.dto.response.InvestmentResponse;
import com.ujjwal.bankease.entity.*;
import com.ujjwal.bankease.enums.AuditAction;
import com.ujjwal.bankease.enums.InvestmentStatus;
import com.ujjwal.bankease.exception.BusinessException;
import com.ujjwal.bankease.exception.ResourceNotFoundException;
import com.ujjwal.bankease.mapper.BankEaseMapper;
import com.ujjwal.bankease.repository.AuditLogRepository;
import com.ujjwal.bankease.repository.BankAccountRepository;
import com.ujjwal.bankease.repository.InvestmentProductRepository;
import com.ujjwal.bankease.repository.InvestmentRepository;
import com.ujjwal.bankease.service.InvestmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvestmentServiceImpl implements InvestmentService {
    private final InvestmentProductRepository products;
    private final InvestmentRepository investments;
    private final BankAccountRepository accounts;
    private final BaseService base;
    private final BankEaseMapper mapper;
    private final AuditLogRepository audit;

    @Override
    public List<InvestmentProductResponse> products() {
        return products.findByActiveTrue().stream().map(mapper::toProduct).toList();
    }

    @Override
    @Transactional
    public InvestmentResponse invest(InvestmentRequest r) {
        User u = base.currentUser();
        InvestmentProduct p = products.findById(r.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Investment product not found"));
        if (!p.isActive()) throw new BusinessException("Investment product is inactive");
        if (r.getAmount().compareTo(p.getMinimumInvestment()) < 0)
            throw new BusinessException("Amount is below minimum investment");
        BankAccount a = accounts.findByIdForUpdate(r.getAccountId()).orElseThrow(() -> new ResourceNotFoundException("Bank account not found"));
        if (!a.getUser().getId().equals(u.getId()))
            throw new BusinessException("You cannot use another user's account");
        if (r.getAmount().compareTo(a.getBalance()) > 0) throw new BusinessException("Insufficient balance");
        a.setBalance(a.getBalance().subtract(r.getAmount()));
        accounts.save(a);
        Investment i = Investment.builder().referenceNumber("INV" + UUID.randomUUID().toString().replace("-", "").substring(0, 20).toUpperCase()).user(u).product(p).fundingAccount(a).principalAmount(r.getAmount()).currentValue(r.getAmount()).status(InvestmentStatus.ACTIVE).build();
        investments.save(i);
        audit.save(AuditLog.builder().user(u).action(AuditAction.INVESTMENT_PURCHASE).description("Investment purchase " + i.getReferenceNumber()).build());
        return mapper.toInvestment(i);
    }

    @Override
    public List<InvestmentResponse> myInvestments() {
        return investments.findByUserOrderByPurchasedAtDesc(base.currentUser()).stream().map(mapper::toInvestment).toList();
    }

    @Override
    @Transactional
    public InvestmentResponse redeem(Long id) {
        User u = base.currentUser();
        Investment i = investments.findByIdAndUser(id, u).orElseThrow(() -> new ResourceNotFoundException("Investment not found"));
        if (i.getStatus() != InvestmentStatus.ACTIVE) throw new BusinessException("Investment is already redeemed");
        BankAccount a = accounts.findByIdForUpdate(i.getFundingAccount().getId()).orElseThrow(() -> new ResourceNotFoundException("Funding account not found"));
        i.setStatus(InvestmentStatus.REDEEMED);
        i.setRedeemedAt(LocalDateTime.now());
        a.setBalance(a.getBalance().add(i.getCurrentValue()));
        accounts.save(a);
        investments.save(i);
        audit.save(AuditLog.builder().user(u).action(AuditAction.INVESTMENT_REDEEM).description("Investment redeemed " + i.getReferenceNumber()).build());
        return mapper.toInvestment(i);
    }
}
