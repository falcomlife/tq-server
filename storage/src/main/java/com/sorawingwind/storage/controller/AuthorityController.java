package com.sorawingwind.storage.controller;

import com.cotte.estate.bean.pojo.ao.storage.AuthorityAo;
import com.cotte.estate.bean.pojo.ao.storage.CompanyAo;
import com.cotte.estate.bean.pojo.doo.storage.AuthorityDo;
import com.cotte.estate.bean.pojo.doo.storage.CompanyDo;
import com.cotte.estatecommon.PageRS;
import com.cotte.estatecommon.RS;
import com.cotte.estatecommon.utils.ListUtil;
import com.sorawingwind.storage.dao.AuthorityDao;
import com.sorawingwind.storage.dao.CompanyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authority")
public class AuthorityController {

    @Autowired
    private AuthorityDao dao;

//    @GetMapping
//    public RS getAll() {
//        List<AuthorityDo> list = this.dao.getAll();
//        List<AuthorityAo> listaor = new ListUtil<AuthorityDo, AuthorityAo>().copyList(list, AuthorityAo.class);
//        return RS.ok(listaor);
//    }

    @GetMapping
    public RS getRoleAuthority(String roleId) {
        List<AuthorityDo> list1 = this.dao.getAll();
        List<AuthorityDo> list2 = this.dao.getRoleAuthority(roleId);
        List<AuthorityAo> listaor = new ListUtil<AuthorityDo, AuthorityAo>().copyList(list1, AuthorityAo.class);
        // 在该角色下的权限
        listaor.stream()
                .filter(item -> list2.stream().map(AuthorityDo::getId).collect(Collectors.toList()).contains(item.getId()))
                .forEach(item -> item.setInRole(true));
        // 不在该角色下的权限
        listaor.stream()
                .filter(item -> !list2.stream().map(AuthorityDo::getId).collect(Collectors.toList()).contains(item.getId()))
                .forEach(item -> item.setInRole(false));

        return RS.ok(listaor);
    }

}
