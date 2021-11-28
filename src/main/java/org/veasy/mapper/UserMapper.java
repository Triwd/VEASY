package org.veasy.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.veasy.entity.User;

@Mapper
public interface UserMapper {

    User loadUserByStudentNo(@Param("studentNo") String studentNo);

    String getIdCardByStudentNo(String currentStudentNo);

    int revisePwd(@Param("newPwd") String newPwd, @Param("id") Integer currentId);

    void updateActivityHoursById(Integer volunteerId, Float hours);

    User loadUserByStudentId(Integer studentId);

    Integer getActivityTimesById(Integer studentId);
}
