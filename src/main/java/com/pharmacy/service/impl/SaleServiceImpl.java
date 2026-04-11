package com.pharmacy.service.impl;

import com.pharmacy.bean.Sale;
import com.pharmacy.mapper.SaleMapper;
import com.pharmacy.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 药品销售服务实现类
 * 实现药品销售相关的业务逻辑
 */
@Service
public class SaleServiceImpl implements SaleService {

    /**
     * 销售Mapper，用于数据库操作
     */
    @Autowired
    private SaleMapper saleMapper;

    @Override
    public int add(Sale sale) {
        return saleMapper.insert(sale);
    }

    @Override
    public int update(Sale sale) {
        return saleMapper.update(sale);
    }

    @Override
    public int delete(Integer saleId) {
        return saleMapper.delete(saleId);
    }

    @Override
    public Sale getById(Integer saleId) {
        return saleMapper.selectById(saleId);
    }

    @Override
    public List<Sale> getAll() {
        return saleMapper.selectAll();
    }
}