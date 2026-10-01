package com.ujjwal.bankease.dto.request;

import com.ujjwal.bankease.enums.LoanType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApplicationRequest {
    @NotNull
    private LoanType loanType;
    @NotNull
    @DecimalMin("10000.00")
    @Digits(integer = 17, fraction = 2)
    private BigDecimal requestedAmount;
    @NotNull
    @Min(6)
    @Max(360)
    private Integer tenureMonths;
}
