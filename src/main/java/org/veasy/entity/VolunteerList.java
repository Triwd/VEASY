package org.veasy.entity;

public class VolunteerList {
    private Integer id;
    private Integer studentId;
    private Integer activityId;

    public VolunteerList(Integer studentId, Integer activityId) {
        this.studentId = studentId;
        this.activityId = activityId;
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
}
