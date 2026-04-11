package com.pharmacy.mapper;

import com.pharmacy.bean.Part;
import java.util.List;

public interface PartMapper {
    int insert(Part part);
    int update(Part part);
    int delete(Integer pId);
    Part selectById(Integer pId);
    List<Part> selectAll();
}