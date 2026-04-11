package com.pharmacy.mapper;

import com.pharmacy.bean.Category;
import java.util.List;

public interface CategoryMapper {
    int insert(Category category);
    int update(Category category);
    int delete(Integer categoryId);
    Category selectById(Integer categoryId);
    List<Category> selectAll();
}