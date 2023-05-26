package com.sorawingwind.storage.controller;

import com.cotte.estate.bean.pojo.ao.storage.RoleAo;
import com.cotte.estate.bean.pojo.ao.storage.RoleAo;
import com.cotte.estate.bean.pojo.ao.storage.RoleAo;
import com.cotte.estate.bean.pojo.doo.storage.RoleDo;
import com.cotte.estate.bean.pojo.doo.storage.RoleDo;
import com.cotte.estate.bean.pojo.doo.storage.RoleDo;
import com.cotte.estate.bean.pojo.doo.storage.RoleDo;
import com.cotte.estatecommon.PageRS;
import com.cotte.estatecommon.RS;
import com.cotte.estatecommon.utils.ListUtil;
import com.sorawingwind.storage.dao.RoleDao;
import com.sorawingwind.storage.dao.RoleDao;
import io.ebean.Ebean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleDao dao;

    @GetMapping
    public RS getByPage(int pageIndex, int pageSize, String name, String companyId) {
        List<RoleDo> list = this.dao.getByPage(pageIndex, pageSize, name, companyId);
        int totleRowCount = this.dao.getCountByPage(name, companyId);
        List<RoleAo> listaor = new ListUtil<RoleDo,RoleAo>().copyList(list, RoleAo.class);
        return RS.ok(new PageRS<>(pageSize, pageIndex, totleRowCount, totleRowCount / pageSize, listaor));
    }

    @PostMapping
    public RS save(@RequestBody RoleAo role){
        RoleDo doo = new RoleDo();
        BeanUtils.copyProperties(role,doo);
        doo.setCreateTime(new Date());
        doo.setIsDelete(false);
        this.dao.save(doo);
        return RS.ok();
    }

    @PostMapping("/authority")
    public RS saveAuthority(@RequestBody RoleAo role){
        this.dao.saveAuthority(role.getId(),role.getAuthorityId());
        return RS.ok();
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('I-3')")
    public RS delete(@RequestBody List<String> ids) {
        List<RoleDo> list = Ebean.createQuery(RoleDo.class).where().idIn(ids).findList();
        for (RoleDo doo : list) {
            doo.setIsDelete(true);
        }
        this.dao.updateAll(list);
        return RS.ok();
    }

}
