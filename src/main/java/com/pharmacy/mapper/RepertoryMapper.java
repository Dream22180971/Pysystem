package com.pharmacy.mapper;

import com.pharmacy.bean.Repertory;
import java.util.List;

public interface RepertoryMapper {
    int insert(Repertory repertory);
    int update(Repertory repertory);
    int delete(Integer rid);
    Repertory selectById(Integer rid);
    List<Repertory> selectAll();
}