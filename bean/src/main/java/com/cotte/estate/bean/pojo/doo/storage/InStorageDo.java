package com.cotte.estate.bean.pojo.doo.storage;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
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
@Table(name="in_storage")
public class InStorageDo {
    @Id
    @GeneratedValue(generator="uuidGenerator")
    private String id;
    @Column(name = "order_id")
    private String orderId;
    @Column(name = "code")
    private String code;
    @Column(name = "image")
    private String image;
    @Column(name = "name")
    private String name;
    @Column(name = "color")
    private String color;
    @Column(name = "bunch_count")
    private Integer bunchCount;
    @Column(name = "bake")
    private String bake;
    @Column(name = "in_count")
    private String inCount;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "sum")
    private BigDecimal sum;
    @Column(name = "incoming_type")
    private String incomingType;
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
