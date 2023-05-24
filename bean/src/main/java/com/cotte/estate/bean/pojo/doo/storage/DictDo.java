package com.cotte.estate.bean.pojo.doo.storage;

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
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name="s_dict")
@Data
public class DictDo {

    @Id
    @GeneratedValue(generator="uuidGenerator")
    private String id;
    @Column(name = "type")
    private String type;
    @Column(name = "type_name")
    private String typeName;
    @Column(name = "item")
    private String item;
    @Column(name = "item_name")
    private String itemName;

}
