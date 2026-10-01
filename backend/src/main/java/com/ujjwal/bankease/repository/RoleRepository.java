package com.ujjwal.bankease.repository;

import com.ujjwal.bankease.entity.Role;
import com.ujjwal.bankease.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);
}