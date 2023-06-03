package com.sorawingwind.storage.dao;


import com.cotte.estate.bean.pojo.ao.storage.DictAo;
import com.cotte.estate.bean.pojo.doo.storage.DictDo;
import com.cotte.estatecommon.RS;
import com.cotte.estatecommon.utils.ListUtil;
import io.ebean.Ebean;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public class DictDao {

    public RS getDictByType(String type) {
        List<DictDo> dictdo = Ebean.createQuery(DictDo.class).where().eq("type", type).findList();
        return RS.ok(new ListUtil<DictDo, DictAo>().copyList(dictdo, DictAo.class));
    }

    public List<DictDo> getDictDoByType(String type) {
        return Ebean.createQuery(DictDo.class).where().eq("type", type).findList();
    }

    public void save(DictDo doo) {
        Ebean.save(doo);
    }

    public DictDo getById(String id) {
        return Ebean.createQuery(DictDo.class).where().idEq(id).findOne();
    }
}
