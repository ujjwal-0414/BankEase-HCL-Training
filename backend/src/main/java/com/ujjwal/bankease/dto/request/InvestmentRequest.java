package com.ujjwal.bankease.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestmentRequest {
    @NotNull
    private Long accountId;
    @NotNull
    private Long productId;
    @NotNull
    @DecimalMin("1.00")
    @Digits(integer = 17, fraction = 2)
    private BigDecimal amount;
}
