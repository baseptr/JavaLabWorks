package com.esdc.booking.service;

import com.esdc.booking.api.dto.AuthRequest;
import com.esdc.booking.api.dto.AuthResponse;
import com.esdc.booking.entity.Role;
import com.esdc.booking.entity.User;
import com.esdc.booking.exception.UserAlreadyExistsException;
import com.esdc.booking.repository.UserRepository;
import com.esdc.booking.security.JwtService;
import com.esdc.booking.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public void register(AuthRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new UserAlreadyExistsException(request.username());
        }
        userRepository.save(User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build());
    }

    public AuthResponse login(AuthRequest request) {
        var user = userDetailsService.loadUserByUsername(request.username());
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Bad credentials");
        }
        return new AuthResponse(jwtService.generateAccessToken(user), jwtService.generateRefreshToken(user));
    }

    public AuthResponse refresh(String refreshToken) {
        String username = jwtService.extractRefreshSubject(refreshToken);
        if (!jwtService.isRefreshTokenStored(refreshToken, username)) {
            throw new BadCredentialsException("Invalid refresh token");
        }
        var user = userDetailsService.loadUserByUsername(username);
        return new AuthResponse(jwtService.generateAccessToken(user), refreshToken);
    }

    public void logout(String username) {
        jwtService.revokeRefreshToken(username);
    }
}
