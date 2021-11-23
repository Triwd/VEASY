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


    //供测试使用的接口
    @RequestMapping("/admin/loadAllActivity")
    @ResponseBody
    public List<Activity> loadAllActivity(){
        //加载数据，存入列表
        List<Activity> activityList = activityService.loadAllActivity();
        //为Activity实体类的状态字段赋值
        statusService.addValueOfStatus(activityList);
        return activityList;
    }

    /**
        “活动”菜单需要的接口
     **/
    @RequestMapping("/user/loadUnderApplyActivity")
    @ResponseBody
    public List<Activity> loadUnderApplyActivity(){
        List<Activity> activityList = activityService.loadUnderApplyActivity();
        statusService.addValueOfStatus(activityList);
        return activityList;
    }

    /**
        “我的”菜单需要的接口
     **/

    @RequestMapping("/user/loadMyActivity")
    @ResponseBody
    public List<Activity> loadMyActivity(){
        return activityService.loadMyActivity();
    }

    @RequestMapping(value = "/user/loadEndActivity")
    @ResponseBody
    public List<Activity> loadEndActivity(){
        List<Activity> activityList = activityService.loadEndActivity();
        statusService.addValueOfStatus(activityList);
        return activityList;
    }

}
