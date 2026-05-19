package com.pharmacy.mapper;

import com.pharmacy.bean.Sale;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SaleMapper {
    int insert(Sale sale);
    int update(Sale sale);
    int delete(Integer saleId);
    Sale selectById(Integer saleId);
    List<Sale> selectAll();

    long countAll();

    List<Sale> selectPage(@Param("offset") int offset,
                          @Param("limit") int limit,
                          @Param("sortField") String sortField,
                          @Param("sortOrder") String sortOrder);
}