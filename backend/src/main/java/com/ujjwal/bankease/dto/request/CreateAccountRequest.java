package com.ujjwal.bankease.dto.request;

import com.ujjwal.bankease.enums.AccountType;
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
public class CreateAccountRequest {
    @NotNull
    private AccountType accountType;
    @DecimalMin("0.00")
    @Digits(integer = 17, fraction = 2)
    private BigDecimal initialDeposit;
}
