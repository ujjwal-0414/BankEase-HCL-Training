package com.ujjwal.bankease.service;

import com.ujjwal.bankease.dto.request.InvestmentRequest;
import com.ujjwal.bankease.dto.response.InvestmentProductResponse;
import com.ujjwal.bankease.dto.response.InvestmentResponse;

import java.util.List;

public interface InvestmentService {
    List<InvestmentProductResponse> products();

    InvestmentResponse invest(InvestmentRequest r);

    List<InvestmentResponse> myInvestments();

    InvestmentResponse redeem(Long id);
}