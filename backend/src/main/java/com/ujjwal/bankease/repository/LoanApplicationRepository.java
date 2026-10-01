package com.ujjwal.bankease.repository;

import com.ujjwal.bankease.entity.LoanApplication;
import com.ujjwal.bankease.entity.User;
import com.ujjwal.bankease.enums.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
    List<LoanApplication> findByUserOrderByCreatedAtDesc(User user);

    List<LoanApplication> findByStatusOrderByCreatedAtAsc(LoanStatus status);

    Optional<LoanApplication> findByIdAndUser(Long id, User user);
}