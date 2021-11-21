package org.veasy.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.veasy.entity.User;

import java.util.Date;

@Mapper
public interface UserMapper {

    User loadUserByStudentNo(@Param("studentNo") String studentNo);

    String getIdCardByStudentNo(String currentStudentNo);

    int revisePwd(@Param("newPwd") String newPwd, @Param("id") Integer currentId);

}