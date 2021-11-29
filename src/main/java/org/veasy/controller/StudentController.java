package org.veasy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.veasy.entity.Response;
import org.veasy.entity.User;
import org.veasy.service.ActivityService;
import org.veasy.service.StatusService;
import org.veasy.service.UserService;
import org.veasy.utils.RedisUtils;
import org.veasy.utils.Util;

import java.util.LinkedList;
import java.util.List;

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

    //申请修改密码
    @RequestMapping("/applyRevisePwd")
    @ResponseBody
    public Response applyRevisePwd(String idCard) {
        if (userService.applyRevisePwd(idCard)) {
            return new Response("success", "身份匹配成功O(∩_∩)O");
        } else return new Response("failed", "匹配失败(T_T)");
    }

    //修改密码
    @RequestMapping(value = "/revisePwd")
    @ResponseBody
    public Response revisePwd(String newPwd){
        if (userService.revisePwd(newPwd)) {
            return new Response("success", "修改成功!");
        } else return new Response("failed", "修改失败");
    }

    //报名
    @RequestMapping("/signUp")
    @ResponseBody
    public Response signUp(Integer activityId) {
        if (userService.signUp(activityId)) {
            return new Response("success", "申请报名成功，待报名结束后关注已参与的活动噢~");
        } else return new Response("failed", "申请报名失败，你已经申请报名成功了或者活动没有名额了噢~");
    }

    //取消报名
    @RequestMapping("/cancelSign")
    @ResponseBody
    public Response cancelSign(Integer activityId) {
        if (userService.cancelSign(activityId)) {
            return new Response("success", "取消报名成功，下次报名前要考虑清楚噢！");
        } else return new Response("failed", "取消报名失败，你还没有申请报名噢！");
    }

    //根据活动Id获取剩余名额
    @RequestMapping("/getRestNumById")
    @ResponseBody
    public Integer getRestNumById(Integer activityId) {
        return activityService.getRestNumById(activityId);
    }

    //提交反馈
    @RequestMapping("/submitFeedback")
    @ResponseBody
    public Response submitFeedback(String content) {
        if (userService.submitFeedback(content)) {
            return new Response("success", "提交成功，感谢您的反馈！");
        } else return new Response("failed", "提交失败，请重试或者联系管理人员！");
    }

    //个人活动总结
    @RequestMapping("/activitySummary")
    @ResponseBody
    public List<String> activitySummary() {
        User currentUser = Util.getCurrentUser();
        List<String> activitySummary = new LinkedList<String>() {{
            add(currentUser.getName());
            add(userService.getActivityTimesById(currentUser.getId()).toString());
            add(currentUser.getActivityHours().toString());
        }};
        return activitySummary;
    }
}
