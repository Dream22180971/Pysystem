package com.pharmacy.controller;

import com.pharmacy.bean.Kcxx;
import com.pharmacy.service.KcxxService;
import com.pharmacy.util.ResultJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 药品库存管理控制器
 * 处理药品库存的增删改查和预警请求
 */
@Controller
@RequestMapping("/kcxx")
public class KcxxController {

    /**
     * 库存服务，用于业务逻辑处理
     */
    @Autowired
    private KcxxService kcxxService;

    /**
     * 获取所有库存记录列表
     * @return 库存记录列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ResultJson list() {
        List<Kcxx> list = kcxxService.getAll();
        return ResultJson.success(list);
    }

    /**
     * 添加库存记录
     * @param kcxx 库存信息对象
     * @return 添加结果
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ResultJson add(Kcxx kcxx) {
        int result = kcxxService.add(kcxx);
        if (result > 0) {
            return ResultJson.success("添加成功");
        } else {
            return ResultJson.error("添加失败");
        }
    }

    /**
     * 更新库存记录
     * @param kcxx 库存信息对象
     * @return 更新结果
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResultJson update(Kcxx kcxx) {
        int result = kcxxService.update(kcxx);
        if (result > 0) {
            return ResultJson.success("更新成功");
        } else {
            return ResultJson.error("更新失败");
        }
    }

    /**
     * 删除库存记录
     * @param kid 库存ID
     * @return 删除结果
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public ResultJson delete(Integer kid) {
        int result = kcxxService.delete(kid);
        if (result > 0) {
            return ResultJson.success("删除成功");
        } else {
            return ResultJson.error("删除失败");
        }
    }

    /**
     * 根据ID获取库存记录
     * @param kid 库存ID
     * @return 库存记录
     */
    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    @ResponseBody
    public ResultJson getById(Integer kid) {
        Kcxx kcxx = kcxxService.getById(kid);
        return ResultJson.success(kcxx);
    }

    /**
     * 获取库存预警列表（库存不足的药品）
     * @return 库存预警列表
     */
    @RequestMapping(value = "/getWarningList", method = RequestMethod.GET)
    @ResponseBody
    public ResultJson getWarningList() {
        List<Kcxx> list = kcxxService.getWarningList();
        return ResultJson.success(list);
    }
}