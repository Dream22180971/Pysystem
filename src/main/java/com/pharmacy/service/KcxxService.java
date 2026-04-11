package com.pharmacy.service;

import com.pharmacy.bean.Kcxx;
import java.util.List;

/**
 * 药品库存服务接口
 * 提供药品库存相关的业务逻辑操作
 */
public interface KcxxService {
    /**
     * 添加库存记录
     * @param kcxx 库存信息对象
     * @return 影响行数
     */
    int add(Kcxx kcxx);
    
    /**
     * 更新库存记录
     * @param kcxx 库存信息对象
     * @return 影响行数
     */
    int update(Kcxx kcxx);
    
    /**
     * 删除库存记录
     * @param kid 库存ID
     * @return 影响行数
     */
    int delete(Integer kid);
    
    /**
     * 根据ID获取库存记录
     * @param kid 库存ID
     * @return 库存信息对象
     */
    Kcxx getById(Integer kid);
    
    /**
     * 获取所有库存记录
     * @return 库存信息列表
     */
    List<Kcxx> getAll();
    
    /**
     * 根据仓库ID获取库存记录
     * @param rid 仓库ID
     * @return 库存信息列表
     */
    List<Kcxx> getByRid(Integer rid);
    
    /**
     * 根据药品名称获取库存记录
     * @param drugsName 药品名称
     * @return 库存信息对象
     */
    Kcxx getByDrugsName(String drugsName);
    
    /**
     * 获取库存预警列表（库存不足的药品）
     * @return 库存预警列表
     */
    List<Kcxx> getWarningList();
}