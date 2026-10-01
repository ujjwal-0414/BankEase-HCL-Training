package com.ujjwal.bankease.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeneficiaryResponse {
    private Long id;
    private String name;
    private String accountNumber;
    private String ifscCode;
    private String bankName;
    private String nickname;
    private boolean active;
}
