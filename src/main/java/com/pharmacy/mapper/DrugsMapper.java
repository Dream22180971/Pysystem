package com.pharmacy.mapper;

import com.pharmacy.bean.Drugs;
import java.util.List;

public interface DrugsMapper {
    int insert(Drugs drugs);
    int update(Drugs drugs);
    int delete(Integer id);
    Drugs selectById(Integer id);
    List<Drugs> selectAll();
    List<Drugs> selectByCategoryId(Integer categoryId);
}