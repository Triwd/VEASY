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

    public String getStatusById(Integer id){
        String statusName = statusMapper.getStatusById(id);
        return statusName;
    }

    public void addValueOfStatus(List<Activity> activityList) {
        for (Activity item : activityList) { item.setStatus(getStatusById(item.getStatusId())); }
    }
}
