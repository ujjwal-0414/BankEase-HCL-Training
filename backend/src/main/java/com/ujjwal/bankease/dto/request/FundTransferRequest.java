package com.ujjwal.bankease.dto.request;

import com.ujjwal.bankease.enums.TransferMode;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundTransferRequest {
    @NotNull
    private Long sourceAccountId;
    @NotNull
    private Long beneficiaryId;
    @NotNull
    @DecimalMin("0.01")
    @Digits(integer = 17, fraction = 2)
    private BigDecimal amount;
    @NotNull
    private TransferMode mode;
    @Size(max = 255)
    private String remarks;
}
