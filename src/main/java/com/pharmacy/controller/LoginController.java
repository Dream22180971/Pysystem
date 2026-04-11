package com.pharmacy.controller;

import com.pharmacy.service.UserinfoService;
import com.pharmacy.util.ResultJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 登录控制器
 * 处理用户登录和退出登录的请求
 */
@Controller
public class LoginController {

    /**
     * 用户服务，用于登录验证
     */
    @Autowired
    private UserinfoService userinfoService;

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @param request HttpServletRequest对象，用于获取会话
     * @return 登录结果
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(String username, String password, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 调用用户服务进行登录验证
        boolean result = userinfoService.login(username, password);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        if (result) {
            // 登录成功，将用户名存入会话
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            writer.write("{\"code\": 200, \"message\": \"登录成功\", \"data\": null}");
        } else {
            // 登录失败，返回错误信息
            writer.write("{\"code\": 500, \"message\": \"用户名或密码错误\", \"data\": null}");
        }
        writer.flush();
        writer.close();
    }

    /**
     * 用户退出登录
     * @param request HttpServletRequest对象，用于获取会话
     * @return 重定向到登录页面
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request) {
        // 使会话无效
        HttpSession session = request.getSession();
        session.invalidate();
        // 重定向到登录页面
        return "redirect:/index.jsp";
    }

    /**
     * 测试方法，用于验证JSON响应
     * @return 测试响应
     */
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void test(HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write("{\"code\": 200, \"message\": \"测试成功\", \"data\": \"测试数据\"}");
        writer.flush();
        writer.close();
    }
}