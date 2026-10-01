package com.ujjwal.bankease.dto.request;

import com.ujjwal.bankease.enums.LoanStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanDecisionRequest {
    @NotNull
    private LoanStatus status;
    @DecimalMin("0.00")
    private BigDecimal interestRate;
    @Size(max = 500)
    private String decisionRemarks;
}
