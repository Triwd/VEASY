package org.veasy.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.veasy.entity.ApplicationList;

@Mapper
public interface ApplicationListMapper {

    //v2.0 申请报名活动，申请成功返回true, 否则返回false
    boolean applyActivity(ApplicationList applicationList);

    //v2.0 取消申请，成功取消则返回true, 否则返回false
    boolean cancelApply(Integer studentId, Integer activityId);

    //判断当前用户是否已经报名了id为 activityId 的活动
    boolean isApplied(@Param("studentId") Integer currentId, Integer activityId);
}
