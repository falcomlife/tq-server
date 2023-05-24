package com.sorawingwind.storage.dao;

import com.cotte.estate.bean.pojo.doo.storage.OutStorageDo;
import io.ebean.Ebean;
import io.ebean.SqlQuery;
import io.ebean.SqlRow;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class OutStorageDao {

    public List<SqlRow> getByPage(int pageIndex, int pageSize, String customerNameItem, String code, String starttime, String endtime) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select ");
        sb.append(" ot.id, ");
        sb.append(" ot.in_storage_id, ");
        sb.append(" ot.bunch_count, ");
        sb.append(" ot.out_count, ");
        sb.append(" ot.code, ");
        sb.append(" ot.out_type, ");
        sb.append(" ot.create_time, ");
        sb.append(" ot.modified_time, ");
        sb.append(" ot.is_delete, ");
        sb.append(" o.customer_name, ");
        sb.append(" o.po_num, ");
        sb.append(" o.item, ");
        sb.append(" o.color, ");
        sb.append(" o.count, ");
        sb.append(" o.code as order_code, ");
        sb.append(" i.code as in_storage_code, ");
        sb.append(" i.name as name, ");
        sb.append(" i.image as image, ");
        sb.append(" i.bake as bake, ");
        sb.append(" i.in_count as in_count ");
        sb.append(" from b_out_storage ot ");
        sb.append(" left join b_in_storage i on ot.in_storage_id = i.id");
        sb.append(" left join `b_order` o on o.id = i.order_id");
        sb.append("  where ot.is_delete = 0  ");
        if (StringUtils.isNotBlank(customerNameItem)) {
            sb.append(" and o.customer_name = :customerNameItem ");
        }
        if (StringUtils.isNotBlank(starttime)) {
            sb.append(" and i.create_time >= :starttime ");
        }
        if (StringUtils.isNotBlank(endtime)) {
            sb.append(" and i.create_time <= :endtime ");
        }
        if (StringUtils.isNotBlank(code)) {
            sb.append(" and ot.code like '%" + code + "%' ");
        }
        sb.append("order by i.create_time desc");
        SqlQuery sq = Ebean.createSqlQuery(sb.toString()).setParameter("customerNameItem", customerNameItem).setParameter("customer_name_item", customerNameItem).setParameter("starttime", starttime).setParameter("endtime", endtime);
        sq.setFirstRow((pageIndex - 1) * pageSize);
        sq.setMaxRows(pageSize);
        return sq.findList();
    }

    public Integer getCountByPage(int pageIndex, int pageSize, String customerNameItem, String code, String starttime, String endtime) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select ");
        sb.append(" count(*) as count ");
        sb.append(" from b_out_storage ot ");
        sb.append(" left join b_in_storage i on ot.in_storage_id = i.id");
        sb.append(" left join `b_order` o on o.id = i.order_id");
        sb.append("  where ot.is_delete = 0  ");
        if (StringUtils.isNotBlank(customerNameItem)) {
            sb.append(" and o.customer_name = :customerNameItem ");
        }
        if (StringUtils.isNotBlank(starttime)) {
            sb.append(" and i.create_time >= :starttime ");
        }
        if (StringUtils.isNotBlank(endtime)) {
            sb.append(" and i.create_time <= :endtime ");
        }
        if (StringUtils.isNotBlank(code)) {
            sb.append(" and ot.code like '%" + code + "%' ");
        }
        return Ebean.createSqlQuery(sb.toString()).setParameter("customerNameItem", customerNameItem).setParameter("customer_name_item", customerNameItem).setParameter("starttime", starttime).setParameter("endtime", endtime).findOne().getInteger("count");
    }

    public void save(OutStorageDo doo) {
        Ebean.save(doo);
    }

    public void update(OutStorageDo doo) {
        Ebean.update(doo);
    }

    public void updataAll(List<OutStorageDo> list) {
        Ebean.updateAll(list);
    }

    public List<SqlRow> getByCode(String code, String orderId, String item) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select ");
        sb.append(" ot.id, ");
        sb.append(" ot.in_storage_id, ");
        sb.append(" ot.bunch_count, ");
        sb.append(" ot.out_count, ");
        sb.append(" ot.code, ");
        sb.append(" ot.out_type, ");
        sb.append(" ot.create_time, ");
        sb.append(" ot.modified_time, ");
        sb.append(" ot.is_delete, ");
        sb.append(" o.customer_name, ");
        sb.append(" o.po_num, ");
        sb.append(" o.item, ");
        sb.append(" o.color, ");
        sb.append(" o.count, ");
        sb.append(" o.code as order_code, ");
        sb.append(" i.code as in_storage_code, ");
        sb.append(" i.name as name, ");
        sb.append(" i.image as image, ");
        sb.append(" i.bake as bake, ");
        sb.append(" i.in_count as in_count ");
        sb.append(" from b_out_storage ot ");
        sb.append(" left join b_in_storage i on ot.in_storage_id = i.id");
        sb.append(" left join `b_order` o on o.id = i.order_id");
        sb.append("  where ot.is_delete = 0  ");
        if (StringUtils.isNotBlank(code)) {
            sb.append(" and ot.code like '%" + code + "%' ");
        }
        if (StringUtils.isNotBlank(orderId)) {
            sb.append(" and o.id = '" + orderId + "' ");
        }
        if (StringUtils.isNotBlank(item)) {
            sb.append(" and o.item = '" + item + "' ");
        }
        return Ebean.createSqlQuery(sb.toString()).findList();
    }

    public List<OutStorageDo> getByInStorageId(String inStorageId) {
        return Ebean.createQuery(OutStorageDo.class).where().eq("is_delete", 0).eq("in_storage_id", inStorageId).findList();
    }

    public List<OutStorageDo> getById(String outStorageId) {
        return Ebean.createQuery(OutStorageDo.class).where().eq("is_delete", 0).eq("id", outStorageId).findList();
    }
}
