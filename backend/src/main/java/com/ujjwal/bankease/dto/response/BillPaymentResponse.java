package com.ujjwal.bankease.dto.response;

import com.ujjwal.bankease.enums.BillPaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillPaymentResponse {
    private Long id;
    private String referenceNumber;
    private Long accountId;
    private String billerName;
    private String billerCategory;
    private String consumerNumber;
    private BigDecimal amount;
    private BillPaymentStatus status;
    private LocalDateTime createdAt;
}
