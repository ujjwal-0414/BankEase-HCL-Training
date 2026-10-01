package com.ujjwal.bankease.service;

import com.ujjwal.bankease.dto.response.AdminUserResponse;
import com.ujjwal.bankease.dto.response.UserResponse;
import com.ujjwal.bankease.enums.KycStatus;

import java.util.List;

public interface AdminService {
    List<AdminUserResponse> users();

    UserResponse updateKyc(Long userId, KycStatus status);

    UserResponse setEnabled(Long userId, boolean enabled);
}