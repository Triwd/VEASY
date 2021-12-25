package org.veasy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.veasy.entity.Activity;
import org.veasy.mapper.StatusMapper;

import java.util.List;

@Service
public class StatusService {
    @Autowired
    StatusMapper statusMapper;

    //根据Id获取状态名
    public String getStatusById(Integer id){
        return statusMapper.getStatusById(id);
    }

    //根据Id为活动填充状态名
    public void addValueOfStatus(List<Activity> activityList) {
        for (Activity item : activityList) { item.setStatus(getStatusById(item.getStatusId())); }
    }
}
