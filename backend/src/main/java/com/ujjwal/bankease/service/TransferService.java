package com.ujjwal.bankease.service;

import com.ujjwal.bankease.dto.request.FundTransferRequest;
import com.ujjwal.bankease.dto.response.TransferResponse;

import java.util.List;

public interface TransferService {
    TransferResponse transfer(FundTransferRequest r);

    List<TransferResponse> history();
}