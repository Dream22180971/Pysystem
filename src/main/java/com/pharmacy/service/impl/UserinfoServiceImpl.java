package com.pharmacy.service.impl;

import com.pharmacy.bean.Userinfo;
import com.pharmacy.mapper.UserinfoMapper;
import com.pharmacy.service.UserinfoService;
import com.pharmacy.util.MD5Util;
import com.pharmacy.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
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
            // 表字段为 date（无时分秒）：使用本地日期避免时区导致的“昨天/明天”偏移
            userinfo.setCreateTime(Date.valueOf(LocalDate.now()));
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

    @Override
    public PageResult<Userinfo> getPage(int page, int size) {
        return getPage(page, size, "createTime", "asc");
    }

    @Override
    public PageResult<Userinfo> getPage(int page, int size, String sortField, String sortOrder) {
        int p = Math.max(1, page);
        int s = Math.min(200, Math.max(1, size));
        int offset = (p - 1) * s;
        long total = userinfoMapper.countAll();
        List<Userinfo> items = userinfoMapper.selectPage(offset, s, sortField, sortOrder);
        // 列表不返回密码
        for (Userinfo u : items) {
            if (u != null) u.setPassword(null);
        }
        return new PageResult<>(items, total);
    }
}