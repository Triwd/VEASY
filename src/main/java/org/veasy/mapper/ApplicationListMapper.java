package org.veasy.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.veasy.entity.ApplicationList;

import java.util.Date;

@Mapper
public interface ApplicationListMapper {

    boolean applyActivity(ApplicationList applicationList);

    //判断当前用户是否已经报名了id为 activityId 的活动
    boolean isApplied(@Param("studentId") Integer currentId, Integer activityId);
}
