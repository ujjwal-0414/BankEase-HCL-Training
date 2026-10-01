package com.ujjwal.bankease.service;

import com.ujjwal.bankease.dto.request.LoginRequest;
import com.ujjwal.bankease.dto.request.RegisterRequest;
import com.ujjwal.bankease.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}