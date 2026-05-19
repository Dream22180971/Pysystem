package com.pharmacy.service.impl;

import com.pharmacy.bean.Drugs;
import com.pharmacy.mapper.DrugsMapper;
import com.pharmacy.service.DrugsService;
import com.pharmacy.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 药品服务实现类
 * 实现药品相关的业务逻辑
 */
@Service
public class DrugsServiceImpl implements DrugsService {

    /**
     * 药品Mapper，用于数据库操作
     */
    @Autowired
    private DrugsMapper drugsMapper;

    @Override
    public int add(Drugs drugs) {
        return drugsMapper.insert(drugs);
    }

    @Override
    public int update(Drugs drugs) {
        return drugsMapper.update(drugs);
    }

    @Override
    public int delete(Integer id) {
        return drugsMapper.delete(id);
    }

    @Override
    public Drugs getById(Integer id) {
        return drugsMapper.selectById(id);
    }

    @Override
    public List<Drugs> getAll() {
        return drugsMapper.selectAll();
    }

    @Override
    public List<Drugs> getByCategoryId(Integer categoryId) {
        return drugsMapper.selectByCategoryId(categoryId);
    }

    @Override
    public PageResult<Drugs> getPage(int page, int size) {
        return getPage(page, size, "id", "asc");
    }

    @Override
    public PageResult<Drugs> getPage(int page, int size, String sortField, String sortOrder) {
        int p = Math.max(1, page);
        int s = Math.min(200, Math.max(1, size));
        int offset = (p - 1) * s;
        long total = drugsMapper.countAll();
        List<Drugs> items = drugsMapper.selectPage(offset, s, sortField, sortOrder);
        return new PageResult<>(items, total);
    }
}