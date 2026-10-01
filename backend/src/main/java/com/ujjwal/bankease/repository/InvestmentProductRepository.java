package com.ujjwal.bankease.repository;

import com.ujjwal.bankease.entity.InvestmentProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvestmentProductRepository extends JpaRepository<InvestmentProduct, Long> {
    List<InvestmentProduct> findByActiveTrue();
}