package com.pharmacy.controller;

import com.pharmacy.mapper.ReportMapper;
import com.pharmacy.util.ResultJson;
import com.pharmacy.vo.ReportDayAggVO;
import com.pharmacy.vo.ReportDrugAggVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 数据报表：从系统业务表（sale/purchase/kcxx）做 SQL 聚合，支持时间范围筛选。
 */
@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private ReportMapper reportMapper;

    @GetMapping("/sales/drug")
    public ResultJson salesByDrug(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
                                  @RequestParam(required = false, defaultValue = "20") Integer limit) {
        List<ReportDrugAggVO> list = reportMapper.selectSalesByDrug(start, end, limit);
        return ResultJson.success(list);
    }

    @GetMapping("/sales/day")
    public ResultJson salesByDay(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                                 @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date end) {
        List<ReportDayAggVO> list = reportMapper.selectSalesByDay(start, end);
        return ResultJson.success(list);
    }

    @GetMapping("/purchase/drug")
    public ResultJson purchaseByDrug(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
                                     @RequestParam(required = false, defaultValue = "20") Integer limit) {
        List<ReportDrugAggVO> list = reportMapper.selectPurchaseByDrug(start, end, limit);
        return ResultJson.success(list);
    }

    @GetMapping("/inventory/low")
    public ResultJson lowStock(@RequestParam(required = false, defaultValue = "10") Integer threshold,
                               @RequestParam(required = false, defaultValue = "50") Integer limit) {
        if (threshold == null || threshold < 0) threshold = 0;
        List<ReportDrugAggVO> list = reportMapper.selectLowStock(threshold, limit);
        return ResultJson.success(list);
    }
}

