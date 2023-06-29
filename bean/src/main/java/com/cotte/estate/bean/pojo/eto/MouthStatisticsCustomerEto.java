package com.cotte.estate.bean.pojo.eto;

import lombok.Data;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
@Data
public class MouthStatisticsCustomerEto {
    private String name;
    private BigDecimal count;
}
