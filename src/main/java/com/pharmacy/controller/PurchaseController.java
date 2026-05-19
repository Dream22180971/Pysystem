package com.pharmacy.controller;

import com.pharmacy.bean.Purchase;
import com.pharmacy.service.PurchaseService;
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
 * 药品采购管理控制器
 */
@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @GetMapping("/list")
    public ResultJson list(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "sortField", required = false, defaultValue = "indate") String sortField,
            @RequestParam(value = "sortOrder", required = false, defaultValue = "asc") String sortOrder
    ) {
        PageResult<Purchase> result = purchaseService.getPage(page, size, sortField, sortOrder);
        return ResultJson.success(result);
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResultJson add(@RequestBody Purchase purchase) {
        int result = purchaseService.add(purchase);
        if (result > 0) {
            return ResultJson.success("添加成功");
        }
        return ResultJson.error("添加失败");
    }

    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResultJson update(@RequestBody Purchase purchase) {
        int result = purchaseService.update(purchase);
        if (result > 0) {
            return ResultJson.success("更新成功");
        }
        return ResultJson.error("更新失败");
    }

    @GetMapping("/delete")
    public ResultJson delete(@RequestParam("pid") Integer pid) {
        int result = purchaseService.delete(pid);
        if (result > 0) {
            return ResultJson.success("删除成功");
        }
        return ResultJson.error("删除失败");
    }

    @GetMapping("/getById")
    public ResultJson getById(@RequestParam("pid") Integer pid) {
        Purchase purchase = purchaseService.getById(pid);
        return ResultJson.success(purchase);
    }
}
