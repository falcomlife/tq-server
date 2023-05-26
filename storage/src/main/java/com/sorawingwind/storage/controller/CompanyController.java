package com.sorawingwind.storage.controller;

import com.cotte.estate.bean.pojo.ao.storage.CompanyAo;
import com.cotte.estate.bean.pojo.doo.storage.CompanyDo;
import com.cotte.estatecommon.PageRS;
import com.cotte.estatecommon.RS;
import com.cotte.estatecommon.utils.ListUtil;
import com.sorawingwind.storage.dao.CompanyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyDao dao;

    @GetMapping
    public RS getByPage(int pageIndex, int pageSize, String name, String code, String starttime, String endtime) {
        List<CompanyDo> list = this.dao.getByPage(pageIndex, pageSize, name, code, starttime, endtime);
        int totleRowCount = this.dao.getCountByPage(name, code, starttime, endtime);
        List<CompanyAo> listaor = new ListUtil<CompanyDo,CompanyAo>().copyList(list, CompanyAo.class);
        return RS.ok(new PageRS<>(pageSize, pageIndex, totleRowCount, totleRowCount / pageSize, listaor));
    }

}
