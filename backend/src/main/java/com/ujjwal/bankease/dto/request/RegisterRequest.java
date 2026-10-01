package com.ujjwal.bankease.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank
    @Size(max = 60)
    private String firstName;
    @NotBlank
    @Size(max = 60)
    private String lastName;
    @NotBlank
    @Email
    @Size(max = 120)
    private String email;
    @NotBlank
    @Size(min = 8, max = 100)
    private String password;
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone number must contain 10 digits")
    private String phoneNumber;
}
