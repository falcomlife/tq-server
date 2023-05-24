package com.sorawingwind.storage.controller;

import com.cotte.estate.bean.pojo.ao.storage.OutStorageAo;
import com.cotte.estate.bean.pojo.ao.storage.UserAo;
import com.cotte.estate.bean.pojo.doo.storage.OutStorageDo;
import com.cotte.estate.bean.pojo.doo.storage.UserDo;
import com.cotte.estatecommon.RS;
import com.cotte.estatecommon.utils.CodeGenerUtil;
import com.cotte.estatecommon.utils.UUIDUtil;
import com.sorawingwind.storage.dao.UserDao;
import io.ebean.Ebean;
import org.apache.catalina.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserDao userDao;

    @PostMapping
    public RS save(@RequestBody UserAo userAo) {
        UserDo userDo = new UserDo();
        BeanUtils.copyProperties(userAo, userDo);
        userDo.setIsDelete(false);
        userDo.setIsLock(false);
        userDo.setPassword("{bcrypt}" + new BCryptPasswordEncoder().encode(userAo.getPassword()));
        this.userDao.save(userDo);
        return RS.ok();
    }
}
