package com.pharmacy.controller;

import com.pharmacy.bean.Sale;
import com.pharmacy.service.SaleService;
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
 * 药品销售管理控制器
 */
@RestController
@RequestMapping("/api/sale")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @GetMapping("/list")
    public ResultJson list(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "sortField", required = false, defaultValue = "saledate") String sortField,
            @RequestParam(value = "sortOrder", required = false, defaultValue = "asc") String sortOrder
    ) {
        PageResult<Sale> result = saleService.getPage(page, size, sortField, sortOrder);
        return ResultJson.success(result);
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResultJson add(@RequestBody Sale sale) {
        int result = saleService.add(sale);
        if (result > 0) {
            return ResultJson.success("添加成功");
        }
        return ResultJson.error("添加失败");
    }

    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResultJson update(@RequestBody Sale sale) {
        int result = saleService.update(sale);
        if (result > 0) {
            return ResultJson.success("更新成功");
        }
        return ResultJson.error("更新失败");
    }

    @GetMapping("/delete")
    public ResultJson delete(@RequestParam("saleId") Integer saleId) {
        int result = saleService.delete(saleId);
        if (result > 0) {
            return ResultJson.success("删除成功");
        }
        return ResultJson.error("删除失败");
    }

    @GetMapping("/getById")
    public ResultJson getById(@RequestParam("saleId") Integer saleId) {
        Sale sale = saleService.getById(saleId);
        return ResultJson.success(sale);
    }
}
