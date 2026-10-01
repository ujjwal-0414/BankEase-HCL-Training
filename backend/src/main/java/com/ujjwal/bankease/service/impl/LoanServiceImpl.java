package com.ujjwal.bankease.service.impl;

import com.ujjwal.bankease.dto.request.LoanApplicationRequest;
import com.ujjwal.bankease.dto.request.LoanDecisionRequest;
import com.ujjwal.bankease.dto.response.LoanResponse;
import com.ujjwal.bankease.entity.AuditLog;
import com.ujjwal.bankease.entity.LoanApplication;
import com.ujjwal.bankease.entity.User;
import com.ujjwal.bankease.enums.AuditAction;
import com.ujjwal.bankease.enums.LoanStatus;
import com.ujjwal.bankease.exception.BusinessException;
import com.ujjwal.bankease.exception.ResourceNotFoundException;
import com.ujjwal.bankease.mapper.BankEaseMapper;
import com.ujjwal.bankease.repository.AuditLogRepository;
import com.ujjwal.bankease.repository.LoanApplicationRepository;
import com.ujjwal.bankease.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
    private final LoanApplicationRepository repo;
    private final BaseService base;
    private final BankEaseMapper mapper;
    private final AuditLogRepository audit;

    @Override
    @Transactional
    public LoanResponse apply(LoanApplicationRequest r) {
        User u = base.currentUser();
        LoanApplication l = LoanApplication.builder().applicationNumber("LOAN" + UUID.randomUUID().toString().replace("-", "").substring(0, 20).toUpperCase()).user(u).loanType(r.getLoanType()).requestedAmount(r.getRequestedAmount()).tenureMonths(r.getTenureMonths()).status(LoanStatus.PENDING).build();
        repo.save(l);
        audit.save(AuditLog.builder().user(u).action(AuditAction.LOAN_APPLICATION).description("Loan application " + l.getApplicationNumber()).build());
        return mapper.toLoan(l);
    }

    @Override
    public List<LoanResponse> myLoans() {
        return repo.findByUserOrderByCreatedAtDesc(base.currentUser()).stream().map(mapper::toLoan).toList();
    }

    @Override
    public List<LoanResponse> pending() {
        return repo.findByStatusOrderByCreatedAtAsc(LoanStatus.PENDING).stream().map(mapper::toLoan).toList();
    }

    @Override
    @Transactional
    public LoanResponse decide(Long id, LoanDecisionRequest r) {
        LoanApplication l = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Loan application not found"));
        if (l.getStatus() != LoanStatus.PENDING)
            throw new BusinessException("Only pending applications can be decided");
        if (r.getStatus() != LoanStatus.APPROVED && r.getStatus() != LoanStatus.REJECTED)
            throw new BusinessException("Decision must be APPROVED or REJECTED");
        if (r.getStatus() == LoanStatus.APPROVED) {
            if (r.getInterestRate() == null) throw new BusinessException("Interest rate is required for approval");
            l.setInterestRate(r.getInterestRate());
            l.setEmiAmount(calculateEmi(l.getRequestedAmount(), r.getInterestRate(), l.getTenureMonths()));
        }
        l.setStatus(r.getStatus());
        l.setDecisionRemarks(r.getDecisionRemarks());
        l.setDecidedAt(LocalDateTime.now());
        repo.save(l);
        audit.save(AuditLog.builder().user(l.getUser()).action(AuditAction.LOAN_DECISION).description("Loan " + l.getApplicationNumber() + " decided as " + r.getStatus()).build());
        return mapper.toLoan(l);
    }

    private BigDecimal calculateEmi(BigDecimal principal, BigDecimal annualRate, int months) {
        BigDecimal m = annualRate.divide(BigDecimal.valueOf(1200), 12, RoundingMode.HALF_UP);
        double p = principal.doubleValue(), rate = m.doubleValue();
        double emi = rate == 0 ? p / months : p * rate * Math.pow(1 + rate, months) / (Math.pow(1 + rate, months) - 1);
        return BigDecimal.valueOf(emi).setScale(2, RoundingMode.HALF_UP);
    }
}
