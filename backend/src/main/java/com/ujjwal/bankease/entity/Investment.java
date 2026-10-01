package com.ujjwal.bankease.entity;

import com.ujjwal.bankease.enums.InvestmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "investments", indexes = @Index(name = "idx_investment_user", columnList = "user_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Investment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 40)
    private String referenceNumber;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private InvestmentProduct product;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private BankAccount fundingAccount;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal principalAmount;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal currentValue;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private InvestmentStatus status = InvestmentStatus.ACTIVE;
    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime purchasedAt = LocalDateTime.now();
    private LocalDateTime redeemedAt;
}
