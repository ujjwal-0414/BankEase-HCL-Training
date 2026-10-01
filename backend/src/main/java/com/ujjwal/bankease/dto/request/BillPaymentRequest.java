package com.ujjwal.bankease.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillPaymentRequest {
    @NotNull
    private Long accountId;
    @NotBlank
    @Size(max = 60)
    private String billerName;
    @NotBlank
    @Size(max = 40)
    private String billerCategory;
    @NotBlank
    @Size(max = 60)
    private String consumerNumber;
    @NotNull
    @DecimalMin("1.00")
    @Digits(integer = 17, fraction = 2)
    private BigDecimal amount;
}
