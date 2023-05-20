package com.cotte.estate.bean.pojo.eto;

import lombok.Data;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
@Data
public class OrderEto {
    private String poNum;
    private String item;
    private String color;
    private BigDecimal count;
    private BigDecimal price;
    private BigDecimal sum;
}
