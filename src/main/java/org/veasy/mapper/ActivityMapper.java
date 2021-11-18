package org.veasy.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.veasy.entity.Activity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Mapper
public interface ActivityMapper {
    //返回所有活动
    public List<Activity> loadAllActivity();

    //返回状态为“报名中”的活动
    public List<Activity> loadUnderApplyActivity();

    //返回状态为“已结束”的活动
    List<Activity> loadEndActivity();

    //返回用户参与的活动
    public List<Activity> loadMyActivity(Integer id);

    List<Activity> loadActivityByTime(@Param("sT") Date startTime, @Param("eT") Date endTime);

    public int createActivityByAdmin(Activity activity);
}
