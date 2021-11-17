package org.veasy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.veasy.entity.Response;
import org.veasy.entity.User;
import org.veasy.service.UserService;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping ("/loadUserById")
    @ResponseBody
    public User loadUserById(Integer id) {
        User user = userService.loadUserById(id);
        return user;
    }

    @RequestMapping("/applyRevisePwd")
    @ResponseBody
    public Response applyRevisePwd(String idCard){
        if(userService.applyRevisePwd(idCard) == true){
            return new Response("success", "身份匹配成功O(∩_∩)O");
        }
        else return new Response("failed", "匹配失败(T_T)");
    }

    @RequestMapping(value = "/revisePwd", method = RequestMethod.PUT)
    @ResponseBody
    public Response revisePwd(String newPwd){
        if(userService.revisePwd(newPwd)) {
            return new Response("success", "修改成功!");
        }
        return new Response("failed", "修改失败");
    }


}
