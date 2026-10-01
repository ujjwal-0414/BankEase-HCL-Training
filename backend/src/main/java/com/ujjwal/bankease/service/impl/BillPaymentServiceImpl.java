package com.ujjwal.bankease.service.impl;

import com.ujjwal.bankease.dto.request.BillPaymentRequest;
import com.ujjwal.bankease.dto.response.BillPaymentResponse;
import com.ujjwal.bankease.entity.AuditLog;
import com.ujjwal.bankease.entity.BankAccount;
import com.ujjwal.bankease.entity.BillPayment;
import com.ujjwal.bankease.entity.User;
import com.ujjwal.bankease.enums.AccountStatus;
import com.ujjwal.bankease.enums.AuditAction;
import com.ujjwal.bankease.enums.BillPaymentStatus;
import com.ujjwal.bankease.exception.BusinessException;
import com.ujjwal.bankease.exception.ResourceNotFoundException;
import com.ujjwal.bankease.mapper.BankEaseMapper;
import com.ujjwal.bankease.repository.AuditLogRepository;
import com.ujjwal.bankease.repository.BankAccountRepository;
import com.ujjwal.bankease.repository.BillPaymentRepository;
import com.ujjwal.bankease.service.BillPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillPaymentServiceImpl implements BillPaymentService {
    private final BankAccountRepository accounts;
    private final BillPaymentRepository bills;
    private final BaseService base;
    private final BankEaseMapper mapper;
    private final AuditLogRepository audit;

    @Override
    @Transactional
    public BillPaymentResponse pay(BillPaymentRequest r) {
        User u = base.currentUser();
        BankAccount a = accounts.findByIdForUpdate(r.getAccountId()).orElseThrow(() -> new ResourceNotFoundException("Bank account not found"));
        if (!a.getUser().getId().equals(u.getId()))
            throw new BusinessException("You cannot use another user's account");
        if (a.getStatus() != AccountStatus.ACTIVE) throw new BusinessException("Account is not active");
        if (r.getAmount().compareTo(a.getBalance()) > 0) throw new BusinessException("Insufficient account balance");
        a.setBalance(a.getBalance().subtract(r.getAmount()));
        accounts.save(a);
        BillPayment b = BillPayment.builder().referenceNumber("BILL" + UUID.randomUUID().toString().replace("-", "").substring(0, 20).toUpperCase()).user(u).account(a).billerName(r.getBillerName()).billerCategory(r.getBillerCategory()).consumerNumber(r.getConsumerNumber()).amount(r.getAmount()).status(BillPaymentStatus.SUCCESS).build();
        bills.save(b);
        audit.save(AuditLog.builder().user(u).action(AuditAction.BILL_PAYMENT).description("Bill payment " + b.getReferenceNumber() + " for " + r.getAmount()).build());
        return mapper.toBill(b);
    }

    @Override
    public List<BillPaymentResponse> history() {
        return bills.findTop50ByUserOrderByCreatedAtDesc(base.currentUser()).stream().map(mapper::toBill).toList();
    }
}
