package com.pharmacy.mapper;

import com.pharmacy.bean.Kcxx;
import java.util.List;

public interface KcxxMapper {
    int insert(Kcxx kcxx);
    int update(Kcxx kcxx);
    int delete(Integer kid);
    Kcxx selectById(Integer kid);
    List<Kcxx> selectAll();
    List<Kcxx> selectByRid(Integer rid);
    Kcxx selectByDrugsName(String drugsName);
}