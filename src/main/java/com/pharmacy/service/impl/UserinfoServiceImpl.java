package com.pharmacy.service.impl;

import com.pharmacy.bean.Userinfo;
import com.pharmacy.mapper.UserinfoMapper;
import com.pharmacy.service.UserinfoService;
import com.pharmacy.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/** 用户业务：密码入库前 MD5；更新时若未传新密码则保留库中原哈希 */
@Service
public class UserinfoServiceImpl implements UserinfoService {

    @Autowired
    private UserinfoMapper userinfoMapper;

    @Override
    public int add(Userinfo userinfo) {
        userinfo.setPassword(MD5Util.encrypt(userinfo.getPassword()));
        if (userinfo.getCreateTime() == null) {
            userinfo.setCreateTime(new Date());
        }
        return userinfoMapper.insert(userinfo);
    }

    @Override
    public int update(Userinfo userinfo) {
        // 前端编辑常省略密码字段：空则沿用库中密文，避免被写成 null
        if (userinfo.getPassword() == null || userinfo.getPassword().isEmpty()) {
            Userinfo existing = userinfoMapper.selectById(userinfo.getId());
            if (existing != null) {
                userinfo.setPassword(existing.getPassword());
            }
        } else {
            userinfo.setPassword(MD5Util.encrypt(userinfo.getPassword()));
        }
        return userinfoMapper.update(userinfo);
    }

    @Override
    public int delete(Integer id) {
        return userinfoMapper.delete(id);
    }

    @Override
    public Userinfo getById(Integer id) {
        return userinfoMapper.selectById(id);
    }

    @Override
    public Userinfo getByUsername(String username) {
        return userinfoMapper.selectByUsername(username);
    }

    @Override
    public List<Userinfo> getAll() {
        return userinfoMapper.selectAll();
    }

    @Override
    public boolean login(String username, String password) {
        // 根据用户名查询用户
        Userinfo userinfo = userinfoMapper.selectByUsername(username);
        // 用户不存在
        if (userinfo == null) {
            return false;
        }
        // 用户被禁用
        if (userinfo.getStatus() == 0) {
            return false;
        }
        // 验证密码（对输入的密码进行MD5加密后与数据库中的密码比较）
        return userinfo.getPassword().equals(MD5Util.encrypt(password));
    }
}