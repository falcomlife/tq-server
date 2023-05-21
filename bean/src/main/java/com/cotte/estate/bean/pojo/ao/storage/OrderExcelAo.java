package com.cotte.estate.bean.pojo.ao.storage;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
*@description: 字典
*@author: sorawingwind
*@date: 2023/4/29
*/
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ApiModel("字典管理")
public class OrderExcelAo {

    private String customerNameItem;
    private String code;
    private String po;
    private String item;
    private String starttime;
    private String endtime;

}
