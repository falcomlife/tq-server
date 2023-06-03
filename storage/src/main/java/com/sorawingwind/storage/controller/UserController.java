package com.sorawingwind.storage.controller;

import cn.hutool.http.HttpRequest;
import com.cotte.estate.bean.pojo.ao.storage.OutStorageAo;
import com.cotte.estate.bean.pojo.ao.storage.RoleAo;
import com.cotte.estate.bean.pojo.ao.storage.UserAo;
import com.cotte.estate.bean.pojo.ao.storage.UserAo;
import com.cotte.estate.bean.pojo.doo.storage.*;
import com.cotte.estate.bean.pojo.doo.storage.UserDo;
import com.cotte.estatecommon.PageRS;
import com.cotte.estatecommon.RS;
import com.cotte.estatecommon.utils.CodeGenerUtil;
import com.cotte.estatecommon.utils.JWTUtil;
import com.cotte.estatecommon.utils.ListUtil;
import com.cotte.estatecommon.utils.UUIDUtil;
import com.sorawingwind.storage.config.security.JWTUtils;
import com.sorawingwind.storage.dao.CompanyDao;
import com.sorawingwind.storage.dao.RoleDao;
import com.sorawingwind.storage.dao.UserDao;
import io.ebean.Ebean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserDao dao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private CompanyDao companyDao;

    @GetMapping
    @PreAuthorize("hasAuthority('I-6')")
    public RS getByPage(int pageIndex, int pageSize, String name, String companyId) {
        List<UserDo> list = this.dao.getByPage(pageIndex, pageSize, name, companyId);
        int totleRowCount = this.dao.getCountByPage(name, companyId);
        List<UserAo> listaor = new ListUtil<UserDo, UserAo>().copyList(list, UserAo.class);
        listaor.stream().forEach(item -> {
            if (item.getIsLock()) {
                item.setIsLockName("是");
            } else {
                item.setIsLockName("否");
            }
        });
        return RS.ok(new PageRS<>(pageSize, pageIndex, totleRowCount, totleRowCount / pageSize, listaor));
    }

    @PostMapping("/role")
    @PreAuthorize("hasAuthority('I-6')")
    public RS saveRole(@RequestBody UserAo user) {
        if(user.getRoleFlag()){
            this.dao.saveRole(user.getId(), user.getRoleId());
        }else{
            this.dao.deleteRole(user.getId(), user.getRoleId());
        }
        return RS.ok();
    }

    @GetMapping("/role")
    @PreAuthorize("hasAuthority('I-6')")
    public RS getUserRole(String userId) {
        List<RoleDo> list1 = this.roleDao.getAll();
        List<RoleDo> list2 = this.roleDao.getRoleAuthority(userId);
        List<RoleAo> listaor = new ListUtil<RoleDo, RoleAo>().copyList(list1, RoleAo.class);
        // 在该角色下的权限
        listaor.stream()
                .filter(item -> list2.stream().map(RoleDo::getId).collect(Collectors.toList()).contains(item.getId()))
                .forEach(item -> item.setInUser(true));
        // 不在该角色下的权限
        listaor.stream()
                .filter(item -> !list2.stream().map(RoleDo::getId).collect(Collectors.toList()).contains(item.getId()))
                .forEach(item -> item.setInUser(false));

        return RS.ok(listaor);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('I-6')")
    public RS delete(@RequestBody List<String> ids) {
        List<UserDo> list = Ebean.createQuery(UserDo.class).where().idIn(ids).findList();
        for (UserDo doo : list) {
            doo.setIsDelete(true);
        }
        this.dao.updateAll(list);
        return RS.ok();
    }

    @PutMapping("/lock")
    @PreAuthorize("hasAuthority('I-6')")
    public RS lock(@RequestBody List<String> ids) {
        List<UserDo> list = Ebean.createQuery(UserDo.class).where().idIn(ids).findList();
        for (UserDo doo : list) {
            doo.setIsLock(true);
        }
        this.dao.updateAll(list);
        return RS.ok();
    }
    @PutMapping("/lock/none")
    @PreAuthorize("hasAuthority('I-6')")
    public RS noneLock(@RequestBody List<String> ids) {
        List<UserDo> list = Ebean.createQuery(UserDo.class).where().idIn(ids).findList();
        for (UserDo doo : list) {
            doo.setIsLock(false);
        }
        this.dao.updateAll(list);
        return RS.ok();
    }

    @PutMapping
    public RS update(@RequestBody UserAo userAo){
        UserDo userDo = this.dao.getById(userAo.getId());
        userDo.setName(userAo.getName());
        this.dao.update(userDo);
        return RS.ok();
    }

    @PostMapping
    public RS save(@RequestBody UserAo userAo) {
        UserDo userDo = new UserDo();
        BeanUtils.copyProperties(userAo, userDo);
        CompanyDo companyDo = this.companyDao.getByCode(userAo.getCompanyCode());
        if (companyDo == null) {
            return RS.bad("未找到编码为" + userAo.getCompanyCode() + "的公司，或公司未审核通过。");
        }
        userDo.setCompanyId(companyDo.getId());
        UserDo userOld = this.dao.getByAccount(userAo.getAccount());
        if (userOld != null) {
            return RS.bad("账户已经存在");
        }
        int count = this.dao.getUserCount();
        userDo.setCode((1000000 + count) + "");
        userDo.setCreateTime(new Date());
        userDo.setIsDelete(false);
        userDo.setIsLock(false);
        userDo.setName("用户");
        userDo.setPassword("{bcrypt}" + new BCryptPasswordEncoder().encode(userAo.getPassword()));
        this.dao.save(userDo);
        return RS.ok();
    }

    @PutMapping("/password")
    @PreAuthorize("hasAuthority('I-6')")
    public RS rebackPassword(@RequestBody UserAo userAo) {
        UserDo userDo = new UserDo();
        BeanUtils.copyProperties(userAo, userDo);
        userDo.setPassword("{bcrypt}" + new BCryptPasswordEncoder().encode("123456"));
        this.dao.rebackPassword(userDo);
        return RS.ok();
    }

    @PutMapping("/newPassword")
    public RS newPassword(@RequestBody UserAo userAo) {
        UserDo userOld = this.dao.getById(userAo.getId());
        if(new BCryptPasswordEncoder().matches(userAo.getPassword(),userOld.getPassword().substring(8))){
            userOld.setPassword("{bcrypt}" + new BCryptPasswordEncoder().encode(userAo.getNewPassword()));
            this.dao.rebackPassword(userOld);
        }else{
            return RS.bad("原密码输入错误");
        }

        return RS.ok();
    }

    @GetMapping("/tokenVail")
    public RS tokenVail(HttpServletRequest request) {
        return RS.ok(JWTUtils.decode(request.getHeader("token")));
    }
}
