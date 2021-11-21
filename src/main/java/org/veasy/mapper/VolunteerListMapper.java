package org.veasy.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.veasy.entity.VolunteerList;

@Mapper
public interface VolunteerListMapper {

    boolean addVolunteer(VolunteerList volunteerList);

    //判断当前用户是否已经报名了id为 activityId 的活动
    boolean isAdded(Integer studentId, Integer activityId);
}
