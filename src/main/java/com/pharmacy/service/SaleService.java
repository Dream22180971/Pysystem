package com.pharmacy.service;

import com.pharmacy.bean.Sale;
import java.util.List;

/**
 * 药品销售服务接口
 * 提供药品销售相关的业务逻辑操作
 */
public interface SaleService {
    /**
     * 添加销售记录
     * @param sale 销售信息对象
     * @return 影响行数
     */
    int add(Sale sale);
    
    /**
     * 更新销售记录
     * @param sale 销售信息对象
     * @return 影响行数
     */
    int update(Sale sale);
    
    /**
     * 删除销售记录
     * @param saleId 销售ID
     * @return 影响行数
     */
    int delete(Integer saleId);
    
    /**
     * 根据ID获取销售记录
     * @param saleId 销售ID
     * @return 销售信息对象
     */
    Sale getById(Integer saleId);
    
    /**
     * 获取所有销售记录
     * @return 销售信息列表
     */
    List<Sale> getAll();
}