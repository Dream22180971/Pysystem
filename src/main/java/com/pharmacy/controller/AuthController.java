package com.pharmacy.controller;

import com.pharmacy.bean.Userinfo;
import com.pharmacy.security.JwtService;
import com.pharmacy.service.UserinfoService;
import com.pharmacy.util.ResultJson;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录接口：校验用户名/密码（MD5）后签发 JWT；角色由 {@code userinfo.P_id} 映射为 Spring Security 角色名。
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserinfoService userinfoService;
    private final JwtService jwtService;

    public AuthController(UserinfoService userinfoService, JwtService jwtService) {
        this.userinfoService = userinfoService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResultJson login(@Valid @RequestBody LoginRequest req) {
        boolean ok = userinfoService.login(req.username(), req.password());
        if (!ok) {
            return ResultJson.error(401, "用户名或密码错误（或账号被禁用）");
        }

        Userinfo user = userinfoService.getByUsername(req.username());
        String role = roleFromPid(user == null ? null : user.getpId());

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        String token = jwtService.issueToken(req.username(), claims);

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("tokenType", "Bearer");
        data.put("username", req.username());
        data.put("role", role);

        return ResultJson.success(data);
    }

    /** 与库表 part.P_id 约定：1 管理员，2 员工 */
    private static String roleFromPid(Integer pid) {
        if (pid == null) {
            return "ROLE_USER";
        }
        return switch (pid) {
            case 1 -> "ROLE_ADMIN";
            case 2 -> "ROLE_EMP";
            default -> "ROLE_USER";
        };
    }

    public record LoginRequest(
            @NotBlank String username,
            @NotBlank String password
    ) {}
}

