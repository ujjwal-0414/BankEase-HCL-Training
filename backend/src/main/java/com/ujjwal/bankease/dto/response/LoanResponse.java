package com.ujjwal.bankease.dto.response;

import com.ujjwal.bankease.enums.LoanStatus;
import com.ujjwal.bankease.enums.LoanType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanResponse {
    private Long id;
    private String applicationNumber;
    private LoanType loanType;
    private BigDecimal requestedAmount;
    private Integer tenureMonths;
    private BigDecimal interestRate;
    private BigDecimal emiAmount;
    private LoanStatus status;
    private String decisionRemarks;
    private LocalDateTime createdAt;
    private LocalDateTime decidedAt;
}
