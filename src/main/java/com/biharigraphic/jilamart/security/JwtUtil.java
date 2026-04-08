package com.biharigraphic.jilamart.security;

import com.biharigraphic.jilamart.enums.TokenType;
import com.biharigraphic.jilamart.exception.enums.ErrorCode;
import com.biharigraphic.jilamart.user.entity.User;
import com.biharigraphic.jilamart.user.exception.UserException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final Long accessTokenValidity = 1000 * 60 * 30L; // 30 min
    private final Long refreshTokenValidity = 1000 * 60 * 60 * 24 * 28L; // 28 days

    private Key getSigningKey() {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // ✅ ACCESS TOKEN
    public String generateAccessToken(User user) {
        return buildToken(user, accessTokenValidity, "ACCESS_TOKEN");
    }

    // ✅ REFRESH TOKEN
    public String generateRefreshToken(User user) {
        return buildToken(user, refreshTokenValidity, "REFRESH_TOKEN");
    }

    private String buildToken(User user, long validity, String type) {
        if (user == null)
            throw new UserException("User is null", ErrorCode.INVALID_USER.name());


        Date now = new Date();
        Date expiry = new Date(System.currentTimeMillis() + validity);

        log.info("NOW: {}", now);
        log.info("EXPIRY: {}", expiry);

        return Jwts.builder()
                .setSubject(user.getPhoneNumber())
                .claim("role", user.getRole().getName().name())
                .claim("type", type) // 🔥 important
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();


    }

    // ✅ Extract Claims (handles expired token)
    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            return e.getClaims(); // 🔥 very important
        }
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public String extractType(String token) {
        return extractAllClaims(token).get("type", String.class);
    }

    // ✅ Validate ACCESS token
    public boolean isAccessTokenValid(String token,TokenType type) {
        return isTokenValid(token, type);
    }

    // ✅ Validate REFRESH token
    public boolean isRefreshTokenValid(String token, TokenType type) {
        return isTokenValid(token,type);
    }

    public boolean isTokenValid(String token, TokenType expectedType) {
        try {
            Claims claims = extractAllClaims(token);

            String type = claims.get("type", String.class);

            if (!expectedType.name().equals(type)) {
                log.error("Invalid token type");
                return false;
            }

            return claims.getExpiration().after(new Date());

        } catch (JwtException e) {
            log.error("JWT error: {}", e.getMessage());
            return false;
        }
    }
}