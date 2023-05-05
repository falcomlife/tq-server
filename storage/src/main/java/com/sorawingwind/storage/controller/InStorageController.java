package com.sorawingwind.storage.controller;

import com.cotte.estate.bean.pojo.ao.storage.InStorageAo;
import com.cotte.estate.bean.pojo.doo.storage.InStorageDo;
import com.cotte.estatecommon.PageRS;
import com.cotte.estatecommon.RS;
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
    public PageRS<InStorageAo> getByPage(@RequestParam int pageIndex, @RequestParam int pageSize, @RequestParam(required = false) String customerNameItem, @RequestParam(required = false) String starttime, @RequestParam(required = false) String endtime) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select * from in_storage i left join order o where i.is_delete = 0 ");
        if (StringUtils.isNotBlank(customerNameItem)) {
            sb.append(" and o.customer_name = :customerNameItem ");
        }
        if (StringUtils.isNotBlank(starttime)) {
            sb.append(" and i.create_time >= :starttime ");
        }
        if (StringUtils.isNotBlank(endtime)) {
            sb.append(" and i.create_time <= :endtime ");
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
            BeanUtils.copyProperties(item, aoInner);
            aoInner.setPoNum(dictController.getById(item.getString("o.po_name")).getItemName());
            aoInner.setItem(dictController.getById(item.getString("o.item")).getItemName());
            aoInner.setCount(dictController.getById(item.getString("o.count")).getItemName());
            aoInner.setCustomerName(dictController.getById(item.getString("o.customer_name")).getItemName());
            aoInner.setCustomerNameId(item.getString("o.customer_name"));
            aoInner.setColor(dictController.getById(item.getString("i.color")).getItemName());
            aoInner.setColorId(item.getString("i.color"));
            aoInner.setIncomingType(dictController.getById(item.getString("i.incoming_type")).getItemName());
            aoInner.setIncomingTypeId(item.getString("i.incoming_type"));
            aoInner.setBake(dictController.getById(item.getString("i.bake")).getItemName());
            aoInner.setBakeId(item.getString("i.bake"));
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
        ExpressionList<InStorageDo> el = Ebean.createQuery(InStorageDo.class).where();
        if (!ids.isEmpty()) {
            el.idIn(ids);
        }
        List<InStorageDo> list = el.order("create_time desc").findList();
        List<InStorageAo> listao = new ListUtil<InStorageDo, InStorageAo>().copyList(list, InStorageAo.class);
        List<InStorageAo> listaor = listao.stream().map(item -> {
            InStorageAo aoInner = new InStorageAo();
            BeanUtils.copyProperties(item, aoInner);

            aoInner.setCustomerName(dictController.getById(item.getCustomerName()).getItemName());
            aoInner.setCustomerNameId(item.getCustomerName());
            aoInner.setColor(dictController.getById(item.getColor()).getItemName());
            aoInner.setColorId(item.getColor());
            aoInner.setIncomingType(dictController.getById(item.getIncomingType()).getItemName());
            aoInner.setIncomingTypeId(item.getIncomingType());
            aoInner.setBake(dictController.getById(item.getBake()).getItemName());
            aoInner.setBakeId(item.getBake());
            return aoInner;
        }).collect(Collectors.toList());
        return listaor;
    }

    @PostMapping
    public RS save(@RequestBody InStorageAo inStorageAo) {
        InStorageDo doo = new InStorageDo();
        BeanUtils.copyProperties(inStorageAo, doo);
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
        doo.setBake(inStorageAo.getBakeId());
        doo.setColor(inStorageAo.getColorId());
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
        calendar.add(calendar.MONTH, 2);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
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
}
