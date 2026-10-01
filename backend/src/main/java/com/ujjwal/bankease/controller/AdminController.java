package com.ujjwal.bankease.controller;

import com.ujjwal.bankease.dto.request.LoanDecisionRequest;
import com.ujjwal.bankease.dto.response.AdminUserResponse;
import com.ujjwal.bankease.dto.response.LoanResponse;
import com.ujjwal.bankease.dto.response.UserResponse;
import com.ujjwal.bankease.enums.KycStatus;
import com.ujjwal.bankease.service.AdminService;
import com.ujjwal.bankease.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService admin;
    private final LoanService loans;

    @GetMapping("/users")
    public List<AdminUserResponse> users() {
        return admin.users();
    }

    @PatchMapping("/users/{id}/kyc")
    public UserResponse kyc(@PathVariable Long id, @RequestParam KycStatus status) {
        return admin.updateKyc(id, status);
    }

    @PatchMapping("/users/{id}/enabled")
    public UserResponse enabled(@PathVariable Long id, @RequestParam boolean value) {
        return admin.setEnabled(id, value);
    }

    @GetMapping("/loans/pending")
    public List<LoanResponse> pendingLoans() {
        return loans.pending();
    }

    @PatchMapping("/loans/{id}/decision")
    public LoanResponse decideLoan(@PathVariable Long id, @Valid @RequestBody LoanDecisionRequest r) {
        return loans.decide(id, r);
    }
}
