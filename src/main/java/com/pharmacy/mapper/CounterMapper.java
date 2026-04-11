package com.pharmacy.mapper;

import com.pharmacy.bean.Counter;
import java.util.List;

public interface CounterMapper {
    int insert(Counter counter);
    int update(Counter counter);
    int delete(Integer cid);
    Counter selectById(Integer cid);
    List<Counter> selectAll();
}