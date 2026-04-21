package com.pharmacy.controller;

import com.pharmacy.bean.Kcxx;
import com.pharmacy.service.KcxxService;
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
 * 药品库存管理控制器
 */
@RestController
@RequestMapping("/api/kcxx")
public class KcxxController {

    @Autowired
    private KcxxService kcxxService;

    @GetMapping("/list")
    public ResultJson list(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "sortField", required = false, defaultValue = "kid") String sortField,
            @RequestParam(value = "sortOrder", required = false, defaultValue = "asc") String sortOrder
    ) {
        PageResult<Kcxx> result = kcxxService.getPage(page, size, sortField, sortOrder);
        return ResultJson.success(result);
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResultJson add(@RequestBody Kcxx kcxx) {
        int result = kcxxService.add(kcxx);
        if (result > 0) {
            return ResultJson.success("添加成功");
        }
        return ResultJson.error("添加失败");
    }

    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResultJson update(@RequestBody Kcxx kcxx) {
        int result = kcxxService.update(kcxx);
        if (result > 0) {
            return ResultJson.success("更新成功");
        }
        return ResultJson.error("更新失败");
    }

    @GetMapping("/delete")
    public ResultJson delete(@RequestParam("kid") Integer kid) {
        int result = kcxxService.delete(kid);
        if (result > 0) {
            return ResultJson.success("删除成功");
        }
        return ResultJson.error("删除失败");
    }

    @GetMapping("/getById")
    public ResultJson getById(@RequestParam("kid") Integer kid) {
        Kcxx kcxx = kcxxService.getById(kid);
        return ResultJson.success(kcxx);
    }

    @GetMapping("/getWarningList")
    public ResultJson getWarningList() {
        List<Kcxx> list = kcxxService.getWarningList();
        return ResultJson.success(list);
    }
}
