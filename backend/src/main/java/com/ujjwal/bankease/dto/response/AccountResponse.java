package com.ujjwal.bankease.dto.response;

import com.ujjwal.bankease.enums.AccountStatus;
import com.ujjwal.bankease.enums.AccountType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse {
    private Long id;
    private String accountNumber;
    private String ifscCode;
    private AccountType accountType;
    private AccountStatus status;
    private BigDecimal balance;
    private BigDecimal dailyTransferLimit;
}
