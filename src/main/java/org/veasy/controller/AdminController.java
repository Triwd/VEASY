package org.veasy.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.veasy.entity.Activity;
import org.veasy.entity.Feedback;
import org.veasy.entity.Response;
import org.veasy.entity.User;
import org.veasy.service.ActivityService;
import org.veasy.service.StatusService;
import org.veasy.service.UserService;
import org.veasy.utils.RedisUtils;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    ActivityService activityService;

    @Autowired
    UserService userService;

    @Autowired
    StatusService statusService;

    @Autowired
    RedisUtils redisUtils;

    //创建活动
    @RequestMapping(value = "/createActivity")
    @ResponseBody
    public Response createActivity(@RequestBody Activity activity) {
        if (activityService.createActivityByAdmin(activity)) {
            return new Response("success", "新建活动成功！");
        } else return new Response("failed", "新建活动失败，请重试或者联系运维人员！");
    }

    //取消活动
    @RequestMapping(value = "/cancelActivity")
    @ResponseBody
    public Response cancelActivity(Integer activityId) {
        if (activityService.cancelActivity(activityId)) {
            return new Response("success!", "活动已取消");
        } else return new Response("failed!", "活动不存在，取消活动失败，请刷新页面");
    }

    //开启报名
    @RequestMapping(value = "/openSign")
    @ResponseBody
    public Response openSign(Integer activityId) {
        if (activityService.openSign(activityId)) {
            return new Response("success", "已开启报名");
        } else return new Response("failed", "活动已经开启，请不要重复开启报名");
    }

    //关闭报名
    @RequestMapping(value = "/closeSign")
    @ResponseBody
    public Response closeSign(@Param(value = "id") Integer activityId) {
        if (activityService.closeSign(activityId)) {
            return new Response("success", "已关闭报名");
        } else return new Response("failed", "活动已经关闭报名或者被取消，如有问题联系运维人员");
    }

    //活动结束总结
    @RequestMapping("/activitySummary")
    @ResponseBody
    public Response activitySummary(Integer activityId, String summary) {
        if (userService.activitySummary(activityId, summary)) {
            return new Response("success", "已完成活动结束总结，本次活动已经结束，辛苦啦");
        } else return new Response("failed", "活动还没有结束，请活动结束后再进行总结噢");
    }

    //根据id查找活动
    @RequestMapping(value = "/getActivityById")
    @ResponseBody
    public Activity getActivityById(Integer activityId) {
        return activityService.getActivityById(activityId);
    }

    //根据时间区间加载活动
    @RequestMapping("/loadActivityByTime")
    @ResponseBody
    public List<Activity> loadActivityByTime(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        List<Activity> activityList = activityService.loadActivityByTime(startTime, endTime);
        statusService.addValueOfStatus(activityList);
        return activityList;
    }

    //生成季度报告
    @RequestMapping("/generateSeasonReport")
    @ResponseBody
    public String generateSeasonReport() {
        Integer numsOfActivity = userService.generateSeasonReport().size();
        return numsOfActivity.toString();
    }

    //生成年度报告
    @RequestMapping("/generateYearReport")
    @ResponseBody
    public String generateYearReport() {
        Integer numsOfActivity = userService.generateYearReport().size();
        return numsOfActivity.toString();
    }

    //开启普通选拔模式
    @RequestMapping("/normalSelectModel")
    @ResponseBody
    public Response normalSelectModel() {
        if (userService.normalSelectModel()) {
            return new Response("success", "开启普通选拔模式成功！");
        }
        return new Response("failed", "开启普通选拔模式失败，请重试或联系管理员！");
    }

    //开启多目标选拔模式
    @RequestMapping("/MOPSelectModel")
    @ResponseBody
    public Response MOPSelectModel() {
        if (userService.MOPSelectMode()) {
            return new Response("success", "开启多目标选拔模式成功！");
        }
        return new Response("failed", "开启多目标选拔模式失败，请重试或联系管理员！");
    }

    @RequestMapping("/checkFeedback")
    @ResponseBody
    public List<Feedback> checkFeedback() {
        return userService.checkFeedback();
    }

    @RequestMapping("/checkSubmitterMsg")
    @ResponseBody
    public User checkSubmitterMsg(Integer studentId) {
        return userService.loadUserById(studentId);
    }
}
