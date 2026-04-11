package com.pharmacy.mapper;

import com.pharmacy.bean.Userinfo;
import java.util.List;

public interface UserinfoMapper {
    int insert(Userinfo userinfo);
    int update(Userinfo userinfo);
    int delete(Integer id);
    Userinfo selectById(Integer id);
    Userinfo selectByUsername(String username);
    List<Userinfo> selectAll();
}