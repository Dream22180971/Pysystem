package com.pharmacy.service;

import com.pharmacy.bean.Userinfo;
import java.util.List;

/**
 * 用户服务接口
 * 提供用户相关的业务逻辑操作
 */
public interface UserinfoService {
    /**
     * 添加用户
     * @param userinfo 用户信息对象
     * @return 影响行数
     */
    int add(Userinfo userinfo);
    
    /**
     * 更新用户信息
     * @param userinfo 用户信息对象
     * @return 影响行数
     */
    int update(Userinfo userinfo);
    
    /**
     * 删除用户
     * @param id 用户ID
     * @return 影响行数
     */
    int delete(Integer id);
    
    /**
     * 根据ID获取用户信息
     * @param id 用户ID
     * @return 用户信息对象
     */
    Userinfo getById(Integer id);
    
    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 用户信息对象
     */
    Userinfo getByUsername(String username);
    
    /**
     * 获取所有用户
     * @return 用户信息列表
     */
    List<Userinfo> getAll();
    
    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录是否成功
     */
    boolean login(String username, String password);
}