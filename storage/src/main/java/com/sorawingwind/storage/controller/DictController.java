package com.sorawingwind.storage.controller;

import cn.hutool.core.lang.Dict;
import com.cotte.estate.bean.pojo.ao.storage.DictAo;
import com.cotte.estate.bean.pojo.ao.storage.query.QDictAo;
import com.cotte.estate.bean.pojo.doo.storage.DictDo;
import com.cotte.estate.bean.pojo.doo.storage.OutStorageDo;
import com.cotte.estatecommon.RS;
import com.cotte.estatecommon.utils.ListUtil;
import com.cotte.estatecommon.utils.UUIDUtil;
import io.ebean.Ebean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dict")
public class DictController {

    @GetMapping("/items")
    public RS getDictByType(@RequestParam String type) {
        List<DictDo> dictdo = Ebean.createQuery(DictDo.class).where().eq("type", type).findList();
        return RS.ok(new ListUtil<DictDo, DictAo>().copyList(dictdo, DictAo.class));

    }

    @PostMapping("/item")
    public RS save(@RequestBody DictAo ao) {
        ao.setId(UUIDUtil.simpleUUid());
        ao.setItem(UUIDUtil.simpleUUid());
        DictDo doo = new DictDo();
        BeanUtils.copyProperties(ao, doo);
        Ebean.save(doo);
        return RS.ok();
    }

    public DictDo getById(String id) {
        if (StringUtils.isBlank(id)) {
            return new DictDo();
        }
        DictDo doo = Ebean.createQuery(DictDo.class).where().idEq(id).findOne();
        if (doo == null) {
            return new DictDo();
        } else {
            return doo;
        }
    }

}
