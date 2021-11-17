package org.veasy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.veasy.entity.Activity;
import org.veasy.mapper.ActivityMapper;

import java.util.Date;
import java.util.List;

@Service
public class ActivityService {
    @Autowired
    ActivityMapper activityMapper;

    public List<Activity> loadAllActivity(){ return activityMapper.loadAllActivity(); }

    public List<Activity> loadUnderApplyActivity() {
        return activityMapper.loadUnderApplyActivity();
    }

    //获取当前用户已参与的活动（包括报名成功“即将开始“和”已结束“）
    public List<Activity> loadMyActivity() {
        return activityMapper.loadMyActivity();
    }

    public List<Activity> loadActivityByTime(Date startTime, Date endTime) {
        return activityMapper.loadActivityByTime(startTime, endTime);
    }

    public boolean createActivityByAdmin(Activity activity){
        if(activityMapper.createActivityByAdmin(activity) == 1) return true;
        else return false;
    }
}
