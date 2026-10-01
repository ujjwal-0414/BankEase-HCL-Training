package com.ujjwal.bankease.dto.response;

import com.ujjwal.bankease.enums.InvestmentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestmentResponse {
    private Long id;
    private String referenceNumber;
    private Long productId;
    private String productName;
    private BigDecimal principalAmount;
    private BigDecimal currentValue;
    private InvestmentStatus status;
    private LocalDateTime purchasedAt;
    private LocalDateTime redeemedAt;
}
