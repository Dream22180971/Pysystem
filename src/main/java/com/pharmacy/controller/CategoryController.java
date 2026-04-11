package com.pharmacy.controller;

import com.pharmacy.bean.Category;
import com.pharmacy.service.CategoryService;
import com.pharmacy.util.ResultJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 药品分类管理控制器
 * 处理药品分类的增删改查请求
 */
@Controller
@RequestMapping("/category")
public class CategoryController {

    /**
     * 分类服务，用于业务逻辑处理
     */
    @Autowired
    private CategoryService categoryService;

    /**
     * 获取所有分类列表
     * @return 分类列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ResultJson list() {
        List<Category> list = categoryService.getAll();
        return ResultJson.success(list);
    }

    /**
     * 添加分类
     * @param category 分类信息对象
     * @return 添加结果
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ResultJson add(Category category) {
        int result = categoryService.add(category);
        if (result > 0) {
            return ResultJson.success("添加成功");
        } else {
            return ResultJson.error("添加失败");
        }
    }

    /**
     * 更新分类信息
     * @param category 分类信息对象
     * @return 更新结果
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResultJson update(Category category) {
        int result = categoryService.update(category);
        if (result > 0) {
            return ResultJson.success("更新成功");
        } else {
            return ResultJson.error("更新失败");
        }
    }

    /**
     * 删除分类
     * @param categoryId 分类ID
     * @return 删除结果
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public ResultJson delete(Integer categoryId) {
        int result = categoryService.delete(categoryId);
        if (result > 0) {
            return ResultJson.success("删除成功");
        } else {
            return ResultJson.error("删除失败");
        }
    }

    /**
     * 根据ID获取分类信息
     * @param categoryId 分类ID
     * @return 分类信息
     */
    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    @ResponseBody
    public ResultJson getById(Integer categoryId) {
        Category category = categoryService.getById(categoryId);
        return ResultJson.success(category);
    }
}