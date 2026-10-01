package com.ujjwal.bankease.controller;

import com.ujjwal.bankease.dto.request.BillPaymentRequest;
import com.ujjwal.bankease.dto.response.BillPaymentResponse;
import com.ujjwal.bankease.service.BillPaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bill-payments")
@RequiredArgsConstructor
public class BillPaymentController {
    private final BillPaymentService service;

    @PostMapping
    public BillPaymentResponse pay(@Valid @RequestBody BillPaymentRequest r) {
        return service.pay(r);
    }

    @GetMapping
    public List<BillPaymentResponse> history() {
        return service.history();
    }
}
