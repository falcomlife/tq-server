package com.cotte.estate.bean.pojo.doo.storage;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName orderAO
 * @description: 订单
 * @author: sora
 * @date: 2022-06-26
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name="`b_order`")
public class OrderDo {
    @Id
    @GeneratedValue(generator="uuidGenerator")
    private String id;
    @Column(name = "code")
    private String code;
    @Column(name = "customer_name")
    private String customerName;
    @Column(name = "image")
    private String image;
    @Column(name = "po_num")
    private String poNum;
    @Column(name = "item")
    private String item;
    @Column(name = "color")
    private String color;
    @Column(name = "count")
    private BigDecimal count;
    @Column(name = "part_sum_count")
    private BigDecimal partSumCount;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "sum")
    private BigDecimal sum;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;
    @Column(name = "is_delete")
    private Integer isDelete;
}
