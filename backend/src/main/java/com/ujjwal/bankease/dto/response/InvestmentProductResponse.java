package com.ujjwal.bankease.dto.response;

import com.ujjwal.bankease.enums.InvestmentType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestmentProductResponse {
    private Long id;
    private String code;
    private String name;
    private InvestmentType type;
    private BigDecimal expectedAnnualReturn;
    private BigDecimal minimumInvestment;
    private boolean active;
}
