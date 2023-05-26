package com.sorawingwind.storage.dao;

import com.cotte.estate.bean.pojo.doo.storage.CompanyDo;
import com.cotte.estate.bean.pojo.doo.storage.CompanyDo;
import io.ebean.Ebean;
import io.ebean.ExpressionList;
import io.ebean.SqlRow;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class CompanyDao {

    public CompanyDo getById(String id){
        return Ebean.createQuery(CompanyDo .class).where().idEq(id).findOne();
    }

    public List<CompanyDo> getByPage(int pageIndex, int pageSize, String name, String code, String starttime, String endtime) {

        ExpressionList<CompanyDo> el = Ebean.createQuery(CompanyDo.class).where();
        if (StringUtils.isNotBlank(name)) {
            el.like("name", "%"+name+"%");
        }
        if (StringUtils.isNotBlank(code)) {
            el.like("code", "%"+code+"%");
        }
        if (StringUtils.isNotBlank(starttime)) {
            el.ge("create_time", starttime);
        }
        if (StringUtils.isNotBlank(endtime)) {
            el.le("create_time", endtime);
        }
        el.eq("is_delete", false);
        el.setFirstRow((pageIndex - 1) * pageSize);
        el.setMaxRows(pageSize);
        List<CompanyDo> list = null;
        list = el.order("create_time desc").findList();
        return list;
    }

    public int getCountByPage(String name, String code, String starttime, String endtime) {
        ExpressionList<CompanyDo> el = Ebean.createQuery(CompanyDo.class).where();
        if (StringUtils.isNotBlank(name)) {
            el.like("name", "%"+name+"%");
        }
        if (StringUtils.isNotBlank(code)) {
            el.like("code", "%"+code+"%");
        }
        if (StringUtils.isNotBlank(starttime)) {
            el.ge("create_time", starttime);
        }
        if (StringUtils.isNotBlank(endtime)) {
            el.le("create_time", endtime);
        }
        el.eq("is_delete", false);
        return el.findCount();
    }

    public void save(CompanyDo doo) {
        Ebean.save(doo);
    }

    public void update(CompanyDo doo) {
        Ebean.update(doo);
    }

    public void updateAll(List<CompanyDo> list) {
        Ebean.updateAll(list);
    }

    public Integer getCountBetweenTimes(Date start, Date end) {
        return Ebean.createQuery(CompanyDo.class).where().ge("create_time", start).lt("create_time", end).findCount();
    }

    public List<SqlRow> getCountBetweenTimesWithColor(String startStr, String endStr) {
        return Ebean.createSqlQuery("select color,sum( `sum` ) as count from `b_order` where create_time >= :start and create_time <= :end and is_delete = 0 GROUP BY color order by count desc").setParameter("start", startStr).setParameter("end", endStr).findList();
    }

    public CompanyDo getByCode(String code) {
        return Ebean.createQuery(CompanyDo.class).where().eq("code", code).eq("is_delete", false).eq("is_enable",true).findOne();
    }

    public List<CompanyDo> getExcels(String customerNameItem, String code, String po, String item, String starttime, String endtime) {
        ExpressionList<CompanyDo> el = Ebean.createQuery(CompanyDo.class).where();
        if (StringUtils.isNotBlank(customerNameItem)) {
            el.eq("customer_name", customerNameItem);
        }
        if (StringUtils.isNotBlank(starttime)) {
            el.ge("create_time", starttime);
        }
        if (StringUtils.isNotBlank(endtime)) {
            el.le("create_time", endtime);
        }
        if (StringUtils.isNotBlank(code)) {
            el.like("code", "%" + code + "%");
        }
        if (StringUtils.isNotBlank(po)) {
            el.like("po_num", "%" + po + "%");
        }
        if (StringUtils.isNotBlank(item)) {
            el.like("item", "%" + item + "%");
        }
        el.eq("is_delete", false);
        List<CompanyDo> list = null;
        if (StringUtils.isBlank(customerNameItem)) {
            list = el.order("create_time desc").findList();
        } else {
            list = el.order("po_num desc, create_time desc").findList();
        }
        return list;
    }
}
