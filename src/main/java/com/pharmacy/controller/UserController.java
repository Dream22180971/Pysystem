package com.pharmacy.controller;

import com.pharmacy.bean.Userinfo;
import com.pharmacy.service.UserinfoService;
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
 * 用户管理控制器
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserinfoService userinfoService;

    @GetMapping("/list")
    public ResultJson list() {
        List<Userinfo> list = userinfoService.getAll();
        return ResultJson.success(list);
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResultJson add(@RequestBody Userinfo userinfo) {
        int result = userinfoService.add(userinfo);
        if (result > 0) {
            return ResultJson.success("添加成功");
        }
        return ResultJson.error("添加失败");
    }

    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResultJson update(@RequestBody Userinfo userinfo) {
        int result = userinfoService.update(userinfo);
        if (result > 0) {
            return ResultJson.success("更新成功");
        }
        return ResultJson.error("更新失败");
    }

    @GetMapping("/delete")
    public ResultJson delete(@RequestParam("id") Integer id) {
        int result = userinfoService.delete(id);
        if (result > 0) {
            return ResultJson.success("删除成功");
        }
        return ResultJson.error("删除失败");
    }

    @GetMapping("/getById")
    public ResultJson getById(@RequestParam("id") Integer id) {
        Userinfo userinfo = userinfoService.getById(id);
        if (userinfo != null) {
            userinfo.setPassword(null);
        }
        return ResultJson.success(userinfo);
    }
}
