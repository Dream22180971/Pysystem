package com.pharmacy.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 从请求头 Authorization（Bearer 令牌）解析 JWT，写入 {@link SecurityContextHolder}。
 * 无头或非法 token 时不阻断请求链，由后续 {@link org.springframework.security.web.authorization.AuthorizationFilter} 对受保护路径返回 401。
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring("Bearer ".length()).trim();
        if (token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Claims claims = jwtService.parseClaims(token);
            String username = claims.getSubject();
            if (username == null || username.isBlank()) {
                filterChain.doFilter(request, response);
                return;
            }

            Collection<? extends GrantedAuthority> authorities = toAuthorities(claims);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException ignored) {
            // Invalid/expired token: treat as unauthenticated
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private static Collection<? extends GrantedAuthority> toAuthorities(Claims claims) {
        Object role = claims.get("role");
        if (role == null) {
            return List.of();
        }

        String roleStr = Objects.toString(role, "").trim();
        if (roleStr.isEmpty()) {
            return List.of();
        }

        List<SimpleGrantedAuthority> list = new ArrayList<>();
        // Expect "ROLE_ADMIN" / "ROLE_EMP"
        list.add(new SimpleGrantedAuthority(roleStr));
        return list;
    }
}

