package org.veasy.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.veasy.entity.ApplicationList;

import java.util.Date;
import java.util.List;

@Mapper
public interface ApplicationListMapper {

    Boolean applyActivity(ApplicationList applicationList);

    //判断当前用户是否已经报名了id为 activityId 的活动
    Integer isApplied(Integer studentId, Integer activityId);

    List<Integer> loadVolunteersById(Integer activityId);

    Date getApplyTime(Integer studentId, Integer activityId);
}
