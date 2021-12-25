package org.veasy.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.veasy.entity.Activity;
import org.veasy.service.ActivityService;
import org.veasy.service.StatusService;

import java.util.List;

@RestController
@Api(tags = "活动数据接口")
public class ActivityController {
    @Autowired
    ActivityService activityService;

    @Autowired
    StatusService statusService;

    @ApiOperation(value = "加载所有活动", notes = "管理员可以访问，获取所有活动的详细信息")
    @RequestMapping("/admin/loadAllActivity")
    @ResponseBody
    public List<Activity> loadAllActivity() {
        //加载数据，存入列表
        List<Activity> activityList = activityService.loadAllActivity();
        //为Activity实体类的状态字段赋值
        statusService.addValueOfStatus(activityList);
        return activityList;
    }

    @ApiOperation(value = "加载所有已经结束的活动", notes = "管理员可以访问，获取所有已经结束活动的详细信息")
    @RequestMapping(value = "/admin/loadEndActivity")
    @ResponseBody
    public List<Activity> loadEndActivity() {
        List<Activity> activityList = activityService.loadEndActivity();
        statusService.addValueOfStatus(activityList);
        return activityList;
    }

    @ApiOperation(value = "加载所有正在报名中的活动", notes = "学生可以访问，获取所有正在报名中活动的详细信息")
    @RequestMapping("/student/loadUnderApplyActivity")
    @ResponseBody
    public List<Activity> loadUnderApplyActivity() {
        List<Activity> activityList = activityService.loadUnderApplyActivity();
        statusService.addValueOfStatus(activityList);
        return activityList;
    }

    @ApiOperation(value = "加载当前用户报名失败的活动", notes = "学生可以访问，获取当前用户所有报名失败的活动的详细信息")
    @RequestMapping("/student/loadFailedActivity")
    @ResponseBody
    public List<Activity> loadFailedActivity() {
        List<Activity> activityList = activityService.loadFailedActivity();
        statusService.addValueOfStatus(activityList);
        return activityList;
    }

    @ApiOperation(value = "加载所有当前用户的活动", notes = "学生可以访问，获取当前用户相关活动的详细信息")
    @RequestMapping("/student/loadMyActivity")
    @ResponseBody
    public List<Activity> loadMyActivity() {
        List<Activity> activityList = activityService.loadMyActivity();
        statusService.addValueOfStatus(activityList);
        return activityList;
    }
}
