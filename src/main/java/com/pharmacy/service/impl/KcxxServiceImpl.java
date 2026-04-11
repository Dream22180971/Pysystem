package com.pharmacy.service.impl;

import com.pharmacy.bean.Kcxx;
import com.pharmacy.mapper.KcxxMapper;
import com.pharmacy.service.KcxxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 药品库存服务实现类
 * 实现药品库存相关的业务逻辑
 */
@Service
public class KcxxServiceImpl implements KcxxService {

    /**
     * 库存Mapper，用于数据库操作
     */
    @Autowired
    private KcxxMapper kcxxMapper;

    @Override
    public int add(Kcxx kcxx) {
        return kcxxMapper.insert(kcxx);
    }

    @Override
    public int update(Kcxx kcxx) {
        return kcxxMapper.update(kcxx);
    }

    @Override
    public int delete(Integer kid) {
        return kcxxMapper.delete(kid);
    }

    @Override
    public Kcxx getById(Integer kid) {
        return kcxxMapper.selectById(kid);
    }

    @Override
    public List<Kcxx> getAll() {
        return kcxxMapper.selectAll();
    }

    @Override
    public List<Kcxx> getByRid(Integer rid) {
        return kcxxMapper.selectByRid(rid);
    }

    @Override
    public Kcxx getByDrugsName(String drugsName) {
        return kcxxMapper.selectByDrugsName(drugsName);
    }

    @Override
    public List<Kcxx> getWarningList() {
        // 获取所有库存记录
        List<Kcxx> all = kcxxMapper.selectAll();
        // 存储库存不足的记录
        List<Kcxx> warningList = new ArrayList<>();
        // 遍历库存记录，筛选出数量小于60的药品
        for (Kcxx kcxx : all) {
            if (kcxx.getNum() < 60) {
                warningList.add(kcxx);
            }
        }
        return warningList;
    }
}