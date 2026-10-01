package com.ujjwal.bankease.entity;

import com.ujjwal.bankease.enums.TransactionStatus;
import com.ujjwal.bankease.enums.TransferMode;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fund_transfers", indexes = {@Index(name = "idx_transfer_user_date", columnList = "user_id,created_at"), @Index(name = "idx_transfer_reference", columnList = "reference_number")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 40)
    private String referenceNumber;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "source_account_id", nullable = false)
    private BankAccount sourceAccount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beneficiary_id")
    private Beneficiary beneficiary;
    @Column(nullable = false, length = 20)
    private String destinationAccountNumber;
    @Column(nullable = false, length = 11)
    private String destinationIfsc;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransferMode mode;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;
    @Column(length = 255)
    private String remarks;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private TransactionStatus status = TransactionStatus.PENDING;
    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
