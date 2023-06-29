package com.cotte.estate.bean.pojo.eto;

import lombok.Data;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
@Data
public class InStorageEto {
    private String customerName;

    private String name;

    private String poNum;

    private String orderColor;
    // 组件数
    private BigDecimal bunchCount;

    private String badReason;
}
