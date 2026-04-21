package com.pharmacy.controller;

import com.pharmacy.bean.AuditLog;
import com.pharmacy.mapper.AuditLogMapper;
import com.pharmacy.util.ResultJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 审计日志只读列表；需登录（JWT）。表结构见 {@code audit_log.sql}。
 */
@RestController
@RequestMapping("/api/audit")
public class AuditLogController {

    @Autowired
    private AuditLogMapper auditLogMapper;

    @GetMapping("/list")
    public ResultJson list() {
        List<AuditLog> list = auditLogMapper.selectAllOrderByIdDesc();
        return ResultJson.success(list);
    }
}
