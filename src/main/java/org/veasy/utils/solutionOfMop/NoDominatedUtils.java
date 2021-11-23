package org.veasy.utils.solutionOfMop;

import org.springframework.beans.factory.annotation.Autowired;
import org.veasy.mapper.ActivityMapper;
import org.veasy.mapper.ApplicationListMapper;
import org.veasy.mapper.UserMapper;

import java.util.Set;


public class NoDominatedUtils {

    @Autowired
    ActivityMapper activityMapper;

    @Autowired
    ApplicationListMapper applicationListMapper;

    @Autowired
    UserMapper userMapper;

    public Integer[] calNoDominatedVolunteer(Integer activityId, Set<Integer> volunteers) {
        Integer volunteerNum = activityMapper.loadActivityById(activityId).getVolunteerNum();
        Integer[] noDominatedVolunteers = new Integer[volunteerNum + 1];
        Integer k = 0;
        for (Integer id : volunteers) {
            boolean isNoDom = true;
            for (Integer oid : volunteers) {
                if ((applicationListMapper.getApplyTime(id, activityId).getTime()
                        <= applicationListMapper.getApplyTime(oid, activityId).getTime())
                        || (userMapper.loadUserByStudentId(id).getActivityHours()
                        <= userMapper.loadUserByStudentId(oid).getActivityHours())) {
                    isNoDom = true;
                } else {
                    isNoDom = false;
                    break;
                }
            }
            if (isNoDom) {
                noDominatedVolunteers[k++] = id;
                volunteers.remove(id);
            }
            if (k == volunteerNum) break;
        }
        return noDominatedVolunteers;
    }
}
