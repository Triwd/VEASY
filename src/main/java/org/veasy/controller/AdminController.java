package org.veasy.controller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.veasy.entity.Activity;
import org.veasy.entity.Feedback;
import org.veasy.entity.Response;
import org.veasy.entity.User;
import org.veasy.service.ActivityService;
import org.veasy.service.StatusService;
import org.veasy.service.UserService;
import org.veasy.utils.RedisUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
@Api(tags = "管理员功能接口")
public class AdminController {
    @Autowired
    ActivityService activityService;

    @Autowired
    UserService userService;

    @Autowired
    StatusService statusService;

    @Autowired
    RedisUtils redisUtils;

    @ApiOperation(value = "创建活动", notes = "管理员输入活动信息，可以创建新活动")
    @RequestMapping(value = "/createActivity")
    @ResponseBody
    public Response createActivity(Integer id, String name, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime, String location, Integer volunteerNum, String contactWay, Float hours, String description) {
        if (activityService.createActivityByAdmin(id, name, startTime, endTime, location, volunteerNum, contactWay, hours, description)) {
            return new Response("success", "新建活动成功！");
        } else return new Response("failed", "新建活动失败，请重试或者联系运维人员！");
    }

    @ApiOperation(value = "取消活动", notes = "根据活动id，管理员可以取消活动")
    @ApiImplicitParam(paramType = "query", name = "activityId", value = "活动id", required = true)
    @ApiResponses({
            @ApiResponse(code = 0, message = "失败"),
            @ApiResponse(code = 1, message = "成功")
    })
    @RequestMapping(value = "/cancelActivity")
    @ResponseBody
    public Response cancelActivity(@RequestParam Integer activityId) {
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
    public Response closeSign(Integer activityId) {
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

    //根据时间区间查找活动
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
        List<Activity> activities = userService.generateSeasonReport();
        Integer nums = activities.size();
        Double hour = 0.0;
        for (Activity activity : activities) {
            hour += activity.getHours() * activity.getVolunteerNum();
        }
        return "本季度共举办了" + nums.toString() + "次活动，累计时长：" + hour.toString() + "小时";
    }

    //生成年度报告
    @RequestMapping("/generateYearReport")
    @ResponseBody
    public String generateYearReport() {
        List<Activity> activities = userService.generateYearReport();
        Integer nums = activities.size();
        Double hour = 0.0;
        for (Activity activity : activities) {
            hour += activity.getHours() * activity.getVolunteerNum();
        }
        return "本年度共举办了" + nums.toString() + "次活动，累计时长：" + hour.toString() + "小时";
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

    //查看反馈
    @RequestMapping("/checkFeedback")
    @ResponseBody
    public List<Feedback> checkFeedback() {
        return userService.checkFeedback();
    }

    //发布公告
    @RequestMapping("/publishNotice")
    @ResponseBody
    public Response publishNotice(String content) {
        if (userService.publishNotice(content)) {
            return new Response("success", "您已成功发布公告");
        } else return new Response("failed", "发布失败，请重试或者联系管理员！");
    }

    //获取活动的志愿者名单
    @RequestMapping("/loadVolunteerByActivityId")
    @ResponseBody
    public List<User> loadVolunteerByActivityId(Integer activityId) {
        return userService.loadVolunteerByActivityId(activityId);
    }

    //
    @RequestMapping("/getUserById")
    @ResponseBody
    public User getUserById(Integer studentId) {
        return userService.loadUserById(studentId);
    }

    //上传图片
    @Value("${prop.upload-folder}")
    private String UPLOAD_FOLDER;

    @PostMapping("/upload")
    public Response upload(@RequestParam(name = "file", required = false) MultipartFile file, HttpServletRequest request) {
        if (file == null) {
            return new Response("failed", "请选择要上传的图片");
        }
        if (file.getSize() > 1024 * 1024 * 5) {
            return new Response("failed", "文件大小不能大于10M");
        }
        //获取文件后缀
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1, file.getOriginalFilename().length());
        if (!"jpg,jpeg,gif,png".toUpperCase().contains(suffix.toUpperCase())) {
            return new Response("failed", "请选择jpg,jpeg,gif,png格式的图片");
        }
        String savePath = UPLOAD_FOLDER;
        File savePathFile = new File(savePath);
        //通过UUID生成唯一文件名
        String filename = UUID.randomUUID().toString().replaceAll("-", "") + "." + suffix;
        try {
            //将文件保存指定目录
            file.transferTo(new File(savePath + filename));
        } catch (Exception e) {
            e.printStackTrace();
            return new Response("failed", "保存文件异常");
        }
        //返回文件名称
        return new Response("success", filename);
    }
}
