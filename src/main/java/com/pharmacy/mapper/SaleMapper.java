package com.pharmacy.mapper;

import com.pharmacy.bean.Sale;
import java.util.List;

public interface SaleMapper {
    int insert(Sale sale);
    int update(Sale sale);
    int delete(Integer saleId);
    Sale selectById(Integer saleId);
    List<Sale> selectAll();
}