package com.pharmacy.controller;

import com.pharmacy.bean.Purchase;
import com.pharmacy.bean.Sale;
import com.pharmacy.service.PurchaseService;
import com.pharmacy.service.SaleService;
import com.pharmacy.util.ResultJson;
import com.pharmacy.vo.BarVO;
import com.pharmacy.vo.PieVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 统计图表数据：按药品名称汇总数量。销售→饼图，采购→柱状图（取前 12 名，避免项过多）。
 */
@RestController
@RequestMapping("/api/statistic")
public class StatisticController {

    @Autowired
    private SaleService saleService;

    @Autowired
    private PurchaseService purchaseService;

    @GetMapping("/getSalePie")
    public ResultJson getSalePie() {
        List<Sale> sales = saleService.getAll();
        Map<String, Integer> sum = new HashMap<>();
        for (Sale s : sales) {
            if (s.getDrugsName() == null) {
                continue;
            }
            sum.merge(s.getDrugsName(), s.getNum() == null ? 0 : s.getNum(), Integer::sum);
        }
        List<PieVO> list = sum.entrySet().stream()
                .map(e -> new PieVO(e.getKey(), e.getValue()))
                .sorted(Comparator.comparingInt(PieVO::getValue).reversed())
                .collect(Collectors.toList());
        if (list.isEmpty()) {
            list.add(new PieVO("暂无销售数据", 1));
        }
        return ResultJson.success(list);
    }

    @GetMapping("/getPurchaseBar")
    public ResultJson getPurchaseBar() {
        List<Purchase> purchases = purchaseService.getAll();
        Map<String, Integer> sum = new HashMap<>();
        for (Purchase p : purchases) {
            if (p.getDrugsName() == null) {
                continue;
            }
            sum.merge(p.getDrugsName(), p.getNum() == null ? 0 : p.getNum(), Integer::sum);
        }
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(sum.entrySet());
        entries.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));
        List<BarVO> list = new ArrayList<>();
        for (int i = 0; i < Math.min(12, entries.size()); i++) {
            Map.Entry<String, Integer> e = entries.get(i);
            list.add(new BarVO(e.getKey(), e.getValue()));
        }
        if (list.isEmpty()) {
            list.add(new BarVO("暂无采购数据", 0));
        }
        return ResultJson.success(list);
    }
}
