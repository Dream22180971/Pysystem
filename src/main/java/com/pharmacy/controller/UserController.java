package com.pharmacy.controller;

import com.pharmacy.bean.Userinfo;
import com.pharmacy.service.UserinfoService;
import com.pharmacy.util.ResultJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 用户管理控制器
 * 处理用户的增删改查请求
 */
@Controller
@RequestMapping("/user")
public class UserController {

    /**
     * 用户服务，用于业务逻辑处理
     */
    @Autowired
    private UserinfoService userinfoService;

    /**
     * 获取所有用户列表
     * @return 用户列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ResultJson list() {
        List<Userinfo> list = userinfoService.getAll();
        return ResultJson.success(list);
    }

    /**
     * 添加用户
     * @param userinfo 用户信息对象
     * @return 添加结果
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ResultJson add(Userinfo userinfo) {
        int result = userinfoService.add(userinfo);
        if (result > 0) {
            return ResultJson.success("添加成功");
        } else {
            return ResultJson.error("添加失败");
        }
    }

    /**
     * 更新用户信息
     * @param userinfo 用户信息对象
     * @return 更新结果
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResultJson update(Userinfo userinfo) {
        int result = userinfoService.update(userinfo);
        if (result > 0) {
            return ResultJson.success("更新成功");
        } else {
            return ResultJson.error("更新失败");
        }
    }

    /**
     * 删除用户
     * @param id 用户ID
     * @return 删除结果
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public ResultJson delete(Integer id) {
        int result = userinfoService.delete(id);
        if (result > 0) {
            return ResultJson.success("删除成功");
        } else {
            return ResultJson.error("删除失败");
        }
    }

    /**
     * 根据ID获取用户信息
     * @param id 用户ID
     * @return 用户信息
     */
    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    @ResponseBody
    public ResultJson getById(Integer id) {
        Userinfo userinfo = userinfoService.getById(id);
        return ResultJson.success(userinfo);
    }
}