package com.ujjwal.bankease.repository;

import com.ujjwal.bankease.entity.FundTransfer;
import com.ujjwal.bankease.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FundTransferRepository extends JpaRepository<FundTransfer, Long> {
    List<FundTransfer> findTop50ByUserOrderByCreatedAtDesc(User user);

    @Query("select coalesce(sum(t.amount),0) from FundTransfer t where t.user=:user and t.status='SUCCESS' and t.createdAt>=:start")
    BigDecimal sumSuccessfulTransfers(@org.springframework.data.repository.query.Param("user") User user, @org.springframework.data.repository.query.Param("start") LocalDateTime start);

    Optional<FundTransfer> findByReferenceNumber(String referenceNumber);
}