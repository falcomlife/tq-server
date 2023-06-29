package com.cotte.estate.bean.pojo.bo.storage;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class InStorageBo {

    private String id;
    private String customerName;
    private String orderId;
    private String outStorageId;
    private String code;
    private String image;
    private String name;
    private BigDecimal bunchCount;
    private String unit;
    private String bake;
    private String inCount;
    private String color;
    private String orderColor;
    private String badReason;
    private String incomingType;
    private String incomingReason;
    private Date createTime;
    private Date modifiedTime;
    private Integer isDelete;
}
