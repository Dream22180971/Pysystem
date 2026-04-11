package com.pharmacy.controller;

import com.pharmacy.bean.Purchase;
import com.pharmacy.service.PurchaseService;
import com.pharmacy.util.ResultJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 药品采购管理控制器
 * 处理药品采购的增删改查请求
 */
@Controller
@RequestMapping("/purchase")
public class PurchaseController {

    /**
     * 采购服务，用于业务逻辑处理
     */
    @Autowired
    private PurchaseService purchaseService;

    /**
     * 获取所有采购记录列表
     * @return 采购记录列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ResultJson list() {
        List<Purchase> list = purchaseService.getAll();
        return ResultJson.success(list);
    }

    /**
     * 添加采购记录
     * @param purchase 采购信息对象
     * @return 添加结果
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ResultJson add(Purchase purchase) {
        int result = purchaseService.add(purchase);
        if (result > 0) {
            return ResultJson.success("添加成功");
        } else {
            return ResultJson.error("添加失败");
        }
    }

    /**
     * 更新采购记录
     * @param purchase 采购信息对象
     * @return 更新结果
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResultJson update(Purchase purchase) {
        int result = purchaseService.update(purchase);
        if (result > 0) {
            return ResultJson.success("更新成功");
        } else {
            return ResultJson.error("更新失败");
        }
    }

    /**
     * 删除采购记录
     * @param pid 采购ID
     * @return 删除结果
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public ResultJson delete(Integer pid) {
        int result = purchaseService.delete(pid);
        if (result > 0) {
            return ResultJson.success("删除成功");
        } else {
            return ResultJson.error("删除失败");
        }
    }

    /**
     * 根据ID获取采购记录
     * @param pid 采购ID
     * @return 采购记录
     */
    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    @ResponseBody
    public ResultJson getById(Integer pid) {
        Purchase purchase = purchaseService.getById(pid);
        return ResultJson.success(purchase);
    }
}