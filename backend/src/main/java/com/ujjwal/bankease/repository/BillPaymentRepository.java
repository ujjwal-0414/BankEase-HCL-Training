package com.ujjwal.bankease.repository;

import com.ujjwal.bankease.entity.BillPayment;
import com.ujjwal.bankease.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillPaymentRepository extends JpaRepository<BillPayment, Long> {
    List<BillPayment> findTop50ByUserOrderByCreatedAtDesc(User user);
}