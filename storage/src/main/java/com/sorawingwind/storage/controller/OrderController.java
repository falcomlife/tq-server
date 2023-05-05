package com.sorawingwind.storage.controller;

import com.cotte.estate.bean.pojo.ao.storage.OrderAo;
import com.cotte.estate.bean.pojo.doo.storage.OrderDo;
import com.cotte.estatecommon.PageRS;
import com.cotte.estatecommon.RS;
import com.cotte.estatecommon.utils.CodeGenerUtil;
import com.cotte.estatecommon.utils.ListUtil;
import com.cotte.estatecommon.utils.UUIDUtil;
import io.ebean.Ebean;
import io.ebean.ExpressionList;
import io.ebean.SqlRow;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private DictController dictController;

    @GetMapping
    public PageRS<OrderAo> getByPage(@RequestParam int pageIndex, @RequestParam int pageSize, @RequestParam(required = false) String customerNameItem, @RequestParam(required = false) String starttime, @RequestParam(required = false) String endtime) {
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
        el.eq("is_delete", false);
        int totleRowCount = el.findCount();
        el.setFirstRow((pageIndex - 1) * pageSize);
        el.setMaxRows(pageSize);
        List<OrderDo> list = el.order("create_time desc").findList();
        List<OrderAo> listao = new ListUtil<OrderDo, OrderAo>().copyList(list, OrderAo.class);
        List<OrderAo> listaor = listao.stream().map(item -> {
            OrderAo aoInner = new OrderAo();
            BeanUtils.copyProperties(item, aoInner);
            aoInner.setCustomerName(dictController.getById(item.getCustomerName()).getItemName());
            aoInner.setCustomerNameId(item.getCustomerName());
            return aoInner;
        }).collect(Collectors.toList());
        return new PageRS<>(pageSize, pageIndex, totleRowCount, totleRowCount / pageSize, listaor);
    }

    @PostMapping
    public RS save(@RequestBody OrderAo orderAo) {
        OrderDo doo = new OrderDo();
        BeanUtils.copyProperties(orderAo, doo);
        int count = Ebean.createQuery(OrderDo.class).where().eq("is_delete",0).findCount();
        doo.setCode(CodeGenerUtil.getCode("O",count));
        doo.setId(UUIDUtil.simpleUUid());
        doo.setCreateTime(new Date());
        doo.setIsDelete(0);
        Ebean.save(doo);
        return RS.ok();
    }

    @PutMapping
    public RS update(@RequestBody OrderAo orderAo) {
        OrderDo doo = new OrderDo();
        BeanUtils.copyProperties(orderAo, doo);
        doo.setCustomerName(orderAo.getCustomerNameId());
        doo.setModifiedTime(new Date());
        Ebean.update(doo);
        return RS.ok();
    }

    @DeleteMapping
    public RS delete(@RequestBody List<String> ids) {
        List<OrderDo> list = Ebean.createQuery(OrderDo.class).where().idIn(ids).findList();
        for (OrderDo doo : list) {
            doo.setIsDelete(1);
        }
        Ebean.updateAll(list);
        return RS.ok();
    }

    @GetMapping("/statistics")
    public RS getMouthStatistics(@RequestParam(required = false) String time) throws ParseException {
//        if(StringUtils.isBlank(time)){
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date end = new Date();
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(end);
//            calendar.set(Calendar.DAY_OF_MONTH,1);
//            calendar.set(Calendar.HOUR,0);
//            calendar.set(Calendar.MINUTE,0);
//            calendar.set(Calendar.SECOND,0);
//            calendar.set(Calendar.MILLISECOND,0);
//            Date start = calendar.getTime();
//            return RS.ok(Ebean.createQuery(OrderDo.class).where().ge("create_time",start).le("create_time",end).findCount());
//        }else{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = sdf.parse(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.add(calendar.MONTH, 2);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date end = calendar.getTime();
        return RS.ok(Ebean.createQuery(OrderDo.class).where().ge("create_time", start).lt("create_time", end).findCount());
//        }
    }

    @GetMapping("/statistics/color")
    public RS getMouthStatisticsColor(@RequestParam(required = false) String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = sdf.parse(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.add(calendar.MONTH, 2);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date end = calendar.getTime();
        String startStr = sdf.format(start);
        String endStr = sdf.format(end);
        List<SqlRow> list = Ebean.createSqlQuery("select color,sum( count ) as count from `order` where create_time >= :start and create_time <= :end and is_delete = 0 GROUP BY color order by count desc").setParameter("start", startStr).setParameter("end", endStr).findList();
        return RS.ok(list.stream().map(item -> {
            String colorid = item.getString("color");
            String count = item.getString("count");
            String color = dictController.getById(colorid).getItemName();
            Map<String, String> map = new HashMap<>();
            map.put("color", color);
            map.put("count", count);
            return map;
        }).collect(Collectors.toList()));
    }
}
