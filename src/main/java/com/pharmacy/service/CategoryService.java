package com.pharmacy.service;

import com.pharmacy.bean.Category;
import java.util.List;

/**
 * 药品分类服务接口
 * 提供药品分类相关的业务逻辑操作
 */
public interface CategoryService {
    /**
     * 添加药品分类
     * @param category 分类信息对象
     * @return 影响行数
     */
    int add(Category category);
    
    /**
     * 更新药品分类
     * @param category 分类信息对象
     * @return 影响行数
     */
    int update(Category category);
    
    /**
     * 删除药品分类
     * @param categoryId 分类ID
     * @return 影响行数
     */
    int delete(Integer categoryId);
    
    /**
     * 根据ID获取分类信息
     * @param categoryId 分类ID
     * @return 分类信息对象
     */
    Category getById(Integer categoryId);
    
    /**
     * 获取所有分类
     * @return 分类信息列表
     */
    List<Category> getAll();
}