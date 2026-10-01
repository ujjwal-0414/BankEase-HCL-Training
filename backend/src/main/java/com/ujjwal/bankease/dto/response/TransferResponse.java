package com.ujjwal.bankease.dto.response;

import com.ujjwal.bankease.enums.TransactionStatus;
import com.ujjwal.bankease.enums.TransferMode;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferResponse {
    private Long id;
    private String referenceNumber;
    private Long sourceAccountId;
    private String destinationAccountNumber;
    private String destinationIfsc;
    private TransferMode mode;
    private BigDecimal amount;
    private TransactionStatus status;
    private String remarks;
    private LocalDateTime createdAt;
}
