package com.pharmacy.mapper;

import com.pharmacy.bean.Purchase;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PurchaseMapper {
    int insert(Purchase purchase);
    int update(Purchase purchase);
    int delete(Integer pid);
    Purchase selectById(Integer pid);
    List<Purchase> selectAll();

    long countAll();

    List<Purchase> selectPage(@Param("offset") int offset,
                              @Param("limit") int limit,
                              @Param("sortField") String sortField,
                              @Param("sortOrder") String sortOrder);
}