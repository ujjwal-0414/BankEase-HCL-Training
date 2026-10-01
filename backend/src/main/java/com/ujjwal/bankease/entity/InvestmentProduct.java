package com.ujjwal.bankease.entity;

import com.ujjwal.bankease.enums.InvestmentType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "investment_products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestmentProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 40)
    private String code;
    @Column(nullable = false, length = 120)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private InvestmentType type;
    @Column(nullable = false, precision = 7, scale = 4)
    private BigDecimal expectedAnnualReturn;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal minimumInvestment;
    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;
}
