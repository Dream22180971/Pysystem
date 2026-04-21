package com.pharmacy.controller;

import com.pharmacy.bean.Drugs;
import com.pharmacy.service.DrugsService;
import com.pharmacy.util.ResultJson;
import com.pharmacy.vo.PageResult;
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
 * 药品管理控制器
 */
@RestController
@RequestMapping("/api/drugs")
public class DrugsController {

    @Autowired
    private DrugsService drugsService;

    @GetMapping("/list")
    public ResultJson list(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
            @RequestParam(value = "sortOrder", required = false, defaultValue = "asc") String sortOrder
    ) {
        PageResult<Drugs> result = drugsService.getPage(page, size, sortField, sortOrder);
        return ResultJson.success(result);
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResultJson add(@RequestBody Drugs drugs) {
        int result = drugsService.add(drugs);
        if (result > 0) {
            return ResultJson.success("添加成功");
        }
        return ResultJson.error("添加失败");
    }

    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResultJson update(@RequestBody Drugs drugs) {
        int result = drugsService.update(drugs);
        if (result > 0) {
            return ResultJson.success("更新成功");
        }
        return ResultJson.error("更新失败");
    }

    @GetMapping("/delete")
    public ResultJson delete(@RequestParam("id") Integer id) {
        int result = drugsService.delete(id);
        if (result > 0) {
            return ResultJson.success("删除成功");
        }
        return ResultJson.error("删除失败");
    }

    @GetMapping("/getById")
    public ResultJson getById(@RequestParam("id") Integer id) {
        Drugs drugs = drugsService.getById(id);
        return ResultJson.success(drugs);
    }

    @GetMapping("/getByCategory")
    public ResultJson getByCategory(@RequestParam("categoryId") Integer categoryId) {
        List<Drugs> list = drugsService.getByCategoryId(categoryId);
        return ResultJson.success(list);
    }
}
