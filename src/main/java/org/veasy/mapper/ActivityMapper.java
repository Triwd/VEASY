package org.veasy.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.veasy.entity.Activity;

import java.util.Date;
import java.util.List;

@Mapper
public interface ActivityMapper {

    //返回创建活动结果
    int createActivityByAdmin(Activity activity);

    //返回取消活动的结果
    boolean cancelActivity(Integer activityId);

    //返回所有活动
    List<Activity> loadAllActivity();

    //返回对应id的活动
    Activity loadActivityById(Integer activityId);

    //返回状态为“报名中”的活动(用户用)
    List<Activity> loadUnderApplyActivity();

    //返回状态为“已结束”的活动（管理员用）
    List<Activity> loadEndActivity();

    //返回用户申请的活动
    List<Activity> loadAppliedActivity(@Param("studentId") Integer currentId);

    //返回用户参与的活动
    List<Activity> loadMyActivity(Integer studentId);

    //返回时间区间内的活动
    List<Activity> loadActivityByTime(@Param("sT") Date startTime, @Param("eT") Date endTime);

    //更新活动剩余名额（关闭报名时用）
    boolean updateRestNum(Integer activityId, Integer restNum);

    //根据活动Id写入活动总结
    boolean activitySummary(Integer activityId, String summary);

    //根据活动Id获取活动开启的时间
    Date getSignTimeById(Integer activityId);

    //根据活动Id设置报名开启的时间
    boolean setSignTimeById(Integer activityId, Date signTime);

}
