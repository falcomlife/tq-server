package com.sorawingwind.storage.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.cotte.estate.bean.pojo.ao.storage.OrderAo;
import com.cotte.estate.bean.pojo.ao.storage.OrderExcelAo;
import com.cotte.estate.bean.pojo.doo.storage.DictDo;
import com.cotte.estate.bean.pojo.doo.storage.InStorageDo;
import com.cotte.estate.bean.pojo.doo.storage.OrderDo;
import com.cotte.estate.bean.pojo.doo.storage.OutStorageDo;
import com.cotte.estate.bean.pojo.eto.MouthStatisticsCustomerEto;
import com.cotte.estate.bean.pojo.eto.OrderEto;
import com.cotte.estatecommon.PageRS;
import com.cotte.estatecommon.RS;
import com.cotte.estatecommon.enums.InUnit;
import com.cotte.estatecommon.utils.CodeGenerUtil;
import com.cotte.estatecommon.utils.ListUtil;
import com.cotte.estatecommon.utils.UUIDUtil;
import com.cotte.estatecommon.enums.OutType;
import com.sorawingwind.storage.dao.OrderDao;
import io.ebean.Ebean;
import io.ebean.SqlRow;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderDao dao;
    @Autowired
    private DictController dictController;

    @GetMapping
    @PreAuthorize("hasAuthority('I-3')")
    public RS getByPage(@RequestParam int pageIndex, @RequestParam int pageSize, @RequestParam(required = false) String customerNameItem, @RequestParam(required = false) String code, @RequestParam(required = false) String po, @RequestParam(required = false) String item, @RequestParam(required = false) String starttime, @RequestParam(required = false) String endtime) {
        List<OrderDo> list = this.dao.getByPage(pageIndex, pageSize, customerNameItem, code, po, item, starttime, endtime);
        int totleRowCount = this.dao.getCountByPage(pageIndex, pageSize, customerNameItem, code, po, item, starttime, endtime);

        List<DictDo> customerDicts = this.dictController.getDictDoByType("customer");
        List<DictDo> colorDicts = this.dictController.getDictDoByType("color");

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
            // 入库反镀件组件数量，只统计单位是个的数量
            int replat = listIn.stream().filter(iin -> inids.contains(iin.getId())).filter(iin -> "5".equals(iin.getIncomingType())).filter(iin -> (InUnit.ONE.getIndex() + "").equals(iin.getUnit())).mapToInt(iin -> iin.getBunchCount().intValue()).sum();
            // 入库来料异常组件数
            int incomingErr = listOut.stream().filter(oin -> inids.contains(oin.getInStorageId())).filter(oin -> (OutType.INSTORAGEERR.getIndex() + "").equals(oin.getOutType())).mapToInt(oin -> oin.getBunchCount().intValue()).sum();
            // 入库非返镀件组件数量，只统计单位是个的数量（已入库组件总数）
            int inStorageSumCountCal = listIn.stream().filter(iin -> inids.contains(iin.getId())).filter(iin -> !"5".equals(iin.getIncomingType())).filter(iin -> (InUnit.ONE.getIndex() + "").equals(iin.getUnit())).mapToInt(iin -> iin.getBunchCount().intValue()).sum();
            // 入库不良品组件数
            //int incomingPoor = listOut.stream().filter(oin -> inids.contains(oin.getInStorageId())).filter(oin -> (OutType.POOR.getIndex() + "").equals(oin.getOutType())).mapToInt(oin -> oin.getBunchCount().intValue()).sum();
            int incomingPoor = listIn.stream().filter(iin -> inids.contains(iin.getId())).filter(iin -> "30bc0ec552cb4a59a23c680362219ecf".equals(iin.getIncomingType())).filter(iin -> (InUnit.ONE.getIndex() + "").equals(iin.getUnit())).mapToInt(iin -> iin.getBunchCount().intValue()).sum();
            // 出库良品组件数
            int outStorageSumGood = listOut.stream().filter(oin -> inids.contains(oin.getInStorageId())).filter(oin->(OutType.GOOD.getIndex()+"").equals(oin.getOutType())).mapToInt(oin->oin.getBunchCount().intValue()).sum();
            // 出库良品组件数 - 入库不良品组件数 = 已出库良品组件总数
            outStorageSumGood = outStorageSumGood - incomingPoor;
            // 已入库组件总数 - 入库不良品组件数 = 已入库组件总数
            inStorageSumCountCal = inStorageSumCountCal - incomingPoor;
            if (oitem.getCount() != null && oitem.getCount().compareTo(new BigDecimal(0)) != 0) {
                BigDecimal replatratio = new BigDecimal(replat).divide(oitem.getCount(), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                BigDecimal incomingErrratio = new BigDecimal(incomingErr).divide(oitem.getCount(), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                aoInner.setReplatRatio(replatratio);
                aoInner.setIncomingRatio(incomingErrratio);
            }
            if(aoInner.getPartSumCount()!=null){
                aoInner.setIncomingBigger(aoInner.getPartSumCount().compareTo(new BigDecimal(inStorageSumCountCal)) == -1);
            }
            aoInner.setReplatCount(replat);
            aoInner.setIncomingCount(incomingErr);
            aoInner.setOutStroageGoodsSumCount(outStorageSumGood);
            aoInner.setPartSumCountCal(inStorageSumCountCal);
            if(StringUtils.isNotBlank(oitem.getCustomerName())) {
                aoInner.setCustomerName(customerDicts.stream().filter(dict -> dict.getId().equals(oitem.getCustomerName())).findFirst().get().getItemName());
            }
            aoInner.setCustomerNameId(oitem.getCustomerName());
            aoInner.setColor(colorDicts.stream().filter(dict -> dict.getId().equals(oitem.getColor())).findFirst().get().getItemName());
            aoInner.setColorId(oitem.getColor());
            listaor.add(aoInner);
        }
        return RS.ok(new PageRS<>(pageSize, pageIndex, totleRowCount, totleRowCount / pageSize, listaor));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('I-3')")
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
        this.dao.save(doo);
        return RS.ok();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('I-3')")
    public RS update(@RequestBody OrderAo orderAo) {
        OrderDo doo = new OrderDo();
        BeanUtils.copyProperties(orderAo, doo);
        doo.setCustomerName(orderAo.getCustomerNameId());
        doo.setColor(orderAo.getColorId());
        doo.setModifiedTime(new Date());
        this.dao.update(doo);
        return RS.ok();
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('I-3')")
    public RS delete(@RequestBody List<String> ids) {
        List<OrderDo> list = Ebean.createQuery(OrderDo.class).where().idIn(ids).findList();
        for (OrderDo doo : list) {
            doo.setIsDelete(1);
        }
        this.dao.updateAll(list);
        return RS.ok();
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasAuthority('I-3')")
    public RS getMouthStatistics(@RequestParam(required = false) String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = sdf.parse(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.add(calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date end = calendar.getTime();
        return RS.ok(this.dao.getCountBetweenTimes(start, end));
//        }
    }

    @GetMapping("/statistics/color")
    @PreAuthorize("hasAuthority('I-3')")
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
        List<SqlRow> list = this.dao.getCountBetweenTimesWithColor(startStr, endStr);
        if (!list.isEmpty()) {
            Double maxsum = list.stream().mapToDouble(sqlrow -> sqlrow.getDouble("count")).sum();
            return RS.ok(list.stream().map(item -> {
                String colorid = item.getString("color");
                String count = item.getString("count");
                BigDecimal ratio = BigDecimal.ZERO;
                if (maxsum != 0.0) {
                    ratio = item.getBigDecimal("count").divide(new BigDecimal(maxsum), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                }
                String color = dictController.getById(colorid).getItemName();
                Map<String, String> map = new HashMap<>();
                map.put("color", color);
                map.put("count", count);
                map.put("ratio", ratio.toString());
                return map;
            }).collect(Collectors.toList()));
        } else {
            return RS.warn("未查询到订单数据。");
        }
    }
    @GetMapping("/statistics/customer")
    @PreAuthorize("hasAuthority('I-3')")
    public RS getMouthStatisticsCustomer(@RequestParam(required = false) String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = sdf.parse(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.add(calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date end = calendar.getTime();
        String startStr = sdf.format(start);
        String endStr = sdf.format(end);
        List<SqlRow> list = this.dao.getCountBetweenTimesWithCustomer(startStr, endStr);
        if (!list.isEmpty()) {
            List<String> customerNames = list.stream().map(item -> dictController.getById(item.getString("customer_name")).getItemName()).collect(Collectors.toList());
            List<BigDecimal> counts = list.stream().map(item -> item.getBigDecimal("count")).collect(Collectors.toList());
            Map<String,List> map = new HashMap<>();
            map.put("name",customerNames);
            map.put("count",counts);
            return RS.ok(map);
        } else {
            return RS.warn("未查询到订单数据。");
        }
    }
    @GetMapping("/statistics/customer/excel")
    @PreAuthorize("hasAuthority('I-3')")
    public void getMouthStatisticsCustomerExcel(HttpServletResponse response, @RequestParam(required = false) String time) throws ParseException, IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = sdf.parse(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.add(calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date end = calendar.getTime();
        String startStr = sdf.format(start);
        String endStr = sdf.format(end);
        List<SqlRow> list = this.dao.getCountBetweenTimesWithCustomer(startStr, endStr);
        if (!list.isEmpty()) {
            List<DictDo> customerDicts = this.dictController.getDictDoByType("customer");
            List<MouthStatisticsCustomerEto> listeto = list.stream().map(item -> {
                MouthStatisticsCustomerEto mouthStatisticsCustomerEto = new MouthStatisticsCustomerEto();
                mouthStatisticsCustomerEto.setName(customerDicts.stream().filter(dict -> dict.getId().equals(item.getString("customer_name"))).findFirst().get().getItemName());
                mouthStatisticsCustomerEto.setCount(item.getBigDecimal("count"));
                return mouthStatisticsCustomerEto;
            }).collect(Collectors.toList());
            OutputStream outputStream = response.getOutputStream();
            // 获取模板路径
            InputStream resourceAsStream = this.getClass().getResourceAsStream("/excel/mouthStatisticsCustomer.xlsx");
            // 创建输出的excel对象
            final ExcelWriter write = EasyExcel.write(outputStream).withTemplate(resourceAsStream).build();
            // 创建第一个sheel页
            final WriteSheet sheet1 = EasyExcel.writerSheet(0, "月客户金额明细").build();
            write.fill(listeto, sheet1);
            // 关闭流
            write.finish();
        }
    }

    @GetMapping("/code")
    @PreAuthorize("hasAuthority('I-3')")
    public RS getByCode(@RequestParam(required = false) String code) {
        List<Map<String, String>> list = this.dao.getByCode(code).stream().map(item -> {
            Map<String, String> map = new HashMap<>();
            map.put("label", item.getCode());
            map.put("value", item.getId());
            return map;
        }).collect(Collectors.toList());
        return RS.ok(list);
    }

    @GetMapping("/id")
    @PreAuthorize("hasAuthority('I-3')")
    public RS getById(@RequestParam(required = true) String id) {
        OrderAo ao = new OrderAo();
        OrderDo doo = this.dao.getById(id);
        BeanUtils.copyProperties(doo, ao);
        return RS.ok(ao);
    }

    @PostMapping("/excel")
    @PreAuthorize("hasAuthority('I-3')")
    public void download(HttpServletResponse response, @RequestBody OrderExcelAo orderAo) throws Exception {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("订单明细", "UTF-8");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx;" + "filename*=utf-8''" + fileName + ".xlsx");
        OutputStream outputStream = response.getOutputStream();
        //FileOutputStream outputStream = new FileOutputStream("/home/sorawingwind/桌面/xx.xlsx");

        //获取数据
        List<OrderDo> listdo = this.dao.getExcels(orderAo.getCustomerNameItem(), orderAo.getCode(), orderAo.getPo(), orderAo.getItem(), orderAo.getStarttime(), orderAo.getEndtime()).stream().map(iitem -> {
            iitem.setColor(dictController.getById(iitem.getColor()).getItemName());
            return iitem;
        }).collect(Collectors.toList());
        List<OrderEto> listeto = new ListUtil<OrderDo, OrderEto>().copyList(listdo, OrderEto.class);
        // 获取模板路径
        InputStream resourceAsStream = this.getClass().getResourceAsStream("/excel/order.xlsx");
        // 创建输出的excel对象
        final ExcelWriter write = EasyExcel.write(outputStream).withTemplate(resourceAsStream).build();
        // 创建第一个sheel页
        final WriteSheet sheet1 = EasyExcel.writerSheet(0, "订单明细").build();
        write.fill(listeto, sheet1);
        // 关闭流
        write.finish();
    }
}
