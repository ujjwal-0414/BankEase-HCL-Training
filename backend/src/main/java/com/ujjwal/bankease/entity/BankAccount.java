package com.ujjwal.bankease.entity;

import com.ujjwal.bankease.enums.AccountStatus;
import com.ujjwal.bankease.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bank_accounts", uniqueConstraints = @UniqueConstraint(name = "uk_account_number", columnNames = "account_number"), indexes = @Index(name = "idx_account_user", columnList = "user_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "account_number", nullable = false, unique = true, length = 20)
    private String accountNumber;
    @Column(nullable = false, length = 11)
    private String ifscCode;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountType accountType;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private AccountStatus status = AccountStatus.ACTIVE;
    @Column(nullable = false, precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;
    @Column(nullable = false, precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal dailyTransferLimit = new BigDecimal("200000.00");
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Version
    private Long version;
    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
