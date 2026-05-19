package com.pharmacy.service.impl;

import com.pharmacy.bean.Category;
import com.pharmacy.mapper.CategoryMapper;
import com.pharmacy.service.CategoryService;
import com.pharmacy.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 药品分类服务实现类
 * 实现药品分类相关的业务逻辑
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    /**
     * 分类Mapper，用于数据库操作
     */
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public int add(Category category) {
        return categoryMapper.insert(category);
    }

    @Override
    public int update(Category category) {
        return categoryMapper.update(category);
    }

    @Override
    public int delete(Integer categoryId) {
        return categoryMapper.delete(categoryId);
    }

    @Override
    public Category getById(Integer categoryId) {
        return categoryMapper.selectById(categoryId);
    }

    @Override
    public List<Category> getAll() {
        return categoryMapper.selectAll();
    }

    @Override
    public PageResult<Category> getPage(int page, int size) {
        return getPage(page, size, "categoryId", "asc");
    }

    @Override
    public PageResult<Category> getPage(int page, int size, String sortField, String sortOrder) {
        int p = Math.max(1, page);
        int s = Math.min(200, Math.max(1, size));
        int offset = (p - 1) * s;
        long total = categoryMapper.countAll();
        boolean sortDesc = sortOrder != null && "desc".equalsIgnoreCase(sortOrder.trim());
        List<Category> items = categoryMapper.selectPage(offset, s, sortDesc);
        return new PageResult<>(items, total);
    }
}