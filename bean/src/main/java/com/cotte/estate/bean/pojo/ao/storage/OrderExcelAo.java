package com.cotte.estate.bean.pojo.ao.storage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

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
public class OrderExcelAo {

    private String customerNameItem;
    private String code;
    private String po;
    private String item;
    private String starttime;
    private String endtime;

}
