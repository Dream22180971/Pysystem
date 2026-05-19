package com.pharmacy.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * 无状态会话 + JWT：除登录与健康检查外，其余请求需携带合法 Bearer Token。
 * {@link com.pharmacy.security.JwtAuthFilter} 在 UsernamePasswordAuthenticationFilter 之前执行。
 * <p>RBAC：管理员全量；员工可操作业务数据但不可访问用户管理与审计；其它角色仅保留基础只读能力（由 JWT 角色与路由控制）。</p>
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthFilter jwtAuthFilter
    ) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // 浏览器 CORS 预检
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/actuator/health")).permitAll()
                        // 仅管理员：审计日志、用户（员工）管理
                        .requestMatchers("/api/audit/**").hasRole("ADMIN")
                        .requestMatchers("/api/user/**").hasRole("ADMIN")
                        // 知识库：读取/检索需登录；重建索引仅管理员
                        .requestMatchers(HttpMethod.POST, "/api/kb/resync").hasRole("ADMIN")
                        // 药智助手：需登录（内部助手）
                        .requestMatchers(HttpMethod.POST, "/api/ai/chat").authenticated()
                        // 销售/采购数据仅管理员与员工可读
                        .requestMatchers(HttpMethod.GET, "/api/sale/**", "/api/purchase/**")
                        .hasAnyRole("ADMIN", "EMP")
                        // 业务写操作：药品/分类/销售/采购/库存
                        .requestMatchers(HttpMethod.POST, "/api/drugs/**", "/api/category/**", "/api/sale/**", "/api/purchase/**", "/api/kcxx/**")
                        .hasAnyRole("ADMIN", "EMP")
                        // 项目内删除多为 GET …/delete
                        .requestMatchers(new AntPathRequestMatcher("/api/*/delete")).hasAnyRole("ADMIN", "EMP")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(eh -> eh
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                            String uri = request.getRequestURI();
                            response.getWriter().write("{\"code\":401,\"message\":\"未登录或Token无效\",\"data\":{\"uri\":\"" + uri + "\"}}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                            String uri = request.getRequestURI();
                            response.getWriter().write("{\"code\":403,\"message\":\"权限不足\",\"data\":{\"uri\":\"" + uri + "\"}}");
                        })
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(
            @Value("${app.cors.allowed-origin-patterns}") String allowedOriginPatterns
    ) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(splitCsv(allowedOriginPatterns));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    private static List<String> splitCsv(String csv) {
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();
    }
}

