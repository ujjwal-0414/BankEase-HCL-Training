package com.ujjwal.bankease.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeneficiaryRequest {
    @NotBlank
    @Size(max = 100)
    private String name;
    @NotBlank
    @Size(max = 20)
    private String accountNumber;
    @NotBlank
    @Size(max = 11)
    private String ifscCode;
    @Size(max = 100)
    private String bankName;
    @Size(max = 50)
    private String nickname;
}
