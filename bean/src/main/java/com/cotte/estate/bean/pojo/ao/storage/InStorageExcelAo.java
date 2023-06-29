package com.cotte.estate.bean.pojo.ao.storage;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @ClassName InStorageExcelAo
 * @description: 入库
 * @author: sora
 * @date: 2022-06-26
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class InStorageExcelAo {

    private String customerNameItem;
    private String incomingType;
    private String code;
    private String starttime;
    private String endtime;

}
