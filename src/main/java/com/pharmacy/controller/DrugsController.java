package com.pharmacy.controller;

import com.pharmacy.bean.Drugs;
import com.pharmacy.service.DrugsService;
import com.pharmacy.util.ResultJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 药品管理控制器
 * 处理药品的增删改查请求
 */
@Controller
@RequestMapping("/drugs")
public class DrugsController {

    /**
     * 药品服务，用于业务逻辑处理
     */
    @Autowired
    private DrugsService drugsService;

    /**
     * 获取所有药品列表
     * @return 药品列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ResultJson list() {
        List<Drugs> list = drugsService.getAll();
        return ResultJson.success(list);
    }

    /**
     * 添加药品
     * @param drugs 药品信息对象
     * @return 添加结果
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ResultJson add(Drugs drugs) {
        int result = drugsService.add(drugs);
        if (result > 0) {
            return ResultJson.success("添加成功");
        } else {
            return ResultJson.error("添加失败");
        }
    }

    /**
     * 更新药品信息
     * @param drugs 药品信息对象
     * @return 更新结果
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResultJson update(Drugs drugs) {
        int result = drugsService.update(drugs);
        if (result > 0) {
            return ResultJson.success("更新成功");
        } else {
            return ResultJson.error("更新失败");
        }
    }

    /**
     * 删除药品
     * @param id 药品ID
     * @return 删除结果
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public ResultJson delete(Integer id) {
        int result = drugsService.delete(id);
        if (result > 0) {
            return ResultJson.success("删除成功");
        } else {
            return ResultJson.error("删除失败");
        }
    }

    /**
     * 根据ID获取药品信息
     * @param id 药品ID
     * @return 药品信息
     */
    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    @ResponseBody
    public ResultJson getById(Integer id) {
        Drugs drugs = drugsService.getById(id);
        return ResultJson.success(drugs);
    }

    /**
     * 根据分类ID获取药品列表
     * @param categoryId 分类ID
     * @return 药品列表
     */
    @RequestMapping(value = "/getByCategory", method = RequestMethod.GET)
    @ResponseBody
    public ResultJson getByCategory(Integer categoryId) {
        List<Drugs> list = drugsService.getByCategoryId(categoryId);
        return ResultJson.success(list);
    }
}