package com.pharmacy.mapper;

import com.pharmacy.bean.AuditLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AuditLogMapper {

    List<AuditLog> selectAllOrderByIdDesc();

    long countAll();

    List<AuditLog> selectPageOrderByIdDesc(@Param("offset") int offset,
                                          @Param("limit") int limit,
                                          @Param("sortField") String sortField,
                                          @Param("sortOrder") String sortOrder);
}
