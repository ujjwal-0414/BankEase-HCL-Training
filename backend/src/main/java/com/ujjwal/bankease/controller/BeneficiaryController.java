package com.ujjwal.bankease.controller;

import com.ujjwal.bankease.dto.request.BeneficiaryRequest;
import com.ujjwal.bankease.dto.response.BeneficiaryResponse;
import com.ujjwal.bankease.service.BeneficiaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beneficiaries")
@RequiredArgsConstructor
public class BeneficiaryController {
    private final BeneficiaryService service;

    @PostMapping
    public ResponseEntity<BeneficiaryResponse> add(@Valid @RequestBody BeneficiaryRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.add(r));
    }

    @GetMapping
    public List<BeneficiaryResponse> list() {
        return service.list();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        service.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
