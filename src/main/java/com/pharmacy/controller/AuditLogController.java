package com.pharmacy.controller;

import com.pharmacy.bean.AuditLog;
import com.pharmacy.mapper.AuditLogMapper;
import com.pharmacy.util.ResultJson;
import com.pharmacy.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResultJson list(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "sortField", required = false, defaultValue = "createdAt") String sortField,
            @RequestParam(value = "sortOrder", required = false, defaultValue = "asc") String sortOrder
    ) {
        int p = Math.max(1, page == null ? 1 : page);
        int s = Math.min(200, Math.max(1, size == null ? 10 : size));
        int offset = (p - 1) * s;
        long total = auditLogMapper.countAll();
        List<AuditLog> items = auditLogMapper.selectPageOrderByIdDesc(offset, s, sortField, sortOrder);
        return ResultJson.success(new PageResult<>(items, total));
    }
}
