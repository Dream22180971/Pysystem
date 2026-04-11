package com.pharmacy.service;

import com.pharmacy.bean.Purchase;
import java.util.List;

/**
 * 药品采购服务接口
 * 提供药品采购相关的业务逻辑操作
 */
public interface PurchaseService {
    /**
     * 添加采购记录
     * @param purchase 采购信息对象
     * @return 影响行数
     */
    int add(Purchase purchase);
    
    /**
     * 更新采购记录
     * @param purchase 采购信息对象
     * @return 影响行数
     */
    int update(Purchase purchase);
    
    /**
     * 删除采购记录
     * @param pid 采购ID
     * @return 影响行数
     */
    int delete(Integer pid);
    
    /**
     * 根据ID获取采购记录
     * @param pid 采购ID
     * @return 采购信息对象
     */
    Purchase getById(Integer pid);
    
    /**
     * 获取所有采购记录
     * @return 采购信息列表
     */
    List<Purchase> getAll();
}