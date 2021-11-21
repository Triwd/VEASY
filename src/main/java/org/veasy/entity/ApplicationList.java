package org.veasy.entity;

import java.util.Date;

public class ApplicationList {
    private Integer id;
    private Integer studentId;
    private Integer activityId;
    private Date applyTime;

    public ApplicationList(Integer studentId, Integer activityId, Date applyTime){
        this.studentId = studentId;
        this.activityId = activityId;
        this.applyTime = applyTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }
}
