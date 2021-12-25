package org.veasy.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.veasy.entity.Feedback;
import org.veasy.entity.Notice;

import java.util.List;

@Mapper
public interface NoticeMapper {
    boolean publishNotice(Notice notice);

    List<Feedback> checkNotice();
}
