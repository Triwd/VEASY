package org.veasy.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.veasy.entity.Feedback;
import org.veasy.mapper.FeedbackMapper;

import java.util.Date;

@Component
public class QueueConsumerListener {
    @Autowired
    FeedbackMapper feedbackMapper;

    //queue模式的消费者
    @JmsListener(destination = "${spring.activemq.queue-name}", containerFactory = "queueListener")
    public boolean readActiveQueue(String content) {
        Feedback feedback = new Feedback();
        feedback.setStudentId(0);
        feedback.setContent(content);
        feedback.setSubmitTime(new Date());
        return feedbackMapper.submitFeedback(feedback);
    }
}