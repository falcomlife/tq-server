package com.sorawingwind.storage.dao;

import com.cotte.estate.bean.pojo.doo.storage.InStorageDo;
import com.cotte.estate.bean.pojo.doo.storage.OutStorageDo;
import io.ebean.Ebean;
import io.ebean.SqlQuery;
import io.ebean.SqlRow;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Repository
public class InStorageDao {
    public List<SqlRow> getByPage(int pageIndex, int pageSize, String customerNameItem, String code, String starttime, String endtime) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select ");
        sb.append("i.id,");
        sb.append("i.order_id,");
        sb.append("i.code,");
        sb.append("i.image,");
        sb.append("i.name,");
        sb.append("i.color,");
        sb.append("o.color as order_color,");
        sb.append("i.bunch_count,");
        sb.append("i.bake,");
        sb.append("i.in_count,");
        sb.append("i.unit,");
        sb.append("i.incoming_type,");
        sb.append("i.incoming_reason,");
        sb.append("i.bad_reason,");
        sb.append("i.create_time,");
        sb.append("i.modified_time,");
        sb.append("i.is_delete,");
        sb.append("o.customer_name,");
        sb.append("o.po_num,");
        sb.append("o.item,");
        sb.append("o.count,");
        sb.append("o.code as order_code,");
        sb.append("ot.code as out_storage_code");
        sb.append(" from b_in_storage i left join b_order o on o.id = i.order_id left join b_out_storage ot on ot.id = i.out_storage_id where i.is_delete = 0 ");
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
            sb.append(" and i.code like '%" + code + "%' ");
        }
        sb.append("order by i.create_time desc");
        SqlQuery sq = Ebean.createSqlQuery(sb.toString()).setParameter("customerNameItem", customerNameItem).setParameter("customer_name_item", customerNameItem).setParameter("starttime", starttime).setParameter("endtime", endtime);
        sq.setFirstRow((pageIndex - 1) * pageSize);
        sq.setMaxRows(pageSize);
        List<SqlRow> list = sq.findList();
        return list;
    }

    public int getCountByPage(int pageIndex, int pageSize, String customerNameItem, String code, String starttime, String endtime) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select ");
        sb.append(" count(*) as count");
        sb.append(" from b_in_storage i left join b_order o on o.id = i.order_id left join b_out_storage ot on ot.id = i.out_storage_id where i.is_delete = 0 ");
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
            sb.append(" and i.code like '%" + code + "%' ");
        }
        int totleRowCount = 0;
        SqlRow sqlRow = Ebean.createSqlQuery(sb.toString()).setParameter("customerNameItem", customerNameItem).setParameter("customer_name_item", customerNameItem).setParameter("starttime", starttime).setParameter("endtime", endtime).findOne();
        if (sqlRow != null && !sqlRow.isEmpty()) {
            totleRowCount = sqlRow.getInteger("count");
        }
        return totleRowCount;
    }

    public List<SqlRow> getByIdstr(String idsStr) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select ");
        sb.append("i.id,");
        sb.append("i.order_id,");
        sb.append("i.code,");
        sb.append("i.image,");
        sb.append("i.name,");
        sb.append("i.color,");
        sb.append("o.color as order_color,");
        sb.append("i.bunch_count,");
        sb.append("i.bake,");
        sb.append("i.in_count,");
        sb.append("i.unit,");
        sb.append("i.incoming_type,");
        sb.append("i.incoming_reason,");
        sb.append("i.create_time,");
        sb.append("i.modified_time,");
        sb.append("i.is_delete,");
        sb.append("o.customer_name,");
        sb.append("o.po_num,");
        sb.append("o.item,");
        sb.append("o.count,");
        sb.append("o.code as order_code,");
        sb.append("ot.code as out_storage_code");
        sb.append(" from b_in_storage i left join b_order o on o.id = i.order_id left join b_out_storage ot on ot.id = i.out_storage_id where i.is_delete = 0 and i.id in (" + idsStr + ")");
        sb.append("order by i.create_time desc");
        return Ebean.createSqlQuery(sb.toString()).findList();
    }

    public int getCodeCount(Date start, Date end) {
        return Ebean.createQuery(InStorageDo.class).where().ge("create_time", start).le("create_time", end).findCount();
    }

    public void save(InStorageDo doo) {
        Ebean.save(doo);
    }

    public void update(InStorageDo doo) {
        Ebean.update(doo);
    }

    public List<InStorageDo> getByIds(List<String> ids) {
        return Ebean.createQuery(InStorageDo.class).where().idIn(ids).findList();
    }

    public void updateAll(List<InStorageDo> list) {
        Ebean.updateAll(list);
    }

    public int getMouthStatistics(Date start, Date end) {
        return Ebean.createQuery(InStorageDo.class).where().ge("create_time", start).lt("create_time", end).findCount();
    }

    public List<InStorageDo> getMouthStatisticsReratio(Date start, Date end) {
        return Ebean.createQuery(InStorageDo.class).where().ge("create_time", start).lt("create_time", end).findList();
    }

    public List<InStorageDo> getByCode(String code) {
        return Ebean.createQuery(InStorageDo.class).where().like("code", "%" + code + "%").eq("is_delete", false).findList();
    }

    public InStorageDo getById(String id) {
        return Ebean.createQuery(InStorageDo.class).where().idEq(id).findOne();
    }

    public List<InStorageDo> getByOrderId(String orderId) {
        return Ebean.createQuery(InStorageDo.class).where().eq("is_delete", 0).eq("order_id", orderId).findList();
    }

    public List<InStorageDo> getByOutStorageId(String outStorageId) {
        return Ebean.createQuery(InStorageDo.class).where().eq("is_delete", 0).eq("out_storage_id", outStorageId).findList();
    }
}
