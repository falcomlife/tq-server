package com.cotte.estatecommon;

import lombok.Data;

import java.io.Serializable;

@Data
public class Subject implements Serializable {

    private static final long serialVersionUID = -8607741530610074402L;

    private String userId;

    private String account;

    private String tenantId;

    private String orgId;

    private String source;
}
