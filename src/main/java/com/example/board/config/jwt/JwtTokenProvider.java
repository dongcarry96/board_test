package com.example.board.config.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final SecretKey secretKey;
    private final long expirationMs;
    private final SecretKey emailSecretKey;
    private final long emailExpirationMs;


    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expirationMs,
            @Value("${jwt.email-secret}") String emailSecret,
            @Value("${jwt.email-expiration}") long emailExpirationMs
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
        this.emailSecretKey = Keys.hmacShaKeyFor(emailSecret.getBytes(StandardCharsets.UTF_8));
        this.emailExpirationMs = emailExpirationMs;
    }

    public String generateToken(String userId) {
        Date now = new Date();
        return Jwts.builder()
                .subject(userId)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationMs))
                .signWith(secretKey)
                .compact();
    }

    public String getUserId(String token) {
        return parseClaims(token, secretKey).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token, secretKey);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String generateEmailToken(String userId) {
        Date now = new Date();
        return Jwts.builder()
                .subject(userId)
                .claim("purpose", "email-verify")
                .issuedAt(now)
                .expiration(new Date(now.getTime() + emailExpirationMs))
                .signWith(emailSecretKey)
                .compact();
    }

    public String getUserIdFromEmailToken(String token) {
        Claims claims = parseClaims(token, emailSecretKey);

        if (!"email-verify".equals(claims.get("purpose"))) {
            throw new JwtException("이메일 인증용 토큰이 아닙니다.");
        }
        return claims.getSubject();
    }

    private Claims parseClaims(String token, SecretKey key) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}