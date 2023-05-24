package com.sorawingwind.storage.controller;

import com.cotte.estate.bean.pojo.ao.storage.OutStorageAo;
import com.cotte.estate.bean.pojo.doo.storage.OutStorageDo;
import com.cotte.estatecommon.PageRS;
import com.cotte.estatecommon.RS;
import com.cotte.estatecommon.utils.CodeGenerUtil;
import com.cotte.estatecommon.utils.UUIDUtil;
import com.cotte.estatecommon.enums.OutType;
import com.sorawingwind.storage.dao.OutStorageDao;
import io.ebean.Ebean;
import io.ebean.SqlQuery;
import io.ebean.SqlRow;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private OutStorageDao dao;
    @Autowired
    private DictController dictController;
    @Value("${project.file.path}")
    private String path;

    @GetMapping
    @PreAuthorize("hasAuthority('I-4')")
    public RS getByPage(@RequestParam int pageIndex, @RequestParam int pageSize, @RequestParam(required = false) String customerNameItem, @RequestParam(required = false) String code, @RequestParam(required = false) String starttime, @RequestParam(required = false) String endtime) {
        List<SqlRow> list = this.dao.getByPage(pageIndex, pageSize,customerNameItem,code, starttime, endtime);
        Integer totleRowCount = this.dao.getCountByPage(pageIndex, pageSize,customerNameItem,code, starttime, endtime);
        List<OutStorageAo> listaor = list.stream().map(item -> {
            OutStorageAo aoInner = new OutStorageAo();
            aoInner.setId(item.getString("id"));
            aoInner.setInStorageId(item.getString("in_storage_id"));
            aoInner.setInStorageCode(item.getString("in_storage_code"));
            aoInner.setCode(item.getString("code"));
            aoInner.setBunchCount(item.getBigDecimal("bunch_count"));
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
            aoInner.setOutType(OutType.getNameByIndex(Integer.parseInt(item.getString("out_type"))));
            aoInner.setInCount(item.getString("in_count"));
            return aoInner;
        }).collect(Collectors.toList());
        return RS.ok(new PageRS<>(pageSize, pageIndex, totleRowCount, totleRowCount / pageSize, listaor));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('I-4')")
    public RS save(@RequestBody OutStorageAo outStorageAo) {
        OutStorageDo doo = new OutStorageDo();
        BeanUtils.copyProperties(outStorageAo, doo);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        Date start = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date end = calendar.getTime();
        int count = Ebean.createQuery(OutStorageDo.class).where().ge("create_time", start).le("create_time", end).findCount();
        doo.setCode(CodeGenerUtil.getCode("O", count));
        doo.setId(UUIDUtil.simpleUUid());
        doo.setCreateTime(new Date());
        doo.setIsDelete(0);
        this.dao.save(doo);
        return RS.ok();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('I-4')")
    public RS update(@RequestBody OutStorageAo outStorageAo) {
        OutStorageDo doo = new OutStorageDo();
        BeanUtils.copyProperties(outStorageAo, doo);
        doo.setModifiedTime(new Date());
        this.dao.update(doo);
        return RS.ok();
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('I-4')")
    public RS delete(@RequestBody List<String> ids) {
        List<OutStorageDo> list = Ebean.createQuery(OutStorageDo.class).where().idIn(ids).findList();
        for (OutStorageDo doo : list) {
            doo.setIsDelete(1);
        }
        this.dao.updataAll(list);
        return RS.ok();
    }

    @PostMapping("/image")
    @PreAuthorize("hasAuthority('I-4')")
    public RS upload(MultipartFile file) throws IOException {
        String originFilename = file.getOriginalFilename();
        String suffixName = originFilename.substring(originFilename.lastIndexOf('.'));
        String filename = UUIDUtil.simpleUUid() + suffixName;
        file.transferTo(new File(this.path + filename));
        return RS.ok(filename);
    }

    @GetMapping("/code")
    @PreAuthorize("hasAuthority('I-4')")
    public RS getByCode(@RequestParam(required = false) String code,@RequestParam(required = false) String orderId,@RequestParam(required = false) String item) {
        List<Map<String, String>> listaor = this.dao.getByCode(code,orderId,item).stream().map(iitem -> {
            Map<String, String> map = new HashMap<>();
            map.put("label", iitem.getString("code"));
            map.put("value", iitem.getString("id"));
            return map;
        }).collect(Collectors.toList());
        return RS.ok(listaor);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('I-4')")
    public RS getListByInStorage(@RequestParam String inStorageId) {
        return RS.ok(this.dao.getByInStorageId(inStorageId).stream().map(item -> {
            OutStorageAo aoInner = new OutStorageAo();
            BeanUtils.copyProperties(item, aoInner);
            aoInner.setOutType(OutType.getNameByIndex(Integer.parseInt(item.getOutType())));
            return aoInner;
        }).collect(Collectors.toList()));
    }

    @GetMapping("/one")
    @PreAuthorize("hasAuthority('I-4')")
    public RS getOneByInStorage(@RequestParam String outStorageId) {
        return RS.ok(this.dao.getById(outStorageId).stream().map(item -> {
            OutStorageAo aoInner = new OutStorageAo();
            BeanUtils.copyProperties(item, aoInner);
            aoInner.setOutType(OutType.getNameByIndex(Integer.parseInt(item.getOutType())));
            return aoInner;
        }).collect(Collectors.toList()));
    }
}
