package org.veasy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.veasy.entity.Activity;
import org.veasy.entity.ApplicationList;
import org.veasy.entity.VolunteerList;
import org.veasy.mapper.ActivityMapper;
import org.veasy.mapper.ApplicationListMapper;
import org.veasy.mapper.VolunteerListMapper;
import org.veasy.utils.RedisUtils;
import org.veasy.utils.Util;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class ActivityService {
    @Autowired
    ActivityMapper activityMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    UserService userService;

    @Autowired
    ApplicationListMapper applicationListMapper;

    @Autowired
    VolunteerListMapper volunteerListMapper;

    /**
     * Service for admin
     */

    //管理员创建活动(直接写入SQL)
    public boolean createActivityByAdmin(Activity activity){
        return activityMapper.createActivityByAdmin(activity) == 1;
    }

    //取消活动，将活动id和名额从redis和MySQL中移除
    public boolean cancelActivity(Integer activityId){
        if(activityMapper.cancelActivity(activityId)){ //如果删除数据库中的活动成功则去删除redis中的对应信息
            return redisUtils.removeActivityCache(activityId);
        }else return false;
    }

    //开启报名，将活动id和名额存入缓存中
    public boolean openSign(Integer activityId){
        Activity activity = activityMapper.loadActivityById(activityId);
        if(activity.getVolunteerNum() == null) return false;//返回类型为Integer, 默认为null
        else return redisUtils.addActivityCache(activityId, activity.getVolunteerNum());
    }

    //关闭报名，将活动id和名额从redis中移除
    public boolean closeSign(Integer activityId){
        Set<Integer> volunteers = redisUtils.getAllStudentId(activityId);
        Date applyTime = new Date();
        //先把redis中活动的志愿者名单(small key)写入MySQL
        for(Integer studentId : volunteers){
            if(!applicationListMapper.isApplied(studentId,activityId)){//如果没有报名记录则写入报名表中
                ApplicationList applicationList = new ApplicationList(studentId, activityId, applyTime);
                applicationListMapper.applyActivity(applicationList);//返回值怎么考虑？
            }
            if(!volunteerListMapper.isAdded(studentId,activityId)){//如果没有记录则写入志愿者表中
                VolunteerList volunteerList = new VolunteerList(studentId, activityId);
                volunteerListMapper.addVolunteer(volunteerList);//返回值怎么考虑？
            }
        }
        return redisUtils.removeActivityCache(activityId);
    }

    //获取所有活动信息
    public List<Activity> loadAllActivity(){ return activityMapper.loadAllActivity(); }

    //获取时间区间内的活动
    public List<Activity> loadActivityByTime(Date startTime, Date endTime) {
        return activityMapper.loadActivityByTime(startTime, endTime);
    }

    //根据id获取活动剩余名额数量
    public Integer getRestNumById(Integer activityId){
        return redisUtils.getRestNum(activityId);
    }

    //根据id获取活动
    public Activity getActivityById(Integer activityId){
        return activityMapper.loadActivityById(activityId);
    }

    /**
     * Service for student
     */

    //获取“报名中”的活动
    public List<Activity> loadUnderApplyActivity() {
        return activityMapper.loadUnderApplyActivity();
    }

    //获取当前用户已参与的活动（包括报名成功“即将开始“和”已结束“）
    public List<Activity> loadMyActivity() {
        return activityMapper.loadMyActivity(Util.getCurrentUser().getId());
    }

    //获取已经结束的活动
    public List<Activity> loadEndActivity() {
        return activityMapper.loadEndActivity();
    }

    //取消活动报名
    public boolean cancelSign(Integer activityId){
        return redisUtils.removeStudentCache(activityId, userService.getCurrentId());
    }

}
