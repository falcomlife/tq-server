package com.sorawingwind.storage.controller;

import com.cotte.estate.bean.pojo.ao.storage.OrderAo;
import com.cotte.estate.bean.pojo.doo.storage.InStorageDo;
import com.cotte.estate.bean.pojo.doo.storage.OrderDo;
import com.cotte.estate.bean.pojo.doo.storage.OutStorageDo;
import com.cotte.estatecommon.PageRS;
import com.cotte.estatecommon.RS;
import com.cotte.estatecommon.enums.InUnit;
import com.cotte.estatecommon.utils.CodeGenerUtil;
import com.cotte.estatecommon.utils.ListUtil;
import com.cotte.estatecommon.utils.UUIDUtil;
import com.cotte.estatecommon.enums.OutType;
import io.ebean.Ebean;
import io.ebean.ExpressionList;
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
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private DictController dictController;

    @GetMapping
    public PageRS<OrderAo> getByPage(@RequestParam int pageIndex, @RequestParam int pageSize, @RequestParam(required = false) String customerNameItem, @RequestParam(required = false) String code, @RequestParam(required = false) String po, @RequestParam(required = false) String item, @RequestParam(required = false) String starttime, @RequestParam(required = false) String endtime) {
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
        int totleRowCount = el.findCount();
        el.setFirstRow((pageIndex - 1) * pageSize);
        el.setMaxRows(pageSize);
        List<OrderDo> list = null;
        if (StringUtils.isBlank(customerNameItem)) {
            list = el.order("create_time desc").findList();
        } else {
            list = el.order("po_num desc, create_time desc").findList();
        }
        List<OrderAo> listao = new ListUtil<OrderDo, OrderAo>().copyList(list, OrderAo.class);
        List<String> orderIds = listao.stream().map(OrderAo::getId).collect(Collectors.toList());
        List<InStorageDo> listIn = new ArrayList<>();
        List<OutStorageDo> listOut = new ArrayList<>();
        if (!orderIds.isEmpty()) {
            listIn = Ebean.createQuery(InStorageDo.class).where().in("order_id", orderIds).eq("is_delete", 0).findList();
            List<String> inStorageIds = listIn.stream().map(InStorageDo::getId).collect(Collectors.toList());
            if (!listIn.isEmpty()) {
                listOut = Ebean.createQuery(OutStorageDo.class).where().in("in_storage_id", inStorageIds).eq("is_delete", 0).findList();
            }
        }
        List<OrderAo> listaor = new ArrayList<>();
        for (OrderAo oitem : listao) {
            OrderAo aoInner = new OrderAo();
            BeanUtils.copyProperties(oitem, aoInner);
            List<String> inids = listIn.stream().filter(iin -> oitem.getId().equals(iin.getOrderId())).map(InStorageDo::getId).collect(Collectors.toList());
            int replat = listIn.stream().filter(iin -> inids.contains(iin.getId())).filter(iin -> "5".equals(iin.getIncomingType())).filter(iin -> (InUnit.ONE.getIndex() + "").equals(iin.getUnit())).mapToInt(iin -> iin.getBunchCount().intValue()).sum();
            int incomingErr = listOut.stream().filter(oin -> inids.contains(oin.getInStorageId())).filter(oin -> (OutType.INSTORAGEERR.getIndex() + "").equals(oin.getOutType())).mapToInt(oin -> oin.getBunchCount().intValue()).sum();
            int inStorageSumCountCal = listIn.stream().filter(iin -> inids.contains(iin.getId())).filter(iin -> !"5".equals(iin.getIncomingType())).filter(iin -> (InUnit.ONE.getIndex() + "").equals(iin.getUnit())).mapToInt(iin -> iin.getBunchCount().intValue()).sum();
            if (oitem.getCount() != null && oitem.getCount().compareTo(new BigDecimal(0)) != 0) {
                BigDecimal replatratio = new BigDecimal(replat).divide(oitem.getCount(), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                BigDecimal incomingErrratio = new BigDecimal(incomingErr).divide(oitem.getCount(), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                aoInner.setReplatRatio(replatratio);
                aoInner.setIncomingRatio(incomingErrratio);
            }
            aoInner.setReplatCount(replat);
            aoInner.setIncomingCount(incomingErr);
            aoInner.setPartSumCountCal(inStorageSumCountCal);
            aoInner.setCustomerName(dictController.getById(oitem.getCustomerName()).getItemName());
            aoInner.setCustomerNameId(oitem.getCustomerName());
            aoInner.setColor(dictController.getById(oitem.getColor()).getItemName());
            aoInner.setColorId(oitem.getColor());
            listaor.add(aoInner);
        }
        return new PageRS<>(pageSize, pageIndex, totleRowCount, totleRowCount / pageSize, listaor);
    }

    @PostMapping
    public RS save(@RequestBody OrderAo orderAo) {
        OrderDo doo = new OrderDo();
        BeanUtils.copyProperties(orderAo, doo);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        Date start = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date end = calendar.getTime();
        int count = Ebean.createQuery(OrderDo.class).where().ge("create_time", start).le("create_time", end).findCount();
        doo.setCode(CodeGenerUtil.getCode("OR", count));
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
        doo.setColor(orderAo.getColorId());
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
        calendar.add(calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
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
        calendar.add(calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date end = calendar.getTime();
        String startStr = sdf.format(start);
        String endStr = sdf.format(end);
        List<SqlRow> list = Ebean.createSqlQuery("select color,sum( `sum` ) as count from `order` where create_time >= :start and create_time <= :end and is_delete = 0 GROUP BY color order by count desc").setParameter("start", startStr).setParameter("end", endStr).findList();
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

    @GetMapping("/code")
    public RS getByCode(@RequestParam(required = false) String code) {
        List<Map<String, String>> list = Ebean.createQuery(OrderDo.class).where().like("code", "%" + code + "%").eq("is_delete", false).findList().stream().map(item -> {
            Map<String, String> map = new HashMap<>();
            map.put("label", item.getCode());
            map.put("value", item.getId());
            return map;
        }).collect(Collectors.toList());
        return RS.ok(list);
    }

    @GetMapping("/id")
    public RS getById(@RequestParam(required = true) String id) {
        OrderAo ao = new OrderAo();
        OrderDo doo = Ebean.createQuery(OrderDo.class).where().idEq(id).findOne();
        BeanUtils.copyProperties(doo, ao);
        return RS.ok(ao);
    }
}
