package com.ujjwal.bankease.service.impl;

import com.ujjwal.bankease.dto.request.CreateAccountRequest;
import com.ujjwal.bankease.dto.response.AccountResponse;
import com.ujjwal.bankease.entity.BankAccount;
import com.ujjwal.bankease.entity.User;
import com.ujjwal.bankease.exception.ResourceNotFoundException;
import com.ujjwal.bankease.mapper.BankEaseMapper;
import com.ujjwal.bankease.repository.BankAccountRepository;
import com.ujjwal.bankease.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final BankAccountRepository repo;
    private final BaseService base;
    private final BankEaseMapper mapper;

    @Override
    @Transactional
    public AccountResponse create(CreateAccountRequest r) {
        User u = base.currentUser();
        BigDecimal deposit = Optional.ofNullable(r.getInitialDeposit()).orElse(BigDecimal.ZERO);
        BankAccount a = BankAccount.builder().accountNumber(generateAccountNumber()).ifscCode("BKEZ0000001").accountType(r.getAccountType()).balance(deposit).user(u).build();
        return mapper.toAccount(repo.save(a));
    }

    @Override
    public List<AccountResponse> myAccounts() {
        return repo.findByUser(base.currentUser()).stream().map(mapper::toAccount).toList();
    }

    @Override
    public AccountResponse get(Long id) {
        return mapper.toAccount(repo.findByIdAndUser(id, base.currentUser()).orElseThrow(() -> new ResourceNotFoundException("Bank account not found")));
    }

    private String generateAccountNumber() {
        String n;
        do {
            n = "60" + String.format("%018d", Math.abs(UUID.randomUUID().getMostSignificantBits()) % 1000000000000000000L);
        } while (repo.findByAccountNumber(n).isPresent());
        return n;
    }
}
