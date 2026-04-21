package com.pharmacy.service;

import com.pharmacy.bean.Drugs;
import com.pharmacy.vo.PageResult;

import java.util.List;

/**
 * 药品服务接口
 * 提供药品相关的业务逻辑操作
 */
public interface DrugsService {
    /**
     * 添加药品
     * @param drugs 药品信息对象
     * @return 影响行数
     */
    int add(Drugs drugs);
    
    /**
     * 更新药品信息
     * @param drugs 药品信息对象
     * @return 影响行数
     */
    int update(Drugs drugs);
    
    /**
     * 删除药品
     * @param id 药品ID
     * @return 影响行数
     */
    int delete(Integer id);
    
    /**
     * 根据ID获取药品信息
     * @param id 药品ID
     * @return 药品信息对象
     */
    Drugs getById(Integer id);
    
    /**
     * 获取所有药品
     * @return 药品信息列表
     */
    List<Drugs> getAll();
    
    /**
     * 根据分类ID获取药品
     * @param categoryId 分类ID
     * @return 药品信息列表
     */
    List<Drugs> getByCategoryId(Integer categoryId);

    PageResult<Drugs> getPage(int page, int size);

    PageResult<Drugs> getPage(int page, int size, String sortField, String sortOrder);
}