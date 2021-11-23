package org.veasy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.veasy.entity.Response;
import org.veasy.service.UserService;

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

    @RequestMapping("/login_page")
    public Response loginPage() {
        return new Response("error", "尚未登录，请登录!");
    }

}
