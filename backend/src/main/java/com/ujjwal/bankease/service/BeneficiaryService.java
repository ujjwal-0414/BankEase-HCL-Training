package com.ujjwal.bankease.service;

import com.ujjwal.bankease.dto.request.BeneficiaryRequest;
import com.ujjwal.bankease.dto.response.BeneficiaryResponse;

import java.util.List;

public interface BeneficiaryService {
    BeneficiaryResponse add(BeneficiaryRequest r);

    List<BeneficiaryResponse> list();

    void deactivate(Long id);
}