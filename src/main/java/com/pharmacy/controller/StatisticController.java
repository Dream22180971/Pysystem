package com.pharmacy.controller;

import com.pharmacy.service.SaleService;
import com.pharmacy.service.PurchaseService;
import com.pharmacy.vo.BarVO;
import com.pharmacy.vo.PieVO;
import com.pharmacy.util.ResultJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 统计数据控制器
 * 提供销售和采购的统计数据，用于前端图表展示
 */
@Controller
@RequestMapping("/statistic")
public class StatisticController {

    /**
     * 销售服务，用于获取销售数据
     */
    @Autowired
    private SaleService saleService;

    /**
     * 采购服务，用于获取采购数据
     */
    @Autowired
    private PurchaseService purchaseService;

    /**
     * 获取销售数据饼图
     * @return 销售数据列表
     */
    @RequestMapping(value = "/getSalePie", method = RequestMethod.GET)
    @ResponseBody
    public ResultJson getSalePie() {
        // 模拟销售数据
        List<PieVO> list = new ArrayList<>();
        list.add(new PieVO("感冒灵颗粒", 342));
        list.add(new PieVO("阿莫西林", 210));
        list.add(new PieVO("布洛芬", 189));
        list.add(new PieVO("健胃消食片", 276));
        list.add(new PieVO("其他", 410));
        return ResultJson.success(list);
    }

    /**
     * 获取采购数据柱状图
     * @return 采购数据列表
     */
    @RequestMapping(value = "/getPurchaseBar", method = RequestMethod.GET)
    @ResponseBody
    public ResultJson getPurchaseBar() {
        // 模拟采购数据
        List<BarVO> list = new ArrayList<>();
        list.add(new BarVO("板蓝根", 320));
        list.add(new BarVO("感冒灵", 480));
        list.add(new BarVO("布洛芬", 290));
        list.add(new BarVO("头孢克肟", 160));
        list.add(new BarVO("维生素C", 410));
        list.add(new BarVO("退热贴", 235));
        return ResultJson.success(list);
    }
}