package com.sorawingwind.storage.controller;

import com.cotte.estate.bean.pojo.ao.storage.OutStorageAo;
import com.cotte.estate.bean.pojo.doo.storage.OutStorageDo;
import com.cotte.estatecommon.PageRS;
import com.cotte.estatecommon.RS;
import com.cotte.estatecommon.utils.ListUtil;
import com.cotte.estatecommon.utils.UUIDUtil;
import io.ebean.Ebean;
import io.ebean.ExpressionList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/outStorage")
public class OutStorageController {

    @Autowired
    private DictController dictController;

    @GetMapping
    public PageRS<OutStorageAo> getByPage(@RequestParam int pageIndex, @RequestParam int pageSize, @RequestParam(required = false) String customerNameItem, @RequestParam(required = false) String starttime, @RequestParam(required = false) String endtime) {
        ExpressionList<OutStorageDo> el = Ebean.createQuery(OutStorageDo.class).where();
        if (StringUtils.isNotBlank(customerNameItem)) {
            el.eq("customer_name", customerNameItem);
        }
        if (StringUtils.isNotBlank(starttime)) {
            el.ge("create_time", starttime);
        }
        if (StringUtils.isNotBlank(endtime)) {
            el.le("create_time", endtime);
        }
        el.eq("is_delete",false);
        int totleRowCount = el.findCount();
        el.setFirstRow((pageIndex - 1) * pageSize);
        el.setMaxRows(pageSize);
        List<OutStorageDo> list = el.order("create_time desc").findList();
        List<OutStorageAo> listao = new ListUtil<OutStorageDo, OutStorageAo>().copyList(list, OutStorageAo.class);
        List<OutStorageAo> listaor = listao.stream().map(item -> {
            OutStorageAo aoInner = new OutStorageAo();
            BeanUtils.copyProperties(item, aoInner);
            aoInner.setCustomerName(dictController.getById(item.getCustomerName()).getItemName());
            aoInner.setCustomerNameId(item.getCustomerName());
            aoInner.setColor(dictController.getById(item.getColor()).getItemName());
            aoInner.setColorId(item.getColor());
            aoInner.setBake(dictController.getById(item.getBake()).getItemName());
            aoInner.setBakeId(item.getBake());
            return aoInner;
        }).collect(Collectors.toList());
        return new PageRS<>(pageSize, pageIndex, totleRowCount, totleRowCount / pageSize, listaor);
    }

    @PostMapping
    public RS save(@RequestBody OutStorageAo outStorageAo) {
        OutStorageDo doo = new OutStorageDo();
        BeanUtils.copyProperties(outStorageAo, doo);
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
        doo.setBake(outStorageAo.getBakeId());
        doo.setColor(outStorageAo.getColorId());
        doo.setCustomerName(outStorageAo.getCustomerNameId());
        doo.setModifiedTime(new Date());
        Ebean.update(doo);
        return RS.ok();
    }

    @DeleteMapping
    public RS delete(@RequestBody List<String> ids) {
        List<OutStorageDo> list = Ebean.createQuery(OutStorageDo.class).where().idIn(ids).findList();
        for (OutStorageDo doo:list) {
            doo.setIsDelete(1);
        }
        Ebean.updateAll(list);
        return RS.ok();
    }

    @PostMapping("/image")
    public RS upload(MultipartFile file) throws IOException {
        String originFilename = file.getOriginalFilename();
        String suffixName = originFilename.substring(originFilename.lastIndexOf('.'));
        String filename = UUIDUtil.simpleUUid()+suffixName;
        file.transferTo(new File("/home/sorawingwind/workhome/test/upload/"+filename));
        return RS.ok(filename);
    }
}
