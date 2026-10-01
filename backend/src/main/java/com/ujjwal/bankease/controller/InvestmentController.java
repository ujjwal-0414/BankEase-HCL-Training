package com.ujjwal.bankease.controller;

import com.ujjwal.bankease.dto.response.InvestmentProductResponse;
import com.ujjwal.bankease.dto.response.InvestmentResponse;
import com.ujjwal.bankease.dto.request.*;
import com.ujjwal.bankease.service.InvestmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/investments")
@RequiredArgsConstructor
public class InvestmentController {
    private final InvestmentService service;

    @GetMapping("/products")
    public List<InvestmentProductResponse> products() {
        return service.products();
    }

    @PostMapping
    public ResponseEntity<InvestmentResponse> invest(@Valid @RequestBody InvestmentRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.invest(r));
    }

    @GetMapping
    public List<InvestmentResponse> mine() {
        return service.myInvestments();
    }

    @PostMapping("/{id}/redeem")
    public InvestmentResponse redeem(@PathVariable Long id) {
        return service.redeem(id);
    }
}
