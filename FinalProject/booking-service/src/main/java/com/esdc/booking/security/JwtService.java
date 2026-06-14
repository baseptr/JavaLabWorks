package com.esdc.booking.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JwtService {

    @Value("${jwt.secret.access}")
    private String accessKey;
    @Value("${jwt.secret.refresh}")
    private String refreshKey;
    @Value("#{${jwt.lifetime.access} * 60 * 60 * 1000}")
    private long accessExpirationMs;
    @Value("#{${jwt.lifetime.refresh} * 24 * 60 * 60 * 1000}")
    private long refreshExpirationMs;

    private final Map<String, String> refreshTokens = new ConcurrentHashMap<>();

    private SecretKey getAccessKey() {
        return Keys.hmacShaKeyFor(accessKey.getBytes(StandardCharsets.UTF_8));
    }

    private SecretKey getRefreshKey() {
        return Keys.hmacShaKeyFor(refreshKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(UserDetails user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("roles", user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .claim("type", "access")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessExpirationMs))
                .signWith(getAccessKey())
                .compact();
    }

    public String generateRefreshToken(UserDetails user) {
        var token = Jwts.builder()
                .subject(user.getUsername())
                .claim("type", "refresh")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpirationMs))
                .signWith(getRefreshKey())
                .compact();
        refreshTokens.put(user.getUsername(), token);
        return token;
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(getAccessKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String extractRefreshSubject(String token) {
        return Jwts.parser()
                .verifyWith(getRefreshKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isRefreshTokenStored(String token, String username) {
        return token.equals(refreshTokens.get(username));
    }

    public boolean isAccessTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getAccessKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public void revokeRefreshToken(String username) {
        refreshTokens.remove(username);
    }
}
