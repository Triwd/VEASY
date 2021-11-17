package org.veasy.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.veasy.entity.User;

@Mapper
public interface UserMapper {

    User loadUserById(@Param("id") Integer id);

    User loadUserByStudentNo(@Param("studentNo") String studentNo);

    int revisePwd(@Param("newPwd") String newPwd, @Param("id") Integer currentId);

    String getIdCardByStudentNo(String currentStudentNo);
}
