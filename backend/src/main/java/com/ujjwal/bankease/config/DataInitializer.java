package com.ujjwal.bankease.config;

import com.ujjwal.bankease.entity.InvestmentProduct;
import com.ujjwal.bankease.entity.Role;
import com.ujjwal.bankease.entity.User;
import com.ujjwal.bankease.enums.InvestmentType;
import com.ujjwal.bankease.enums.KycStatus;
import com.ujjwal.bankease.enums.RoleType;
import com.ujjwal.bankease.repository.InvestmentProductRepository;
import com.ujjwal.bankease.repository.RoleRepository;
import com.ujjwal.bankease.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    private final RoleRepository roleRepository;
    private final InvestmentProductRepository productRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Bean
    CommandLineRunner init() {
        return args -> {
            Role user = roleRepository.findByName(RoleType.USER).orElseGet(() -> roleRepository.save(Role.builder().name(RoleType.USER).build()));
            Role admin = roleRepository.findByName(RoleType.ADMIN).orElseGet(() -> roleRepository.save(Role.builder().name(RoleType.ADMIN).build()));
            if (productRepository.count() == 0) {
                productRepository.save(InvestmentProduct.builder().code("FD-7.0").name("BankEase Fixed Deposit 7.00%").type(InvestmentType.FIXED_DEPOSIT).expectedAnnualReturn(new BigDecimal("7.0000")).minimumInvestment(new BigDecimal("1000.00")).build());
                productRepository.save(InvestmentProduct.builder().code("MF-12.0").name("BankEase Growth Mutual Fund").type(InvestmentType.MUTUAL_FUND).expectedAnnualReturn(new BigDecimal("12.0000")).minimumInvestment(new BigDecimal("500.00")).build());
                productRepository.save(InvestmentProduct.builder().code("BND-8.5").name("BankEase Government Bond Basket").type(InvestmentType.BOND).expectedAnnualReturn(new BigDecimal("8.5000")).minimumInvestment(new BigDecimal("5000.00")).build());
            }
            if (!userRepository.existsByEmail("admin@bankease.com")) {
                userRepository.save(User.builder().firstName("BankEase").lastName("Admin").email("admin@bankease.com").password(encoder.encode("Admin@12345")).role(admin).kycStatus(KycStatus.VERIFIED).build());
            }
        };
    }
}
