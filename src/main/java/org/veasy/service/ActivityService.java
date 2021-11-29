package org.veasy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.veasy.entity.Activity;
import org.veasy.entity.ApplicationList;
import org.veasy.entity.VolunteerList;
import org.veasy.mapper.*;
import org.veasy.utils.RedisUtils;
import org.veasy.utils.Util;
import org.veasy.utils.solutionOfMop.CalculateUtils;
import org.veasy.utils.solutionOfMop.HandleResult;
import org.veasy.utils.solutionOfMop.NoDominatedUtils;

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

    @Autowired
    ModeMapper modeMapper;

    @Autowired
    StatusMapper statusMapper;

    /**
     * Service for admin
     */

    //管理员创建活动(直接写入SQL)
    public boolean createActivityByAdmin(Activity activity) {
        return activityMapper.createActivityByAdmin(activity) == 1;
    }

    //取消活动，将活动id和名额从redis和MySQL中移除
    public boolean cancelActivity(Integer activityId){
        if (activityMapper.cancelActivity(activityId)) { //如果删除数据库中的活动成功则去删除redis中的对应信息
            return redisUtils.removeActivityCache(activityId);
        } else return false;
    }

    //开启报名，将活动id和剩余名额存入缓存中
    public boolean openSign(Integer activityId) {
        activityMapper.setSignTimeById(activityId, new Date());
        Activity activity = activityMapper.loadActivityById(activityId);
        statusMapper.setActivityStatusById(activityId, 1);
        return redisUtils.addActivityCache(activityId, activity.getRestNum());
    }

    //关闭报名，将活动id和名额从redis中移除，并将剩余名额写入MySQL
    public boolean closeSign(Integer activityId) {
        Set<Integer> volunteers = redisUtils.getAllStudentId(activityId);
        Date applyTime = new Date();
        //先把redis中活动报名名单(small key)写入MySQL的申请表中
        for (Integer studentId : volunteers) {
            //写申请表
            if (Boolean.FALSE.equals(applicationListMapper.isApplied(studentId, activityId) != null)) {//如果没有申请记录则写入申请表中
                if (studentId == 0) continue;
                ApplicationList applicationList = new ApplicationList(studentId, activityId, applyTime);
                applicationListMapper.applyActivity(applicationList);//返回值怎么考虑？
            }
        }
        //根据模式不同，以不同方式写报名表
        //如果为普通选拔模式
        if (modeMapper.getUsingMode().getId() == 1) {
            for (Integer studentId : volunteers) {
                if (studentId == 0) continue;
                if (Boolean.FALSE.equals(volunteerListMapper.isAdded(studentId, activityId) != null)) {//如果没有记录写入志愿者表中
                    VolunteerList volunteerList = new VolunteerList(studentId, activityId);
                    volunteerListMapper.addVolunteer(volunteerList);//返回值怎么考虑？
                }
            }
        }
        //如果为多目标选拔模式
        else {
            Double[] aVolunteer = CalculateUtils.appropriateVolunteer();
            //处理MOP计算结果，得到实际活动情况下最优志愿者信息
            HandleResult.handleHours(aVolunteer[0]);
            HandleResult.handleDate(aVolunteer[1], activityMapper.getSignTimeById(activityId), applyTime);
            //将多目标优化算法选拔的志愿者写入志愿者表
            NoDominatedUtils noDominatedUtils = new NoDominatedUtils();
            //获取经过非支配排序的结果
            Integer[] noDominatedVolunteersId = noDominatedUtils.calNoDominatedVolunteer(activityId, volunteers);
            for (Integer studentId : noDominatedVolunteersId) {
                if (Boolean.FALSE.equals(volunteerListMapper.isAdded(studentId, activityId))) {//如果没有记录写入志愿者表中
                    VolunteerList volunteerList = new VolunteerList(studentId, activityId);
                    volunteerListMapper.addVolunteer(volunteerList);//返回值怎么考虑？
                }
            }
        }
        statusMapper.setActivityStatusById(activityId, 2);
        activityMapper.updateRestNum(activityId, redisUtils.getRestNum(activityId));
        //从缓存中移除活动
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

    //获取用户申请过的活动
    public List<Activity> loadAppliedActivity() {
        return activityMapper.loadAppliedActivity(userService.getCurrentId());
    }

    ;

    //获取当前用户已参与的活动（包括报名成功“即将开始“和”已结束“）
    public List<Activity> loadMyActivity() {
        return activityMapper.loadMyActivity(Util.getCurrentUser().getId());
    }

    //获取报名失败的活动
    public List<Activity> loadFailedActivity() {
        List<Activity> activities = loadAppliedActivity();
        List<Activity> myActivities = loadMyActivity();
        for (Activity myActivity : myActivities) {
            for (Activity appliedActivity : activities) {
                if (appliedActivity.getId() == myActivity.getId()) {
                    activities.remove(appliedActivity);
                    break;
                }
            }
        }
        return activities;
    }

    //获取已经结束的活动
    public List<Activity> loadEndActivity() {
        return activityMapper.loadEndActivity();
    }

}
