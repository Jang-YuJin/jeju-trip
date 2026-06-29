package com.trip.jeju.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {
    private final SecretKey key;
    private final long accessTokenValidity;
    private final long refreshTokenValidity;

    public JwtProvider(@Value("${jwt.secret}") String secret,
                       @Value("${jwt.access-token-validity}") long accessTokenValidity,
                       @Value("${jwt.refresh-token-validity}") long refreshTokenValidity) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenValidity = accessTokenValidity;
        this.refreshTokenValidity = refreshTokenValidity;
    }

    public String createAccessToken(Integer userId, String role) {
        return createToken(userId, role, accessTokenValidity);
    }

    public String createRefreshToken(Integer userId) {
        return createToken(userId, null, refreshTokenValidity);
    }

    private String createToken(Integer userId, String role, long validity) {
        Date now = new Date();
        JwtBuilder builder = Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(now)
                .expiration(new Date(now.getTime() + validity))
                .signWith(key);
        if (role != null) builder.claim("role", role);
        return builder.compact();
    }

    public Integer getUserId(String token) {
        return Integer.parseInt(parse(token).getPayload().getSubject());
    }

    public String getRole(String token) {
        return parse(token).getPayload().get("role", String.class);
    }

    public boolean validateToken(String token) {
        try {
            parse(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Jws<Claims> parse(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
    }
}
