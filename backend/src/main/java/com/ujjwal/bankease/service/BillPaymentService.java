package com.ujjwal.bankease.service;

import com.ujjwal.bankease.dto.request.BillPaymentRequest;
import com.ujjwal.bankease.dto.response.BillPaymentResponse;

import java.util.List;

public interface BillPaymentService {
    BillPaymentResponse pay(BillPaymentRequest r);

    List<BillPaymentResponse> history();
}