package com.sorawingwind.storage.service;

import com.cotte.estate.bean.pojo.doo.storage.UserDo;
import com.cotte.estate.bean.pojo.dto.UserAuthenticationDto;
import com.sorawingwind.storage.config.security.UserDetailsImpl;
import com.sorawingwind.storage.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserDetailServiceImpl implements UserDetailsService, UserDetailsPasswordService{

    @Autowired
    private UserDao userDao;

    // 登录查询账密是否正确
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查询用户信息
        UserAuthenticationDto dto = new UserAuthenticationDto(null, null, username.split("/")[0], username.split("/")[1], null, null, null, null);
        UserAuthenticationDto user = this.userDao.loginAuthentication(dto);
        //如果没有查询到用户就抛出异常
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("查无该用户,请重试");
        }
        UserDetailsImpl userdetails = new UserDetailsImpl();
        userdetails.setId(user.getId());
        userdetails.setUsername(user.getUsername());
        userdetails.setPassword(user.getPassword());
        userdetails.setAuthorities(user.getAuthoritys());
        userdetails.setAccountNonLocked(true);
        userdetails.setEnabled(true);
        userdetails.setAccountNonExpired(true);
        userdetails.setCredentialsNonExpired(true);
        //TODO 查询对应的权限信息
        return userdetails;
    }

    // 更新用户后修改相应token
    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        return null;
    }
}