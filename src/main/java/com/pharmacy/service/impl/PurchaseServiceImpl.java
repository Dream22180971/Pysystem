package com.pharmacy.service.impl;

import com.pharmacy.bean.Purchase;
import com.pharmacy.mapper.PurchaseMapper;
import com.pharmacy.service.PurchaseService;
import com.pharmacy.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 药品采购服务实现类
 * 实现药品采购相关的业务逻辑
 */
@Service
public class PurchaseServiceImpl implements PurchaseService {

    /**
     * 采购Mapper，用于数据库操作
     */
    @Autowired
    private PurchaseMapper purchaseMapper;

    @Override
    public int add(Purchase purchase) {
        return purchaseMapper.insert(purchase);
    }

    @Override
    public int update(Purchase purchase) {
        return purchaseMapper.update(purchase);
    }

    @Override
    public int delete(Integer pid) {
        return purchaseMapper.delete(pid);
    }

    @Override
    public Purchase getById(Integer pid) {
        return purchaseMapper.selectById(pid);
    }

    @Override
    public List<Purchase> getAll() {
        return purchaseMapper.selectAll();
    }

    @Override
    public PageResult<Purchase> getPage(int page, int size) {
        return getPage(page, size, "indate", "asc");
    }

    @Override
    public PageResult<Purchase> getPage(int page, int size, String sortField, String sortOrder) {
        int p = Math.max(1, page);
        int s = Math.min(200, Math.max(1, size));
        int offset = (p - 1) * s;
        long total = purchaseMapper.countAll();
        List<Purchase> items = purchaseMapper.selectPage(offset, s, sortField, sortOrder);
        return new PageResult<>(items, total);
    }
}