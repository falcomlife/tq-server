package com.sorawingwind.storage.dao;

import com.cotte.estate.bean.pojo.doo.storage.OrderDo;
import com.cotte.estate.bean.pojo.eto.OrderEto;
import io.ebean.Ebean;
import io.ebean.ExpressionList;
import io.ebean.SqlRow;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Repository
public class OrderDao {

    public OrderDo getById(String id){
        return Ebean.createQuery(OrderDo .class).where().idEq(id).findOne();
    }

    public List<OrderDo> getByPage(int pageIndex, int pageSize, String customerNameItem, String code, String po, String item, String starttime, String endtime) {

        ExpressionList<OrderDo> el = Ebean.createQuery(OrderDo.class).where();
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
        el.setFirstRow((pageIndex - 1) * pageSize);
        el.setMaxRows(pageSize);
        List<OrderDo> list = null;
        if (StringUtils.isBlank(customerNameItem)) {
            list = el.order("create_time desc").findList();
        } else {
            list = el.order("po_num desc, create_time desc").findList();
        }
        return list;
    }

    public int getCountByPage(int pageIndex, int pageSize, String customerNameItem, String code, String po, String item, String starttime, String endtime) {

        ExpressionList<OrderDo> el = Ebean.createQuery(OrderDo.class).where();
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
        return el.findCount();
    }

    public void save(OrderDo doo) {
        Ebean.save(doo);
    }

    public void update(OrderDo doo) {
        Ebean.update(doo);
    }

    public void updateAll(List<OrderDo> list) {
        Ebean.updateAll(list);
    }

    public Integer getCountBetweenTimes(Date start, Date end) {
        return Ebean.createQuery(OrderDo.class).where().ge("create_time", start).lt("create_time", end).findCount();
    }

    public List<SqlRow> getCountBetweenTimesWithColor(String startStr, String endStr) {
        return Ebean.createSqlQuery("select color,sum( `sum` ) as count from `b_order` where sum is not null and create_time >= :start and create_time <= :end and is_delete = 0 GROUP BY color order by count asc").setParameter("start", startStr).setParameter("end", endStr).findList();
    }

    public List<SqlRow> getCountBetweenTimesWithCustomer(String startStr, String endStr) {
        return Ebean.createSqlQuery("select customer_name,sum( `sum` ) as count from `b_order` where sum is not null and create_time >= :start and create_time <= :end and is_delete = 0 GROUP BY customer_name order by count desc").setParameter("start", startStr).setParameter("end", endStr).findList();
    }

    public List<OrderDo> getByCode(String code) {
        return Ebean.createQuery(OrderDo.class).where().eq("code", code).eq("is_delete", false).findList();
    }

    public List<OrderDo> getExcels(String customerNameItem, String code, String po, String item, String starttime, String endtime) {
        ExpressionList<OrderDo> el = Ebean.createQuery(OrderDo.class).where();
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
        List<OrderDo> list = null;
        if (StringUtils.isBlank(customerNameItem)) {
            list = el.order("create_time desc").findList();
        } else {
            list = el.order("po_num desc, create_time desc").findList();
        }
        return list;
    }
}
