package org.veasy.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.veasy.entity.*;
import org.veasy.mapper.ActivityMapper;
import org.veasy.mapper.ApplicationListMapper;
import org.veasy.mapper.RolesMapper;
import org.veasy.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.veasy.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    ActivityMapper activityMapper;

    @Autowired
    RolesMapper rolesMapper;

    @Autowired
    ApplicationListMapper applicationListMapper;

    @Autowired
    private PasswordEncoder encoder;

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
    public Integer getCurrentId(){
        return Util.getCurrentUser().getId();
    }

    //获取当前用户的姓名
    public String getCurrentName(){ return Util.getCurrentUser().getName(); }

    //申请报名活动（MOP）
    public boolean applyActivity(Integer activityId){
        Date applyTime = new Date();
        ApplicationList applicationList = new ApplicationList(getCurrentId(), activityId, applyTime);
        if(applicationListMapper.isApplied(getCurrentId(),activityId)){
            return false;
        }
        else return applicationListMapper.applyActivity(applicationList);
    }

    //申请修改密码
    public boolean applyRevisePwd(String idCard) {
        String realIdCard = userMapper.getIdCardByStudentNo(getCurrentStudentNo());
        if(idCard == realIdCard) return true;
        else return false;
    }

    //修改密码
    public boolean revisePwd(String newPwd) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String pwdAfterEncode = bCryptPasswordEncoder.encode(newPwd);
        if(userMapper.revisePwd(pwdAfterEncode, getCurrentId()) == 1){
            return true;
        }
        else return false;
    }

    /**
     * Service of admin
     */

    //生成季度报告
    public List<Activity> generateSeasonReport(){

        Date currentDate = new Date();
        Calendar time = Calendar.getInstance();
        int currentDateMonth = currentDate.getMonth();
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
        time.set(Calendar.HOUR_OF_DAY,0);
        time.set(Calendar.MINUTE,0);
        Date startTime = time.getTime();
        time.set(Calendar.MONTH,11);
        time.set(Calendar.DAY_OF_MONTH,31);
        time.set(Calendar.HOUR_OF_DAY,0);
        time.set(Calendar.MINUTE,0);
        Date endTime = time.getTime();
        return activityMapper.loadActivityByTime(startTime, endTime);
    }
}
