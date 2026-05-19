package com.pharmacy.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

/**
 * JWT 签发与校验（HS256）。subject 存用户名；claims 中可放 role 等，供 {@link JwtAuthFilter} 还原权限。
 * 密钥长度须满足 HMAC-SHA256 要求，见 {@code application.yml} 中 {@code app.security.jwt.secret}。
 */
@Service
public class JwtService {
    private final SecretKey key;
    private final long ttlSeconds;

    public JwtService(
            @Value("${app.security.jwt.secret}") String secret,
            @Value("${app.security.jwt.ttl-seconds}") long ttlSeconds
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.ttlSeconds = ttlSeconds;
    }

    /** 签发访问令牌，过期时间由 {@code app.security.jwt.ttl-seconds} 决定 */
    public String issueToken(String subject, Map<String, Object> claims) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(ttlSeconds)))
                .claims(claims)
                .signWith(key)
                .compact();
    }

    /** 解析并验签；签名无效或过期时由 jjwt 抛异常，由过滤器捕获 */
    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

