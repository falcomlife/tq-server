package com.sorawingwind.storage.controller;

import com.cotte.estate.bean.pojo.ao.storage.OutStorageAo;
import com.cotte.estate.bean.pojo.ao.storage.UserAo;
import com.cotte.estate.bean.pojo.doo.storage.CompanyDo;
import com.cotte.estate.bean.pojo.doo.storage.OutStorageDo;
import com.cotte.estate.bean.pojo.doo.storage.UserDo;
import com.cotte.estatecommon.RS;
import com.cotte.estatecommon.utils.CodeGenerUtil;
import com.cotte.estatecommon.utils.UUIDUtil;
import com.sorawingwind.storage.dao.CompanyDao;
import com.sorawingwind.storage.dao.UserDao;
import io.ebean.Ebean;
import org.apache.catalina.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserDao userDao;
    @Autowired
    private CompanyDao companyDao;

    @PostMapping
    public RS save(@RequestBody UserAo userAo) {
        UserDo userDo = new UserDo();
        BeanUtils.copyProperties(userAo, userDo);
        CompanyDo companyDo = this.companyDao.getByCode(userAo.getCompanyCode());
        if(companyDo == null){
            return RS.bad("未找到编码为"+userAo.getCompanyCode()+"的公司，或公司未审核通过。");
        }
        userDo.setCompanyId(companyDo.getId());
        UserDo userOld = this.userDao.getByAccount(userAo.getAccount());
        if(userOld != null){
            return RS.bad("账户已经存在");
        }
        userDo.setCreateTime(new Date());
        userDo.setIsDelete(false);
        userDo.setIsLock(false);
        userDo.setPassword("{bcrypt}" + new BCryptPasswordEncoder().encode(userAo.getPassword()));
        this.userDao.save(userDo);
        return RS.ok();
    }

    @PutMapping
    public RS rebackPassword(@RequestBody UserAo userAo) {
        UserDo userDo = new UserDo();
        BeanUtils.copyProperties(userAo, userDo);
        userDo.setPassword("{bcrypt}" + new BCryptPasswordEncoder().encode("123456"));
        this.userDao.rebackPassword(userDo);
        return RS.ok();
    }

    @GetMapping("/tokenVail")
    public RS tokenVail(){
        return RS.ok();
    }
}
