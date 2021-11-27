package org.veasy.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.veasy.entity.Feedback;

import java.util.List;

@Mapper
public interface FeedbackMapper {

    boolean submitFeedback(Feedback feedback);

    List<Feedback> checkFeedback();
}
