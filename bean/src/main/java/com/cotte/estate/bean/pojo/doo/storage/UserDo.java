package com.cotte.estate.bean.pojo.doo.storage;

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
@Table(name = "s_user")
public class UserDo {
    @Id
    @GeneratedValue(generator="uuidGenerator")
    private String id;
    @Column(name = "company_id")
    private String companyId;
    @Column(name = "name")
    private String name;
    @Column(name = "account")
    private String account;
    @Column(name = "password")
    private String password;
    @Column(name = "is_lock")
    private Boolean isLock;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private Date modifiedTime;
    @Column(name = "is_delete")
    private Boolean isDelete;
}
