package com.cotte.estate.bean.pojo.ao.storage;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserAo {

    private String id;
    private String companyId;
    private String roleId;
    private String companyCode;
    private String name;
    private String code;
    private String account;
    private String password;
    private String newPassword;
    private Boolean isLock;
    private String isLockName;
    private Boolean roleFlag;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;
    private Boolean isDelete;
}
