package com.pharmacy.controller;

import com.pharmacy.bean.Sale;
import com.pharmacy.service.SaleService;
import com.pharmacy.util.ResultJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 药品销售管理控制器
 * 处理药品销售的增删改查请求
 */
@Controller
@RequestMapping("/sale")
public class SaleController {

    /**
     * 销售服务，用于业务逻辑处理
     */
    @Autowired
    private SaleService saleService;

    /**
     * 获取所有销售记录列表
     * @return 销售记录列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ResultJson list() {
        List<Sale> list = saleService.getAll();
        return ResultJson.success(list);
    }

    /**
     * 添加销售记录
     * @param sale 销售信息对象
     * @return 添加结果
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ResultJson add(Sale sale) {
        int result = saleService.add(sale);
        if (result > 0) {
            return ResultJson.success("添加成功");
        } else {
            return ResultJson.error("添加失败");
        }
    }

    /**
     * 更新销售记录
     * @param sale 销售信息对象
     * @return 更新结果
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResultJson update(Sale sale) {
        int result = saleService.update(sale);
        if (result > 0) {
            return ResultJson.success("更新成功");
        } else {
            return ResultJson.error("更新失败");
        }
    }

    /**
     * 删除销售记录
     * @param saleId 销售ID
     * @return 删除结果
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public ResultJson delete(Integer saleId) {
        int result = saleService.delete(saleId);
        if (result > 0) {
            return ResultJson.success("删除成功");
        } else {
            return ResultJson.error("删除失败");
        }
    }

    /**
     * 根据ID获取销售记录
     * @param saleId 销售ID
     * @return 销售记录
     */
    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    @ResponseBody
    public ResultJson getById(Integer saleId) {
        Sale sale = saleService.getById(saleId);
        return ResultJson.success(sale);
    }
}