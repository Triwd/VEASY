package org.veasy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.veasy.entity.Activity;
import org.veasy.service.ActivityService;
import org.veasy.service.StatusService;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@RestController
public class ActivityController {
    @Autowired
    ActivityService activityService;

    @Autowired
    StatusService statusService;

    @RequestMapping("/loadAllActivity")
    @ResponseBody
    public List<Activity> loadAllActivity(){
        //加载数据，存入列表
        List<Activity> activityList = activityService.loadAllActivity();
        //为Activity实体类的状态字段赋值
        statusService.addValueOfStatus(activityList);
        return activityList;
    }

    @RequestMapping("/loadUnderApplyActivity")
    @ResponseBody
    public List<Activity> loadUnderApplyActivity(){
        List<Activity> activityList = activityService.loadUnderApplyActivity();
        statusService.addValueOfStatus(activityList);
        return activityList;
    }

    @RequestMapping("/loadMyActivity")
    @ResponseBody
    public List<Activity> loadMyActivity(){
        List<Activity> activityList = activityService.loadMyActivity();
        statusService.addValueOfStatus(activityList);
        return activityList;
    }

    @RequestMapping("/loadActivityByTime")
    @ResponseBody
    public List<Activity> loadActivityByTime(@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date startTime, @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date endTime){
        List<Activity> activityList = activityService.loadActivityByTime(startTime, endTime);
        statusService.addValueOfStatus(activityList);
        return  activityList;
    }
}
