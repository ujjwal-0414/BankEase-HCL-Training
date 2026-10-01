package com.ujjwal.bankease.service;

import com.ujjwal.bankease.dto.request.LoanApplicationRequest;
import com.ujjwal.bankease.dto.request.LoanDecisionRequest;
import com.ujjwal.bankease.dto.response.LoanResponse;

import java.util.List;

public interface LoanService {
    LoanResponse apply(LoanApplicationRequest r);

    List<LoanResponse> myLoans();

    List<LoanResponse> pending();

    LoanResponse decide(Long id, LoanDecisionRequest r);
}