package service;

import dto.AuthRequest;
import dto.AuthResponse;
import exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import security.UserDetailsServiceImpl;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    public void register(AuthRequest authRequest) {
        if (userDetailsService.exists(authRequest.username())) {
            throw new UserAlreadyExistsException("User already exists: " + authRequest.username());
        }
        var user = User.builder()
                .username(authRequest.username())
                .password(passwordEncoder.encode(authRequest.password()))
                .roles("USER")
                .build();
        userDetailsService.save(user);
    }

    public AuthResponse login(AuthRequest authRequest) {
        UserDetails user = userDetailsService.loadUserByUsername(authRequest.username());
        if (!passwordEncoder.matches(authRequest.password(), user.getPassword())) {
            throw new BadCredentialsException("Bad credentials");
        }
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
