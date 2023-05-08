package com.sorawingwind.storage.controller;

import com.cotte.estate.bean.pojo.ao.storage.InStorageAo;
import com.cotte.estate.bean.pojo.ao.storage.OutStorageAo;
import com.cotte.estate.bean.pojo.doo.storage.InStorageDo;
import com.cotte.estate.bean.pojo.doo.storage.OrderDo;
import com.cotte.estate.bean.pojo.doo.storage.OutStorageDo;
import com.cotte.estatecommon.PageRS;
import com.cotte.estatecommon.RS;
import com.cotte.estatecommon.utils.CodeGenerUtil;
import com.cotte.estatecommon.utils.ListUtil;
import com.cotte.estatecommon.utils.UUIDUtil;
import com.sorawingwind.storage.OutType;
import io.ebean.Ebean;
import io.ebean.ExpressionList;
import io.ebean.SqlQuery;
import io.ebean.SqlRow;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/outStorage")
public class OutStorageController {

    @Autowired
    private DictController dictController;

    //    @GetMapping
//    public PageRS<OutStorageAo> getByPage(@RequestParam int pageIndex, @RequestParam int pageSize, @RequestParam(required = false) String customerNameItem, @RequestParam(required = false) String starttime, @RequestParam(required = false) String endtime) {
//        ExpressionList<OutStorageDo> el = Ebean.createQuery(OutStorageDo.class).where();
//        if (StringUtils.isNotBlank(customerNameItem)) {
//            el.eq("customer_name", customerNameItem);
//        }
//        if (StringUtils.isNotBlank(starttime)) {
//            el.ge("create_time", starttime);
//        }
//        if (StringUtils.isNotBlank(endtime)) {
//            el.le("create_time", endtime);
//        }
//        el.eq("is_delete",false);
//        int totleRowCount = el.findCount();
//        el.setFirstRow((pageIndex - 1) * pageSize);
//        el.setMaxRows(pageSize);
//        List<OutStorageDo> list = el.order("create_time desc").findList();
//        List<OutStorageAo> listao = new ListUtil<OutStorageDo, OutStorageAo>().copyList(list, OutStorageAo.class);
//        List<OutStorageAo> listaor = listao.stream().map(item -> {
//            OutStorageAo aoInner = new OutStorageAo();
//            BeanUtils.copyProperties(item, aoInner);
//            aoInner.setCustomerName(dictController.getById(item.getCustomerName()).getItemName());
//            aoInner.setCustomerNameId(item.getCustomerName());
//            aoInner.setColor(dictController.getById(item.getColor()).getItemName());
//            aoInner.setColorId(item.getColor());
//            aoInner.setBake(dictController.getById(item.getBake()).getItemName());
//            aoInner.setBakeId(item.getBake());
//            return aoInner;
//        }).collect(Collectors.toList());
//        return new PageRS<>(pageSize, pageIndex, totleRowCount, totleRowCount / pageSize, listaor);
//    }
    @GetMapping
    public PageRS<OutStorageAo> getByPage(@RequestParam int pageIndex, @RequestParam int pageSize, @RequestParam(required = false) String customerNameItem, @RequestParam(required = false) String starttime, @RequestParam(required = false) String endtime) {
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
        sb.append(" i.bake as bake ");
        sb.append(" from out_storage ot ");
        sb.append(" left join in_storage i on ot.in_storage_id = i.id");
        sb.append(" left join `order` o on o.id = i.order_id");
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

        List<OutStorageAo> listaor = list.stream().map(item -> {
            OutStorageAo aoInner = new OutStorageAo();
            aoInner.setId(item.getString("id"));
            aoInner.setInStorageId(item.getString("in_storage_id"));
            aoInner.setInStorageCode(item.getString("in_storage_code"));
            aoInner.setCode(item.getString("code"));
            aoInner.setBunchCount(item.getInteger("bunch_count"));
            aoInner.setCreateTime(item.getDate("create_time"));
            aoInner.setImage(item.getString("image"));
            aoInner.setOutCount(item.getString("out_count"));
            aoInner.setName(item.getString("name"));
            aoInner.setPoNum(item.getString("po_num"));
            aoInner.setItem(item.getString("item"));
            aoInner.setCount(item.getString("count"));
            aoInner.setIsDelete(0);
            aoInner.setCustomerName(dictController.getById(item.getString("customer_name")).getItemName());
            aoInner.setCustomerNameId(item.getString("customer_name"));
            aoInner.setColor(dictController.getById(item.getString("color")).getItemName());
            aoInner.setColorId(item.getString("color"));
            aoInner.setBake(dictController.getById(item.getString("bake")).getItemName());
            aoInner.setBakeId(item.getString("bake"));
            aoInner.setOutTypeId(item.getString("out_type"));
            if("1".equals(item.getString("out_type"))){
                aoInner.setOutType(OutType.NORMAL.getName());
            }else if("2".equals(item.getString("out_type"))){
                aoInner.setOutType(OutType.INSTORAGEERR.getName());
            }else if("3".equals(item.getString("out_type"))){
                aoInner.setOutType(OutType.WORKLOSS.getName());
            }else if("4".equals(item.getString("out_type"))){
                aoInner.setOutType(OutType.OTHER.getName());
            }

            return aoInner;
        }).collect(Collectors.toList());
        return new PageRS<>(pageSize, pageIndex, totleRowCount, totleRowCount / pageSize, listaor);
    }

    @PostMapping
    public RS save(@RequestBody OutStorageAo outStorageAo) {
        OutStorageDo doo = new OutStorageDo();
        BeanUtils.copyProperties(outStorageAo, doo);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),0,0,0);
        Date start = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH,1);
        Date end = calendar.getTime();
        int count = Ebean.createQuery(OutStorageDo.class).where().ge("create_time",start).le("create_time",end).findCount();
        doo.setCode(CodeGenerUtil.getCode("O",count));
        doo.setId(UUIDUtil.simpleUUid());
        doo.setCreateTime(new Date());
        doo.setIsDelete(0);
        Ebean.save(doo);
        return RS.ok();
    }

    @PutMapping
    public RS update(@RequestBody OutStorageAo outStorageAo) {
        OutStorageDo doo = new OutStorageDo();
        BeanUtils.copyProperties(outStorageAo, doo);
        doo.setModifiedTime(new Date());
        Ebean.update(doo);
        return RS.ok();
    }

    @DeleteMapping
    public RS delete(@RequestBody List<String> ids) {
        List<OutStorageDo> list = Ebean.createQuery(OutStorageDo.class).where().idIn(ids).findList();
        for (OutStorageDo doo : list) {
            doo.setIsDelete(1);
        }
        Ebean.updateAll(list);
        return RS.ok();
    }

    @PostMapping("/image")
    public RS upload(MultipartFile file) throws IOException {
        String originFilename = file.getOriginalFilename();
        String suffixName = originFilename.substring(originFilename.lastIndexOf('.'));
        String filename = UUIDUtil.simpleUUid() + suffixName;
        file.transferTo(new File("/home/sorawingwind/workhome/test/upload/" + filename));
        return RS.ok(filename);
    }

    @GetMapping("/code")
    public RS getByCode(@RequestParam(required = false) String code) {
        List<Map<String,String>> list = Ebean.createQuery(OutStorageDo.class).where().like("code", "%" + code + "%").eq("is_delete", false).findList().stream().map(item -> {
            Map<String, String> map = new HashMap<>();
            map.put("label",item.getCode());
            map.put("value",item.getId());
            return map;
        }).collect(Collectors.toList());
        return RS.ok(list);
    }

    @GetMapping("/list")
    public RS getListByInStorage(@RequestParam String inStorageId){
        return RS.ok(Ebean.createQuery(OutStorageDo.class).where().eq("is_delete",0).eq("in_storage_id",inStorageId).findList().stream().map(item ->{
            OutStorageAo aoInner = new OutStorageAo();
            BeanUtils.copyProperties(item,aoInner);
            if("1".equals(item.getOutType())){
                aoInner.setOutType(OutType.NORMAL.getName());
            }else if("2".equals(item.getOutType())){
                aoInner.setOutType(OutType.INSTORAGEERR.getName());
            }else if("3".equals(item.getOutType())){
                aoInner.setOutType(OutType.WORKLOSS.getName());
            }else if("4".equals(item.getOutType())){
                aoInner.setOutType(OutType.OTHER.getName());
            }
            return aoInner;
        }).collect(Collectors.toList()));
    }
    @GetMapping("/one")
    public RS getOneByInStorage(@RequestParam String outStorageId){
        return RS.ok(Ebean.createQuery(OutStorageDo.class).where().eq("is_delete",0).eq("id",outStorageId).findList().stream().map(item ->{
            OutStorageAo aoInner = new OutStorageAo();
            BeanUtils.copyProperties(item,aoInner);
            if("1".equals(item.getOutType())){
                aoInner.setOutType(OutType.NORMAL.getName());
            }else if("2".equals(item.getOutType())){
                aoInner.setOutType(OutType.INSTORAGEERR.getName());
            }else if("3".equals(item.getOutType())){
                aoInner.setOutType(OutType.WORKLOSS.getName());
            }else if("4".equals(item.getOutType())){
                aoInner.setOutType(OutType.OTHER.getName());
            }
            return aoInner;
        }).collect(Collectors.toList()));
    }
}
