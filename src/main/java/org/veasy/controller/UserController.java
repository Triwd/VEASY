package org.veasy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.veasy.entity.Activity;
import org.veasy.entity.Response;
import org.veasy.entity.User;
import org.veasy.service.ActivityService;
import org.veasy.service.StatusService;
import org.veasy.service.UserService;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ActivityService activityService;

    @Autowired
    StatusService statusService;


    /**
     以下为角色为student的接口
     **/

    @RequestMapping ("/user/applyActivity")
    @ResponseBody
    public Response applyActivity(Integer activityId) {
        if(userService.applyActivity(activityId)){
            return new Response("true", "申请报名成功，请关注报名结果！");
        }
        return new Response("false", "申请报名失败，请联系管理人员！");
    }

    @RequestMapping("/user/applyRevisePwd")
    @ResponseBody
    public Response applyRevisePwd(String idCard){
        if(userService.applyRevisePwd(idCard) == true){
            return new Response("success", "身份匹配成功O(∩_∩)O");
        }
        else return new Response("failed", "匹配失败(T_T)");
    }

    @RequestMapping(value = "/user/revisePwd", method = RequestMethod.GET)
    @ResponseBody
    public Response revisePwd(String newPwd){
        if(userService.revisePwd(newPwd)) {
            return new Response("success", "修改成功!");
        }
        return new Response("failed", "修改失败");
    }

    /**
     以下为角色为admin的接口
     **/

    @RequestMapping("/admin/generateSeasonReport")
    @ResponseBody
    public List<Activity> generateSeasonReport(){
        List<Activity> seasonReport = userService.generateSeasonReport();
        return seasonReport;
    }

    @RequestMapping("/admin/generateYearReport")
    @ResponseBody
    public List<Activity> generateYearReport(){
        List<Activity> yearReport = userService.generateYearReport();
        return yearReport;
    }

}
