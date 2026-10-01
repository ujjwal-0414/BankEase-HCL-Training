package com.ujjwal.bankease.repository;

import com.ujjwal.bankease.entity.BankAccount;
import com.ujjwal.bankease.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    List<BankAccount> findByUser(User user);

    Optional<BankAccount> findByIdAndUser(Long id, User user);

    Optional<BankAccount> findByAccountNumber(String accountNumber);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from BankAccount a where a.id=:id")
    Optional<BankAccount> findByIdForUpdate(@org.springframework.data.repository.query.Param("id") Long id);
}