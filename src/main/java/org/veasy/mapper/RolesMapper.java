package org.veasy.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.veasy.entity.Role;

import java.util.List;

@Mapper
public interface RolesMapper {

    List<Role> getRolesByUserId(@Param("userId") Integer userId);
}
