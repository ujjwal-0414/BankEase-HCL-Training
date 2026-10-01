package com.ujjwal.bankease.entity;

import com.ujjwal.bankease.enums.LoanStatus;
import com.ujjwal.bankease.enums.LoanType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan_applications", indexes = @Index(name = "idx_loan_user", columnList = "user_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 40)
    private String applicationNumber;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LoanType loanType;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal requestedAmount;
    @Column(nullable = false)
    private Integer tenureMonths;
    @Column(precision = 7, scale = 4)
    private BigDecimal interestRate;
    @Column(precision = 19, scale = 2)
    private BigDecimal emiAmount;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private LoanStatus status = LoanStatus.PENDING;
    @Column(length = 500)
    private String decisionRemarks;
    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime decidedAt;
}
