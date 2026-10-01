package com.ujjwal.bankease.service.impl;

import com.ujjwal.bankease.dto.request.LoginRequest;
import com.ujjwal.bankease.dto.request.RegisterRequest;
import com.ujjwal.bankease.dto.response.AuthResponse;
import com.ujjwal.bankease.entity.AuditLog;
import com.ujjwal.bankease.entity.Role;
import com.ujjwal.bankease.entity.User;
import com.ujjwal.bankease.enums.AuditAction;
import com.ujjwal.bankease.enums.RoleType;
import com.ujjwal.bankease.exception.BusinessException;
import com.ujjwal.bankease.repository.AuditLogRepository;
import com.ujjwal.bankease.repository.RoleRepository;
import com.ujjwal.bankease.repository.UserRepository;
import com.ujjwal.bankease.security.CustomUserDetails;
import com.ujjwal.bankease.security.JwtService;
import com.ujjwal.bankease.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager manager;
    private final JwtService jwtService;
    private final AuditLogRepository audit;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest r) {
        if (userRepository.existsByEmail(r.getEmail().toLowerCase()))
            throw new BusinessException("Email is already registered");
        Role role = roleRepository.findByName(RoleType.USER).orElseThrow(() -> new BusinessException("USER role is not initialized"));
        User u = User.builder().firstName(r.getFirstName()).lastName(r.getLastName()).email(r.getEmail().toLowerCase()).password(encoder.encode(r.getPassword())).phoneNumber(r.getPhoneNumber()).role(role).build();
        userRepository.save(u);
        audit.save(AuditLog.builder().user(u).action(AuditAction.REGISTER).description("New customer registered").build());
        return response(u);
    }

    @Override
    public AuthResponse login(LoginRequest r) {
        try {
            manager.authenticate(new UsernamePasswordAuthenticationToken(r.getEmail().toLowerCase(), r.getPassword()));
            User u = userRepository.findWithRoleByEmail(r.getEmail().toLowerCase()).orElseThrow();
            u.setFailedLoginAttempts(0);
            userRepository.save(u);
            audit.save(AuditLog.builder().user(u).action(AuditAction.LOGIN_SUCCESS).description("Successful login").build());
            return response(u);
        } catch (AuthenticationException e) {
            userRepository.findByEmail(r.getEmail().toLowerCase()).ifPresent(u -> {
                u.setFailedLoginAttempts(u.getFailedLoginAttempts() + 1);
                userRepository.save(u);
                audit.save(AuditLog.builder().user(u).action(AuditAction.LOGIN_FAILED).description("Failed login attempt").build());
            });
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    private AuthResponse response(User u) {
        String token = jwtService.generateToken(new CustomUserDetails(u));
        return AuthResponse.builder().token(token).tokenType("Bearer").userId(u.getId()).name(u.getFirstName() + " " + u.getLastName()).email(u.getEmail()).role(u.getRole().getName().name()).build();
    }
}
