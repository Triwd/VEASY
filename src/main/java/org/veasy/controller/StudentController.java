package org.veasy.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.veasy.entity.Response;
import org.veasy.service.ActivityService;
import org.veasy.service.StatusService;
import org.veasy.service.UserService;
import org.veasy.utils.RedisUtils;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    UserService userService;

    @Autowired
    ActivityService activityService;

    @Autowired
    StatusService statusService;

    @Autowired
    RedisUtils redisUtils;

//    //申请报名v2.0
//    @RequestMapping ("/applyActivity")
//    @ResponseBody
//    public Response applyActivity(Integer activityId) {
//        if(userService.applyActivity(activityId)){
//            return new Response("true", "申请报名成功，请关注报名结果！");
//        }
//        return new Response("false", "申请报名失败，请联系管理人员！");
//    }
//
//    //取消报名v2.0
//    @RequestMapping("/cancelApply")
//    @ResponseBody
//    public Response cancelApply(Integer activityId){
//        if(userService.cancelApply(activityId)){
//            return new Response("success", "取消申请成功，下次报名前要考虑清楚噢！");
//        }
//        else return new Response("failed", "取消申请失败，请重试或联系管理人员。");
//    }

    //申请修改密码
    @RequestMapping("/applyRevisePwd")
    @ResponseBody
    public Response applyRevisePwd(String idCard) {
        if (userService.applyRevisePwd(idCard)) {
            return new Response("success", "身份匹配成功O(∩_∩)O");
        } else return new Response("failed", "匹配失败(T_T)");
    }

    //修改密码
    @RequestMapping(value = "/revisePwd", method = RequestMethod.POST)
    @ResponseBody
    public Response revisePwd(String newPwd){
        if (userService.revisePwd(newPwd)) {
            return new Response("success", "修改成功!");
        } else return new Response("failed", "修改失败");
    }

    //报名
    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    @ResponseBody
    public Response signUp(@Param(value = "activityId") Integer activityId) {
        if (userService.signUp(activityId)) {
            return new Response("success", "申请报名成功，待报名结束后关注已参与的活动噢~");
        } else return new Response("failed", "申请报名失败，你已经申请报名成功了或者活动没有名额了噢~");
    }

    //取消报名
    @RequestMapping(value = "/cancelSign", method = RequestMethod.POST)
    @ResponseBody
    public Response cancelSign(@Param(value = "id") Integer activityId) {
        if (userService.cancelSign(activityId)) {
            return new Response("success", "取消报名成功，下次报名前要考虑清楚噢！");
        } else return new Response("failed", "取消报名失败，你还没有申请报名噢！");
    }

    @RequestMapping("/getRestNumById")
    @ResponseBody
    public Integer getRestNumById(Integer activityId) {
        return activityService.getRestNumById(activityId);
    }
}
