package org.veasy.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StatusMapper {
     String getStatusById(Integer id);

     Boolean setActivityStatusById(Integer activityId, Integer id);
}
