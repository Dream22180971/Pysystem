package com.pharmacy.mapper;

import com.pharmacy.bean.Userinfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserinfoMapper {
    int insert(Userinfo userinfo);
    int update(Userinfo userinfo);
    int delete(Integer id);
    Userinfo selectById(Integer id);
    Userinfo selectByUsername(String username);
    List<Userinfo> selectAll();

    long countAll();

    List<Userinfo> selectPage(@Param("offset") int offset,
                              @Param("limit") int limit,
                              @Param("sortField") String sortField,
                              @Param("sortOrder") String sortOrder);
}