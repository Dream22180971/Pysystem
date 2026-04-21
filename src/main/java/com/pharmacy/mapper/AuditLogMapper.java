package com.pharmacy.mapper;

import com.pharmacy.bean.AuditLog;

import java.util.List;

public interface AuditLogMapper {

    List<AuditLog> selectAllOrderByIdDesc();
}
