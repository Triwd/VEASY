package org.veasy.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.veasy.entity.Response;
import org.springframework.stereotype.Controller;
import org.veasy.service.ActivityService;
import org.veasy.service.StatusService;
import org.veasy.service.UserService;
import org.veasy.utils.RedisUtils;

@Controller
@RequestMapping("/student")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ActivityService activityService;

    @Autowired
    StatusService statusService;

    @Autowired
    RedisUtils redisUtils;


    /**
     以下为角色为student的接口
     **/
    //申请报名（无redis版本）
    @RequestMapping ("/applyActivity")
    @ResponseBody
    public Response applyActivity(Integer activityId) {
        if(userService.applyActivity(activityId)){
            return new Response("true", "申请报名成功，请关注报名结果！");
        }
        return new Response("false", "申请报名失败，请联系管理人员！");
    }

    //申请修改密码
    @RequestMapping("/applyRevisePwd")
    @ResponseBody
    public Response applyRevisePwd(String idCard){
        if(userService.applyRevisePwd(idCard) == true){
            return new Response("success", "身份匹配成功O(∩_∩)O");
        }
        else return new Response("failed", "匹配失败(T_T)");
    }

    //修改密码
    @RequestMapping(value = "/revisePwd", method = RequestMethod.POST)
    @ResponseBody
    public Response revisePwd(String newPwd){
        if(userService.revisePwd(newPwd)) {
            return new Response("success", "修改成功!");
        }
        return new Response("failed", "修改失败");
    }

    //报名
    @RequestMapping (value = "/signUp",method = RequestMethod.POST)
    @ResponseBody
    public Response signUp(@Param(value = "activityId")Integer activityId, @Param(value = "studentId")Integer studentId){
        if(redisUtils.addStudentCache(activityId,studentId)){
            return new Response("success", "报名成功，请在已参与的活动列表中获取活动联系信息。");
        }else {
            return new Response("failed","报名失败，活动没有名额了噢，请关注补选或者其他活动~");
        }
    }

    //取消报名
    @RequestMapping (value = "/cancelSign",method = RequestMethod.POST)
    @ResponseBody
    public Response cancelSign(@Param(value = "id")Integer activityId){
        if(activityService.cancelActivity(activityId))
        {
            return new Response("success", "取消报名成功，下次报名前要考虑清楚噢！");
        }
        else return new Response("failed", "取消报名失败，报名操作现已冻结，请等待补选开启或者联系管理员！");
    }
}
