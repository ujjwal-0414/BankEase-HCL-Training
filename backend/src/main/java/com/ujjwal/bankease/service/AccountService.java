package com.ujjwal.bankease.service;

import com.ujjwal.bankease.dto.request.CreateAccountRequest;
import com.ujjwal.bankease.dto.response.AccountResponse;

import java.util.List;

public interface AccountService {
    AccountResponse create(CreateAccountRequest request);

    List<AccountResponse> myAccounts();

    AccountResponse get(Long id);
}