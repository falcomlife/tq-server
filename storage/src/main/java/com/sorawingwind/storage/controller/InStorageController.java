package com.sorawingwind.storage.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.cotte.estate.bean.pojo.ao.storage.*;
import com.cotte.estate.bean.pojo.bo.storage.InStorageBo;
import com.cotte.estate.bean.pojo.doo.storage.DictDo;
import com.cotte.estate.bean.pojo.doo.storage.InStorageDo;
import com.cotte.estate.bean.pojo.doo.storage.OrderDo;
import com.cotte.estate.bean.pojo.doo.storage.OutStorageDo;
import com.cotte.estate.bean.pojo.eto.InStorageEto;
import com.cotte.estate.bean.pojo.eto.OrderEto;
import com.cotte.estatecommon.PageRS;
import com.cotte.estatecommon.RS;
import com.cotte.estatecommon.enums.InUnit;
import com.cotte.estatecommon.utils.CodeGenerUtil;
import com.cotte.estatecommon.utils.ListUtil;
import com.cotte.estatecommon.utils.UUIDUtil;
import com.sorawingwind.storage.dao.InStorageDao;
import com.sorawingwind.storage.dao.OrderDao;
import io.ebean.Ebean;
import io.ebean.ExpressionList;
import io.ebean.SqlQuery;
import io.ebean.SqlRow;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.lang.model.type.UnionType;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/inStorage")
public class InStorageController {

    @Autowired
    private InStorageDao dao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private DictController dictController;

    @GetMapping
    @PreAuthorize("hasAuthority('I-1')")
    public RS getByPage(@RequestParam int pageIndex, @RequestParam int pageSize, @RequestParam(required = false) String customerNameItem, @RequestParam(required = false) String incomingTypeItem, @RequestParam(required = false) String code, @RequestParam(required = false) String starttime, @RequestParam(required = false) String endtime) {
        List<SqlRow> list = this.dao.getByPage(pageIndex, pageSize, customerNameItem, incomingTypeItem, code, starttime, endtime);
        int totleRowCount = this.dao.getCountByPage(pageIndex, pageSize, customerNameItem, incomingTypeItem, code, starttime, endtime);
        List<DictDo> customerDicts = this.dictController.getDictDoByType("customer");
        List<DictDo> colorDicts = this.dictController.getDictDoByType("color");
        List<DictDo> incomingtypeDicts = this.dictController.getDictDoByType("incomingtype");
        List<DictDo> ctDicts = this.dictController.getDictDoByType("ct");
        List<InStorageAo> listaor = list.stream().map(item -> {
            InStorageAo aoInner = new InStorageAo();
            aoInner.setId(item.getString("id"));
            aoInner.setOutStorageCode(item.getString("out_storage_code"));
            aoInner.setCode(item.getString("code"));
            aoInner.setBunchCount(item.getBigDecimal("bunch_count"));
            aoInner.setCreateTime(item.getDate("create_time"));
            aoInner.setImage(item.getString("image"));
            aoInner.setInCount(item.getString("in_count"));
            aoInner.setUnit(InUnit.getNameByIndex(Integer.parseInt(item.getString("unit"))));
            aoInner.setUnitId(item.getString("unit"));
            aoInner.setName(item.getString("name"));
            aoInner.setPoNum(item.getString("po_num"));
            aoInner.setItem(item.getString("item"));
            aoInner.setCount(item.getBigDecimal("count"));
            aoInner.setOrderId(item.getString("order_id"));
            aoInner.setOrderCode(item.getString("order_code"));
            aoInner.setIsDelete(0);
            aoInner.setCustomerName(customerDicts.stream().filter(dict -> dict.getId().equals(item.getString("customer_name"))).findFirst().get().getItemName());
            aoInner.setCustomerNameId(item.getString("customer_name"));
            if (StringUtils.isNotBlank(item.getString("color"))) {
                aoInner.setColor(colorDicts.stream().filter(dict -> dict.getId().equals(item.getString("color"))).findFirst().get().getItemName());
            }
            aoInner.setColorId(item.getString("color"));
            aoInner.setOrderColor(colorDicts.stream().filter(dict -> dict.getId().equals(item.getString("order_color"))).findFirst().get().getItemName());
            aoInner.setOrderColorId(item.getString("order_color"));
            aoInner.setIncomingType(incomingtypeDicts.stream().filter(dict -> dict.getId().equals(item.getString("incoming_type"))).findFirst().get().getItemName());
            aoInner.setIncomingTypeId(item.getString("incoming_type"));
            aoInner.setIncomingReason(item.getString("incoming_reason"));
            aoInner.setBadReason(item.getString("bad_reason"));
            aoInner.setBake(ctDicts.stream().filter(dict -> dict.getId().equals(item.getString("bake"))).findFirst().get().getItemName());
            aoInner.setBakeId(item.getString("bake"));
            return aoInner;
        }).collect(Collectors.toList());
        return RS.ok(new PageRS<>(pageSize, pageIndex, totleRowCount, totleRowCount / pageSize, listaor));
    }

    /**
     * @description: 打印时根据选中的数据重新查询
     * @author: sorawingwind
     * @date: 2023/5/4
     */
    @PostMapping("/ids")
    @PreAuthorize("hasAuthority('I-1')")
    public RS getByIds(@RequestBody List<String> ids) {

        List<DictDo> customerDicts = this.dictController.getDictDoByType("customer");
        List<DictDo> colorDicts = this.dictController.getDictDoByType("color");
        List<DictDo> incomingtypeDicts = this.dictController.getDictDoByType("incomingtype");
        List<DictDo> ctDicts = this.dictController.getDictDoByType("ct");

        StringBuffer idssb = new StringBuffer();
        for (String id : ids) {
            idssb.append("\"" + id + "\",");
        }
        String idsStr = idssb.toString().substring(0, idssb.toString().length() - 1);
        List<SqlRow> sqlRows = this.dao.getByIdstr(idsStr);
        List<InStorageAo> listaor = sqlRows.stream().map(item -> {
            InStorageAo aoInner = new InStorageAo();
            aoInner.setId(item.getString("id"));
            aoInner.setOutStorageCode(item.getString("out_storage_code"));
            aoInner.setCode(item.getString("code"));
            aoInner.setBunchCount(item.getBigDecimal("bunch_count"));
            aoInner.setCreateTime(item.getDate("create_time"));
            aoInner.setImage(item.getString("image"));
            aoInner.setInCount(item.getString("in_count"));
            aoInner.setUnit(item.getString("unit"));
            aoInner.setName(item.getString("name"));
            aoInner.setPoNum(item.getString("po_num"));
            aoInner.setItem(item.getString("item"));
            aoInner.setCount(item.getBigDecimal("count"));
            aoInner.setOrderId(item.getString("order_id"));
            aoInner.setOrderCode(item.getString("order_code"));
            aoInner.setIsDelete(0);
            aoInner.setCustomerName(customerDicts.stream().filter(dict -> dict.getId().equals(item.getString("customer_name"))).findFirst().get().getItemName());
            aoInner.setCustomerNameId(item.getString("customer_name"));
            aoInner.setColor(colorDicts.stream().filter(dict -> dict.getId().equals(item.getString("color"))).findFirst().get().getItemName());
            aoInner.setColorId(item.getString("color"));
            aoInner.setOrderColor(colorDicts.stream().filter(dict -> dict.getId().equals(item.getString("order_color"))).findFirst().get().getItemName());
            aoInner.setOrderColorId(item.getString("order_color"));
            aoInner.setIncomingType(incomingtypeDicts.stream().filter(dict -> dict.getId().equals(item.getString("incoming_type"))).findFirst().get().getItemName());
            aoInner.setIncomingTypeId(item.getString("incoming_type"));
            aoInner.setIncomingReason(item.getString("incoming_reason"));
            aoInner.setBadReason(item.getString("bad_reason"));
            aoInner.setBake(ctDicts.stream().filter(dict -> dict.getId().equals(item.getString("bake"))).findFirst().get().getItemName());
            aoInner.setBakeId(item.getString("bake"));
            return aoInner;
        }).collect(Collectors.toList());
        return RS.ok(listaor);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('I-1')")
    public RS save(@RequestBody InStorageAo inStorageAo) {
        InStorageDo doo = new InStorageDo();
        BeanUtils.copyProperties(inStorageAo, doo);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        Date start = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date end = calendar.getTime();
        int count = this.dao.getCodeCount(start, end);
        doo.setCode(CodeGenerUtil.getCode("I", count));
        doo.setId(UUIDUtil.simpleUUid());
        doo.setCreateTime(new Date());
        doo.setIsDelete(0);
        this.dao.save(doo);
        return RS.ok();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('I-1')")
    public RS update(@RequestBody InStorageAo inStorageAo) {
        InStorageDo doo = new InStorageDo();
        BeanUtils.copyProperties(inStorageAo, doo);
        doo.setColor(inStorageAo.getColorId());
        doo.setBake(inStorageAo.getBakeId());
        doo.setIncomingType(inStorageAo.getIncomingTypeId());
        doo.setUnit(inStorageAo.getUnitId());
        doo.setModifiedTime(new Date());
        this.dao.update(doo);
        return RS.ok();
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('I-1')")
    public RS delete(@RequestBody List<String> ids) {
        List<InStorageDo> list = this.dao.getByIds(ids);
        for (InStorageDo doo : list) {
            doo.setIsDelete(1);
        }
        this.dao.updateAll(list);
        return RS.ok();
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasAuthority('I-1')")
    public RS getMouthStatistics(@RequestParam(required = false) String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = sdf.parse(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.add(calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date end = calendar.getTime();
        return RS.ok(this.dao.getMouthStatistics(start, end));
    }

    @GetMapping("/statistics/reratio")
    @PreAuthorize("hasAuthority('I-1')")
    public RS getMouthStatisticsReratio(@RequestParam(required = false) String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = sdf.parse(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.add(calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date end = calendar.getTime();
        List<InStorageDo> recountList = this.dao.getMouthStatisticsReratio(start, end);
        BigDecimal sumcount = new BigDecimal(0);
        BigDecimal recount = new BigDecimal(0);
        for (InStorageDo doo : recountList) {
            if ("5".equals(doo.getIncomingType())) {
                if (doo.getBunchCount() != null) {
                    recount = recount.add(doo.getBunchCount());
                }
            }
            if (doo.getBunchCount() != null) {
                sumcount = sumcount.add(doo.getBunchCount());
            }
        }
        BigDecimal ratio = new BigDecimal(0);
        if (sumcount.compareTo(BigDecimal.ZERO) != 0) {
            ratio = (recount.divide(sumcount, 4, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(100)).setScale(2);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("recount", recount);
        map.put("ratio", ratio);
        return RS.ok(map);
    }

    @GetMapping("/code")
    @PreAuthorize("hasAuthority('I-1')")
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
    @PreAuthorize("hasAuthority('I-1')")
    public RS getById(@RequestParam(required = true) String id) {
        InStorageAo iao = new InStorageAo();
        InStorageDo idoo = this.dao.getById(id);
        BeanUtils.copyProperties(idoo, iao);
        OrderDo doo = this.orderDao.getById(idoo.getOrderId());
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
    @PreAuthorize("hasAuthority('I-1')")
    public RS getByOrderId(@RequestParam String orderId) {
        return RS.ok(this.dao.getByOrderId(orderId).stream().map(item -> {
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
    @PreAuthorize("hasAuthority('I-1')")
    public RS getByOutStorageId(@RequestParam String outStorageId) {
        return RS.ok(this.dao.getByOutStorageId(outStorageId).stream().map(item -> {
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

    @PostMapping("/excel")
    @PreAuthorize("hasAuthority('I-1')")
    public void download(HttpServletResponse response, @RequestBody InStorageExcelAo inStorageExcelAo) throws Exception {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("入库明细", "UTF-8");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx;" + "filename*=utf-8''" + fileName + ".xlsx");
        OutputStream outputStream = response.getOutputStream();
        //FileOutputStream outputStream = new FileOutputStream("/home/sorawingwind/桌面/xx.xlsx");
        //String customerNameItem, String incomingTypeItem, String code, String starttime,  String endtime
        List<DictDo> customerDicts = this.dictController.getDictDoByType("customer");
        List<DictDo> colorDicts = this.dictController.getDictDoByType("color");
        //获取数据
        List<InStorageEto> listeto = this.dao.getExcels(inStorageExcelAo.getCustomerNameItem(), inStorageExcelAo.getIncomingType(), inStorageExcelAo.getCode(), inStorageExcelAo.getStarttime(), inStorageExcelAo.getEndtime());
        listeto.stream().forEach(item ->
            {
                item.setCustomerName(customerDicts.stream().filter(c -> c.getId().equals(item.getCustomerName())).findFirst().get().getItemName());
                item.setOrderColor(colorDicts.stream().filter(c -> c.getId().equals(item.getOrderColor())).findFirst().get().getItemName());
            }
        );
        // 获取模板路径
        InputStream resourceAsStream = this.getClass().getResourceAsStream("/excel/inStorage.xlsx");
        // 创建输出的excel对象
        final ExcelWriter write = EasyExcel.write(outputStream).withTemplate(resourceAsStream).build();
        // 创建第一个sheel页
        final WriteSheet sheet1 = EasyExcel.writerSheet(0, "入库明细").build();
        write.fill(listeto, sheet1);
        // 关闭流
        write.finish();
    }
}
