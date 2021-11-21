package org.veasy.controller;

import org.veasy.entity.Response;
import org.veasy.service.ActivityService;
import org.veasy.service.UserService;
import org.veasy.utils.RedisUtils;
import org.veasy.entity.Activity;
import org.veasy.entity.Response;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


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
    RedisUtils redisUtils;

    //创建活动
    @RequestMapping (value = "/createActivity",method = RequestMethod.POST)
    @ResponseBody
    public String createActivity(@RequestBody Activity activity){
        if(activityService.createActivityByAdmin(activity))
        {
            System.out.println("创建"+activity.getName()+"活动成功");
            return "添加成功";
        }
        else return "添加失败，请重试或联系运维人员";
    }

    //取消活动
    @RequestMapping (value = "/cancelActivity",method = RequestMethod.POST)
    @ResponseBody
    public Response cancelActivity(@Param(value = "id") Integer activityId){
        if(activityService.cancelActivity(activityId))
        {
            return new Response("success!", "活动已取消");
        }
        else return new Response("failed!", "活动不存在，取消活动失败，请刷新页面");
    }

    //开启报名
    @RequestMapping (value = "/openSign",method = RequestMethod.POST)
    @ResponseBody
    public Response openSign(@Param(value = "id")Integer activityId){
        if(activityService.openSign(activityId))
        {
            return new Response("success", "已开启报名");
        }
        else return new Response("failed", "开启报名失败，请重试或联系运维人员");
    }

    //开启报名
    @RequestMapping (value = "/closeSign",method = RequestMethod.POST)
    @ResponseBody
    public Response closeSign(@Param(value = "id")Integer activityId){
        if(activityService.closeSign(activityId))
        {
            return new Response("success", "已关闭报名");
        }
        else return new Response("failed", "关闭报名失败，请重试或联系运维人员");
    }

    //根据id查找活动
    @RequestMapping (value = "/getActivityById",method = RequestMethod.POST)
    @ResponseBody
    public Activity getActivityById(@Param(value = "id")Integer activityId){
        return activityService.getActivityById(activityId);
    }

    @RequestMapping("/loadActivityByTime")
    @ResponseBody
    public List<Activity> loadActivityByTime(Date startTime, Date endTime){
        return activityService.loadActivityByTime(startTime, endTime);
    }

    //生成季度报告
    @RequestMapping("/generateSeasonReport")
    @ResponseBody
    public List<Activity> generateSeasonReport(){
        List<Activity> seasonReport = userService.generateSeasonReport();
        return seasonReport;
    }

    //生成年度报告
    @RequestMapping("/generateYearReport")
    @ResponseBody
    public List<Activity> generateYearReport(){
        List<Activity> yearReport = userService.generateYearReport();
        return yearReport;
    }
}
