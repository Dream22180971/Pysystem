package com.pharmacy.mapper;

import com.pharmacy.bean.Drugs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DrugsMapper {
    int insert(Drugs drugs);
    int update(Drugs drugs);
    int delete(Integer id);
    Drugs selectById(Integer id);
    List<Drugs> selectAll();
    List<Drugs> selectByCategoryId(Integer categoryId);

    long countAll();

    List<Drugs> selectPage(@Param("offset") int offset,
                           @Param("limit") int limit,
                           @Param("sortField") String sortField,
                           @Param("sortOrder") String sortOrder);
}