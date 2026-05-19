package com.pharmacy.mapper;

import com.pharmacy.bean.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper {
    int insert(Category category);
    int update(Category category);
    int delete(Integer categoryId);
    Category selectById(Integer categoryId);
    List<Category> selectAll();

    long countAll();

    List<Category> selectPage(@Param("offset") int offset,
                              @Param("limit") int limit,
                              @Param("sortDesc") boolean sortDesc);
}