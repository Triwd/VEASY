package org.veasy.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.veasy.entity.Role;
import org.veasy.entity.User;
import org.veasy.mapper.RolesMapper;
import org.veasy.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.veasy.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RolesMapper rolesMapper;

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
        return user;
    }

    public User loadUserById(Integer id) {
        return userMapper.loadUserById(id);
    }

    public String getCurrentStudentNo(){
        return Util.getCurrentUser().getUsername();
    }

    public Integer getCurrentId(){
        return Util.getCurrentUser().getId();
    }

    public boolean applyRevisePwd(String idCard) {
        String realIdCard = userMapper.getIdCardByStudentNo(getCurrentStudentNo());
        if(idCard == realIdCard) return true;
        else return false;
    }

    public boolean revisePwd(String newPwd) {
        if(userMapper.revisePwd(newPwd, getCurrentId()) == 1){
            return true;
        }
        else return false;
    }
}
