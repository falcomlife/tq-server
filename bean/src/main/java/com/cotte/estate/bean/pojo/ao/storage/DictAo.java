package com.cotte.estate.bean.pojo.ao.storage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
public class DictAo {

    @Id
    @GeneratedValue(generator="uuidGenerator")
    private String id;
    private String type;
    private String typeName;
    private String item;
    private String itemName;

}
