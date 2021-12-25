package org.veasy.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.veasy.entity.User;

import java.util.Date;
import java.util.List;

@Mapper
public interface UserMapper {

    User loadUserByStudentNo(@Param("studentNo") String studentNo);

    String getIdCardByStudentNo(String studentNo);

    int revisePwd(@Param("newPwd") String newPwd, @Param("id") Integer currentId);

    void updateActivityHoursById(Integer volunteerId, Float hours);

    User loadUserByStudentId(Integer studentId);

    Integer getActivityTimesById(Integer studentId);

    void submitFeedbackSecretly(Date date, String content);

    List<User> loadVolunteerByActivityId(Integer activityId);
}
