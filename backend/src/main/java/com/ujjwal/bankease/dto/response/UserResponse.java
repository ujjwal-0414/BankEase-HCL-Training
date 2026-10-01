package com.ujjwal.bankease.dto.response;

import com.ujjwal.bankease.enums.KycStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private KycStatus kycStatus;
    private String role;
}
