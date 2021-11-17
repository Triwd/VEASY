package org.veasy.controller;

import org.veasy.entity.Response;
import org.veasy.entity.User;
import org.veasy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sang on 2017/12/17.
 */
@RestController
public class LoginController {

    @Autowired
    UserService userService;

    @RequestMapping("/login_error")
    public Response loginError() {
        return new Response("error", "登录失败!");
    }

    @RequestMapping("/login_success")
    public Response loginSuccess() {
        return new Response("success", "登录成功!");
    }

    /**
     * 如果自动跳转到这个页面，说明用户未登录，返回相应的提示即可
     * <p>
     * 如果要支持表单登录，可以在这个方法中判断请求的类型，进而决定返回JSON还是HTML页面
     *
     * @return
     */
    @RequestMapping("/login_page")
    public Response loginPage() {
        return new Response("error", "尚未登录，请登录!");
    }

}
