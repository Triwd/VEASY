package org.veasy.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StatusMapper {
    public String getStatusById(Integer id);
}
