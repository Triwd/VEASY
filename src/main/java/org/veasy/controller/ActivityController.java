package org.veasy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.veasy.entity.Activity;
import org.veasy.service.ActivityService;
import org.veasy.service.StatusService;

import java.util.List;

@RestController
public class ActivityController {
    @Autowired
    ActivityService activityService;

    @Autowired
    StatusService statusService;

    //加载所有活动
    @RequestMapping("/admin/loadAllActivity")
    @ResponseBody
    public List<Activity> loadAllActivity() {
        //加载数据，存入列表
        List<Activity> activityList = activityService.loadAllActivity();
        //为Activity实体类的状态字段赋值
        statusService.addValueOfStatus(activityList);
        return activityList;
    }

    //加载已经结束的活动
    @RequestMapping(value = "/admin/loadEndActivity")
    @ResponseBody
    public List<Activity> loadEndActivity() {
        List<Activity> activityList = activityService.loadEndActivity();
        statusService.addValueOfStatus(activityList);
        return activityList;
    }

    //加载报名中的活动
    @RequestMapping("/student/loadUnderApplyActivity")
    @ResponseBody
    public List<Activity> loadUnderApplyActivity() {
        List<Activity> activityList = activityService.loadUnderApplyActivity();
        statusService.addValueOfStatus(activityList);
        return activityList;
    }

    //加载报名失败的活动
    @RequestMapping("/student/loadFailedActivity")
    @ResponseBody
    public List<Activity> loadFailedActivity() {
        List<Activity> activityList = activityService.loadFailedActivity();
        statusService.addValueOfStatus(activityList);
        return activityList;
    }

    //加载我的活动，包括报名失败的活动和报名成功的活动
    @RequestMapping("/student/loadMyActivity")
    @ResponseBody
    public List<Activity> loadMyActivity() {
        List<Activity> activityList = activityService.loadMyActivity();
        statusService.addValueOfStatus(activityList);
        return activityList;
    }
}
