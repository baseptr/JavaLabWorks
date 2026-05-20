package com.esdc.service;

import com.esdc.dto.AuthRequest;
import com.esdc.dto.AuthResponse;
import com.esdc.exception.UserAlreadyExistsException;
import com.esdc.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationManager authenticationManager;

    public void register(AuthRequest request) {
        if (userDetailsService.exists(request.username())) {
            throw new UserAlreadyExistsException("User already exists: " + request.username());
        }
        var user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .roles("USER")
                .build();
        userDetailsService.save(user);
    }

    public AuthResponse login(AuthRequest request) {
        /*UserDetails user = userDetailsService.loadUserByUsername(request.username());
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Bad credentials");
        }*/
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        var user = userDetailsService.loadUserByUsername(request.username());
        return new AuthResponse(jwtService.generateAccessToken(user), jwtService.generateRefreshToken(user));
    }

    public AuthResponse refresh(String refreshToken) {
        String username = jwtService.extractRefreshToken(refreshToken);
        if (!jwtService.isRefreshTokenStored(refreshToken, username)) {
            throw new RuntimeException("Invalid refresh token");
        }
        var user = userDetailsService.loadUserByUsername(username);
        return new AuthResponse(jwtService.generateAccessToken(user), refreshToken);
    }

    public void logout(String username) {
        jwtService.revokeRefreshToken(username);
    }
}
