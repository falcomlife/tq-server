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
 * @ClassName OutStorageAo
 * @description: 出库
 * @author: sora
 * @date: 2022-06-26
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name="b_out_storage")
public class OutStorageDo {
    @Id
    @GeneratedValue(generator="uuidGenerator")
    private String id;
    @Column(name = "in_storage_id")
    private String inStorageId;
    @Column(name = "image")
    private String image;
    @Column(name = "code")
    private String code;
    @Column(name = "bunch_count")
    private BigDecimal bunchCount;
    @Column(name = "out_count")
    private String outCount;
    @Column(name = "out_type")
    private String outType;
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
