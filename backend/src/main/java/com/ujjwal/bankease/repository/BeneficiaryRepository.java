package com.ujjwal.bankease.repository;

import com.ujjwal.bankease.entity.Beneficiary;
import com.ujjwal.bankease.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
    List<Beneficiary> findByUser(User user);

    Optional<Beneficiary> findByIdAndUser(Long id, User user);
}