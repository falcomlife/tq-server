package com.sorawingwind.storage.controller;

import cn.hutool.core.lang.Dict;
import com.cotte.estate.bean.pojo.ao.storage.DictAo;
import com.cotte.estate.bean.pojo.ao.storage.query.QDictAo;
import com.cotte.estate.bean.pojo.doo.storage.DictDo;
import com.cotte.estate.bean.pojo.doo.storage.OutStorageDo;
import com.cotte.estatecommon.RS;
import com.cotte.estatecommon.utils.ListUtil;
import com.cotte.estatecommon.utils.UUIDUtil;
import com.sorawingwind.storage.dao.DictDao;
import io.ebean.Ebean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dict")
public class DictController {

    @Autowired
    private DictDao dao;

    @GetMapping("/items")
    @PreAuthorize("hasAuthority('I-2')")
    public RS getDictByType(@RequestParam String type) {
        return this.dao.getDictByType(type);

    }

    @PostMapping("/item")
    @PreAuthorize("hasAuthority('I-2')")
    public RS save(@RequestBody DictAo ao) {
        ao.setId(UUIDUtil.simpleUUid());
        ao.setItem(UUIDUtil.simpleUUid());
        DictDo doo = new DictDo();
        BeanUtils.copyProperties(ao, doo);
        this.dao.save(doo);
        return RS.ok();
    }

    public List<DictDo> getDictDoByType(@RequestParam String type) {
        return this.dao.getDictDoByType(type);
    }

    public DictDo getById(String id) {
        if (StringUtils.isBlank(id)) {
            return new DictDo();
        }
        DictDo doo = this.dao.getById(id);
        if (doo == null) {
            return new DictDo();
        } else {
            return doo;
        }
    }

}
