package org.veasy.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.veasy.entity.SelectMode;

@Mapper
public interface ModeMapper {

    SelectMode getUsingMode();

    Boolean setMode(Integer id, Integer use);
}
