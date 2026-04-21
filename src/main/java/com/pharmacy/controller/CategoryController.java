package com.pharmacy.controller;

import com.pharmacy.bean.Category;
import com.pharmacy.service.CategoryService;
import com.pharmacy.util.ResultJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 药品分类管理控制器
 */
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public ResultJson list() {
        List<Category> list = categoryService.getAll();
        return ResultJson.success(list);
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResultJson add(@RequestBody Category category) {
        int result = categoryService.add(category);
        if (result > 0) {
            return ResultJson.success("添加成功");
        }
        return ResultJson.error("添加失败");
    }

    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResultJson update(@RequestBody Category category) {
        int result = categoryService.update(category);
        if (result > 0) {
            return ResultJson.success("更新成功");
        }
        return ResultJson.error("更新失败");
    }

    @GetMapping("/delete")
    public ResultJson delete(@RequestParam("categoryId") Integer categoryId) {
        int result = categoryService.delete(categoryId);
        if (result > 0) {
            return ResultJson.success("删除成功");
        }
        return ResultJson.error("删除失败");
    }

    @GetMapping("/getById")
    public ResultJson getById(@RequestParam("categoryId") Integer categoryId) {
        Category category = categoryService.getById(categoryId);
        return ResultJson.success(category);
    }
}
