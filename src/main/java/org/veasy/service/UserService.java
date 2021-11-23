package org.veasy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.veasy.entity.Activity;
import org.veasy.entity.Role;
import org.veasy.entity.User;
import org.veasy.mapper.*;
import org.veasy.utils.RedisUtils;
import org.veasy.utils.Util;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    ActivityMapper activityMapper;

    @Autowired
    RolesMapper rolesMapper;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    ApplicationListMapper applicationListMapper;

    @Autowired
    ModeMapper modelMapper;

    @Autowired
    StatusMapper statusMapper;

    @Override
    public UserDetails loadUserByUsername(String studentNo) throws UsernameNotFoundException {
        User user = userMapper.loadUserByStudentNo(studentNo);
        if (user == null) {
            //避免返回null，这里返回一个不含有任何值的User对象，在后期的密码比对过程中一样会验证失败
            throw new UsernameNotFoundException("用户不存在");
        }
        //查询用户的角色信息，并返回存入user中
        List<Role> roles = rolesMapper.getRolesByUserId(user.getId());
        user.setRoles(roles);
        user.setEnabled(true);
        return user;
    }

    /**
     * Service of student
     */

    //获取当前用户的学号
    public String getCurrentStudentNo(){
        return Util.getCurrentUser().getUsername();
    }

    //获取当前用户的id
    public Integer getCurrentId() {
        return Util.getCurrentUser().getId();
    }

    //获取当前用户的姓名
    public String getCurrentName() {
        return Util.getCurrentUser().getName();
    }

    //报名活动
    public boolean signUp(Integer activityId) {
        if (modelMapper.getUsingMode().getId() == 2) {//如果为多目标选拔
            return redisUtils.addStudentCacheInMOPMode(activityId, getCurrentId());
        } else return redisUtils.addStudentCacheInNormalMode(activityId, getCurrentId());//如果为普通选拔
    }

    //取消活动报名
    public boolean cancelSign(Integer activityId) {
        if (modelMapper.getUsingMode().getId() == 2) {
            return redisUtils.removeStudentCacheInMOPMode(activityId, getCurrentId());
        }
        return redisUtils.removeStudentCacheInNormalMode(activityId, getCurrentId());
    }

    //申请修改密码
    public boolean applyRevisePwd(String idCard) {
        String realIdCard = userMapper.getIdCardByStudentNo(getCurrentStudentNo());
        return Objects.equals(idCard, realIdCard);
    }

    //修改密码
    public boolean revisePwd(String newPwd) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String pwdAfterEncode = bCryptPasswordEncoder.encode(newPwd);
        return userMapper.revisePwd(pwdAfterEncode, getCurrentId()) == 1;
    }

    /**
     * Service of admin
     */

    //生成季度报告
    public List<Activity> generateSeasonReport(){
        Calendar time = Calendar.getInstance();
        int currentDateMonth = time.get(Calendar.MONTH);
        int startMonth = 1;
        if(currentDateMonth < 4) startMonth = 4;
        else if(currentDateMonth < 7) startMonth = 7;
        else if(currentDateMonth < 10) startMonth = 10;
        time.set(Calendar.MONTH,startMonth-1);
        time.set(Calendar.DAY_OF_MONTH,1);
        time.set(Calendar.HOUR_OF_DAY,0);
        time.set(Calendar.MINUTE,0);
        Date startTime = time.getTime();
        time.set(Calendar.MONTH,startMonth+1);
        time.set(Calendar.DAY_OF_MONTH,30);
        time.set(Calendar.HOUR_OF_DAY,0);
        time.set(Calendar.MINUTE,0);
        Date endTime = time.getTime();
        return activityMapper.loadActivityByTime(startTime, endTime);
    }

    //生成年度报告
    public List<Activity> generateYearReport() {
        Calendar time = Calendar.getInstance();
        time.set(Calendar.MONTH,0);
        time.set(Calendar.DAY_OF_MONTH,1);
        time.set(Calendar.HOUR_OF_DAY, 0);
        time.set(Calendar.MINUTE, 0);
        Date startTime = time.getTime();
        time.set(Calendar.MONTH, 11);
        time.set(Calendar.DAY_OF_MONTH, 31);
        time.set(Calendar.HOUR_OF_DAY, 0);
        time.set(Calendar.MINUTE, 0);
        Date endTime = time.getTime();
        return activityMapper.loadActivityByTime(startTime, endTime);
    }

    //开启普通选拔模式
    public boolean normalSelectModel() {
        //关闭多目标选拔模式并开启普通选拔模式
        return (modelMapper.setMode(2, 0) && modelMapper.setMode(1, 1));
    }

    //开启多目标选拔模式
    public boolean MOPSelectMode() {
        //关闭普通选拔模式并开启多目标选拔模式
        return (modelMapper.setMode(1, 0) && modelMapper.setMode(2, 1));
    }

    //活动结束总结
    public boolean activitySummary(Integer activityId, String summary) {
        Date nowTime = new Date();
        if (nowTime.compareTo(activityMapper.loadActivityById(activityId).getEndTime()) >= 0) {//现在时间已经大于等于结束时间
            List<Integer> volunteersId = applicationListMapper.loadVolunteersById(activityId);
            statusMapper.setActivityStatusById(activityId, 3);
            //更新志愿者时长
            Float hours = activityMapper.loadActivityById(activityId).getHours();
            for (Integer volunteerId : volunteersId) {
                userMapper.updateActivityHoursById(volunteerId, hours);
            }
            return activityMapper.activitySummary(activityId, summary);
        } else return false;
    }

    public User loadUserById(Integer studentId) {
        return userMapper.loadUserByStudentId(studentId);
    }
}
