package org.veasy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.veasy.entity.Response;

import javax.jms.Destination;
import javax.jms.Queue;
import javax.jms.Topic;

@RestController
public class ProducerController {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private Queue queue;

    @Autowired
    private Topic topic;

    //匿名提交反馈
    @RequestMapping("/student/submitFeedbackSecretly")
    @ResponseBody
    public Response sendQueue(String content) {
        this.sendMessage(this.queue, content);
        return new Response("sucess", "您已匿名提交反馈，管理员无法根据反馈查询到您的信息");
    }

    //不要使用这个接口，除非你知道他是干嘛的
    @RequestMapping("/admin/dontUse")
    @ResponseBody
    public Response sendTopic(String content) {
        this.sendMessage(this.topic, content);
        return new Response("sucess", "您已成功发布一则公告");
    }

    // 发送消息，destination是发送到的队列，message是待发送的消息
    private void sendMessage(Destination destination, final String message) {
        jmsMessagingTemplate.convertAndSend(destination, message);
    }
}