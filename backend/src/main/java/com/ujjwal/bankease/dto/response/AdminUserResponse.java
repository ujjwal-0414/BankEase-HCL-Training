package com.ujjwal.bankease.dto.response;

import com.ujjwal.bankease.enums.KycStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUserResponse {
    private Long id;
    private String name;
    private String email;
    private String role;
    private KycStatus kycStatus;
    private boolean enabled;
    private int failedLoginAttempts;
}
