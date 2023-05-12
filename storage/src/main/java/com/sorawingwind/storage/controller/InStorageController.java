package com.sorawingwind.storage.controller;

import com.cotte.estate.bean.pojo.ao.storage.InStorageAo;
import com.cotte.estate.bean.pojo.ao.storage.OrderAo;
import com.cotte.estate.bean.pojo.ao.storage.OutStorageAo;
import com.cotte.estate.bean.pojo.doo.storage.InStorageDo;
import com.cotte.estate.bean.pojo.doo.storage.OrderDo;
import com.cotte.estate.bean.pojo.doo.storage.OutStorageDo;
import com.cotte.estatecommon.PageRS;
import com.cotte.estatecommon.RS;
import com.cotte.estatecommon.utils.CodeGenerUtil;
import com.cotte.estatecommon.utils.ListUtil;
import com.cotte.estatecommon.utils.UUIDUtil;
import io.ebean.Ebean;
import io.ebean.ExpressionList;
import io.ebean.SqlQuery;
import io.ebean.SqlRow;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/inStorage")
public class InStorageController {

    @Autowired
    private DictController dictController;

    @GetMapping
    public PageRS<InStorageAo> getByPage(@RequestParam int pageIndex, @RequestParam int pageSize, @RequestParam(required = false) String customerNameItem, @RequestParam(required = false) String code, @RequestParam(required = false) String starttime, @RequestParam(required = false) String endtime) {
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
        sb.append(" from in_storage i left join `order` o on o.id = i.order_id left join out_storage ot on ot.id = i.out_storage_id where i.is_delete = 0 ");
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
        int totleRowCount = 0;
        List<SqlRow> sqlRows = Ebean.createSqlQuery(sb.toString()).setParameter("customerNameItem", customerNameItem).setParameter("customer_name_item", customerNameItem).setParameter("starttime", starttime).setParameter("endtime", endtime).findList();
        if (sqlRows != null && !sqlRows.isEmpty()) {
            totleRowCount = sqlRows.size();
        }
        SqlQuery sq = Ebean.createSqlQuery(sb.toString()).setParameter("customerNameItem", customerNameItem).setParameter("customer_name_item", customerNameItem).setParameter("starttime", starttime).setParameter("endtime", endtime);
        sq.setFirstRow((pageIndex - 1) * pageSize);
        sq.setMaxRows(pageSize);
        List<SqlRow> list = sq.findList();

        List<InStorageAo> listaor = list.stream().map(item -> {
            InStorageAo aoInner = new InStorageAo();
            aoInner.setId(item.getString("id"));
            aoInner.setOutStorageCode(item.getString("out_storage_code"));
            aoInner.setCode(item.getString("code"));
            aoInner.setBunchCount(item.getInteger("bunch_count"));
            aoInner.setCreateTime(item.getDate("create_time"));
            aoInner.setImage(item.getString("image"));
            aoInner.setInCount(item.getString("in_count"));
            aoInner.setName(item.getString("name"));
            aoInner.setPoNum(item.getString("po_num"));
            aoInner.setItem(item.getString("item"));
            aoInner.setCount(item.getInteger("count"));
            aoInner.setOrderId(item.getString("order_id"));
            aoInner.setOrderCode(item.getString("order_code"));
            aoInner.setIsDelete(0);
            aoInner.setCustomerName(dictController.getById(item.getString("customer_name")).getItemName());
            aoInner.setCustomerNameId(item.getString("customer_name"));
            aoInner.setColor(dictController.getById(item.getString("color")).getItemName());
            aoInner.setColorId(item.getString("color"));
            aoInner.setOrderColor(dictController.getById(item.getString("order_color")).getItemName());
            aoInner.setOrderColorId(item.getString("order_color"));
            aoInner.setIncomingType(dictController.getById(item.getString("incoming_type")).getItemName());
            aoInner.setIncomingTypeId(item.getString("incoming_type"));
            aoInner.setIncomingReason(item.getString("incoming_reason"));
            aoInner.setBake(dictController.getById(item.getString("bake")).getItemName());
            aoInner.setBakeId(item.getString("bake"));
            return aoInner;
        }).collect(Collectors.toList());
        return new PageRS<>(pageSize, pageIndex, totleRowCount, totleRowCount / pageSize, listaor);
    }

    /**
     * @description: 打印时根据选中的数据重新查询
     * @author: sorawingwind
     * @date: 2023/5/4
     */
    @PostMapping("/ids")
    public List<InStorageAo> getByIds(@RequestBody List<String> ids) {
        StringBuffer idssb = new StringBuffer();
        for (String id : ids) {
            idssb.append("\"" + id + "\",");
        }
        String idsStr = idssb.toString().substring(0, idssb.toString().length() - 1);
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
        sb.append(" from in_storage i left join `order` o on o.id = i.order_id left join out_storage ot on ot.id = i.out_storage_id where i.is_delete = 0 and i.id in (" + idsStr + ")");
        sb.append("order by i.create_time desc");
        List<SqlRow> sqlRows = Ebean.createSqlQuery(sb.toString()).findList();

        List<InStorageAo> listaor = sqlRows.stream().map(item -> {
            InStorageAo aoInner = new InStorageAo();
            aoInner.setId(item.getString("id"));
            aoInner.setOutStorageCode(item.getString("out_storage_code"));
            aoInner.setCode(item.getString("code"));
            aoInner.setBunchCount(item.getInteger("bunch_count"));
            aoInner.setCreateTime(item.getDate("create_time"));
            aoInner.setImage(item.getString("image"));
            aoInner.setInCount(item.getString("in_count"));
            aoInner.setName(item.getString("name"));
            aoInner.setPoNum(item.getString("po_num"));
            aoInner.setItem(item.getString("item"));
            aoInner.setCount(item.getInteger("count"));
            aoInner.setOrderId(item.getString("order_id"));
            aoInner.setOrderCode(item.getString("order_code"));
            aoInner.setIsDelete(0);
            aoInner.setCustomerName(dictController.getById(item.getString("customer_name")).getItemName());
            aoInner.setCustomerNameId(item.getString("customer_name"));
            aoInner.setColor(dictController.getById(item.getString("color")).getItemName());
            aoInner.setColorId(item.getString("color"));
            aoInner.setOrderColor(dictController.getById(item.getString("order_color")).getItemName());
            aoInner.setOrderColorId(item.getString("order_color"));
            aoInner.setIncomingType(dictController.getById(item.getString("incoming_type")).getItemName());
            aoInner.setIncomingTypeId(item.getString("incoming_type"));
            aoInner.setIncomingReason(item.getString("incoming_reason"));
            aoInner.setBake(dictController.getById(item.getString("bake")).getItemName());
            aoInner.setBakeId(item.getString("bake"));
            return aoInner;
        }).collect(Collectors.toList());
        return listaor;
    }

    @PostMapping
    public RS save(@RequestBody InStorageAo inStorageAo) {
        InStorageDo doo = new InStorageDo();
        BeanUtils.copyProperties(inStorageAo, doo);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        Date start = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date end = calendar.getTime();
        int count = Ebean.createQuery(InStorageDo.class).where().ge("create_time", start).le("create_time", end).findCount();
        doo.setCode(CodeGenerUtil.getCode("I", count));
        doo.setId(UUIDUtil.simpleUUid());
        doo.setCreateTime(new Date());
        doo.setIsDelete(0);
        Ebean.save(doo);
        return RS.ok();
    }

    @PutMapping
    public RS update(@RequestBody InStorageAo inStorageAo) {
        InStorageDo doo = new InStorageDo();
        BeanUtils.copyProperties(inStorageAo, doo);
        doo.setColor(inStorageAo.getColorId());
        doo.setBake(inStorageAo.getBakeId());
        doo.setIncomingType(inStorageAo.getIncomingTypeId());
        doo.setModifiedTime(new Date());
        Ebean.update(doo);
        return RS.ok();
    }

    @DeleteMapping
    public RS delete(@RequestBody List<String> ids) {
        List<InStorageDo> list = Ebean.createQuery(InStorageDo.class).where().idIn(ids).findList();
        for (InStorageDo doo : list) {
            doo.setIsDelete(1);
        }
        Ebean.updateAll(list);
        return RS.ok();
    }

    @GetMapping("/statistics")
    public RS getMouthStatistics(@RequestParam(required = false) String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = sdf.parse(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.add(calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date end = calendar.getTime();
        return RS.ok(Ebean.createQuery(InStorageDo.class).where().ge("create_time", start).lt("create_time", end).findCount());
    }

    @GetMapping("/statistics/reratio")
    public RS getMouthStatisticsReratio(@RequestParam(required = false) String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = sdf.parse(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.add(calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date end = calendar.getTime();
        List<InStorageDo> recountList = Ebean.createQuery(InStorageDo.class).where().ge("create_time", start).lt("create_time", end).findList();
        int sumcount = 0;
        int recount = 0;
        for (InStorageDo doo : recountList) {
            if ("5".equals(doo.getIncomingType())) {
                if (doo.getBunchCount() != null) {
                    recount += doo.getBunchCount();
                }
            }
            if (doo.getBunchCount() != null) {
                sumcount += doo.getBunchCount();
            }
        }
        BigDecimal ratio = new BigDecimal(0);
        if (sumcount != 0) {
            ratio = (new BigDecimal(recount).divide(new BigDecimal(sumcount), 4, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(100)).setScale(2);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("recount", recount);
        map.put("ratio", ratio);
        return RS.ok(map);
    }

    @GetMapping("/code")
    public RS getByCode(@RequestParam(required = false) String code) {
        List<Map<String, String>> list = Ebean.createQuery(InStorageDo.class).where().like("code", "%" + code + "%").eq("is_delete", false).findList().stream().map(item -> {
            Map<String, String> map = new HashMap<>();
            map.put("label", item.getCode());
            map.put("value", item.getId());
            return map;
        }).collect(Collectors.toList());
        return RS.ok(list);
    }

    @GetMapping("/id")
    public RS getById(@RequestParam(required = true) String id) {
        InStorageAo iao = new InStorageAo();
        InStorageDo idoo = Ebean.createQuery(InStorageDo.class).where().idEq(id).findOne();
        BeanUtils.copyProperties(idoo, iao);
        OrderDo doo = Ebean.createQuery(OrderDo.class).where().idEq(idoo.getOrderId()).findOne();
        if (StringUtils.isNotBlank(doo.getCustomerName())) {
            iao.setCustomerName(dictController.getById(doo.getCustomerName()).getItemName());
        }
        if (StringUtils.isNotBlank(doo.getColor())) {
            iao.setColor(dictController.getById(doo.getColor()).getItemName());
        }
        iao.setBake(dictController.getById(idoo.getBake()).getItemName());
        iao.setCustomerNameId(doo.getCustomerName());
        iao.setPoNum(doo.getPoNum());
        iao.setItem(doo.getItem());
        iao.setCount(doo.getCount());
        return RS.ok(iao);
    }

    @GetMapping("/order")
    public RS getByOrderId(@RequestParam String orderId) {
        return RS.ok(Ebean.createQuery(InStorageDo.class).where().eq("is_delete", 0).eq("order_id", orderId).findList().stream().map(item -> {
            InStorageAo aoInner = new InStorageAo();
            BeanUtils.copyProperties(item, aoInner);
            aoInner.setColor(dictController.getById(item.getColor()).getItemName());
            aoInner.setColorId(item.getColor());
            aoInner.setIncomingType(dictController.getById(item.getIncomingType()).getItemName());
            aoInner.setIncomingTypeId(item.getIncomingType());
            aoInner.setIncomingReason(item.getIncomingReason());
            aoInner.setBake(dictController.getById(item.getBake()).getItemName());
            aoInner.setBakeId(item.getBake());
            List<OutStorageAo> list = new ArrayList<>();
            if (StringUtils.isNotBlank(item.getOutStorageId())) {
                OutStorageAo ao = new OutStorageAo();
                OutStorageDo doo = Ebean.createQuery(OutStorageDo.class).where().eq("id", item.getOutStorageId()).findOne();
                BeanUtils.copyProperties(doo, ao);
                list.add(ao);
                aoInner.setExpandType("outStorageByInStorage");
            } else {
                List<OutStorageDo> listInner = Ebean.createQuery(OutStorageDo.class).where().eq("in_storage_id", item.getId()).findList();
                list.addAll(new ListUtil<OutStorageDo, OutStorageAo>().copyList(listInner, OutStorageAo.class));
                aoInner.setExpandType("");
                aoInner.setExpandType("outStoragesByInStorage");
            }
            aoInner.setOutStorageList(list);
            return aoInner;
        }).collect(Collectors.toList()));
    }

    @GetMapping("/outStorage")
    public RS getByOutStorageId(@RequestParam String outStorageId) {
        return RS.ok(Ebean.createQuery(InStorageDo.class).where().eq("is_delete", 0).eq("out_storage_id", outStorageId).findList().stream().map(item -> {
            InStorageAo aoInner = new InStorageAo();
            BeanUtils.copyProperties(item, aoInner);
            aoInner.setColor(dictController.getById(item.getColor()).getItemName());
            aoInner.setColorId(item.getColor());
            aoInner.setIncomingType(dictController.getById(item.getIncomingType()).getItemName());
            aoInner.setIncomingTypeId(item.getIncomingType());
            aoInner.setIncomingReason(item.getIncomingReason());
            aoInner.setBake(dictController.getById(item.getBake()).getItemName());
            aoInner.setBakeId(item.getBake());
            return aoInner;
        }).collect(Collectors.toList()));
    }
}
