package com.cotte.estatecommon.enumType;

/**
 * @ClassName KFStatusEnum
 * @description: 客服管理的处理状态
 * @author: qhs
 * @date: 2022-04-02 08:24
 */
public enum KFStatusEnum {

    //待处理
    PENDING("0","待处理"),

    //进行中
    HAVEINHAND("1","进行中"),

    //已完成
    COMPLETED("2","已完成");

    private String code;

    private String describe;

    KFStatusEnum(String code, String describe){
        this.code = code;
        this.describe = describe;

    }
    public String getDes(){
        return this.describe;
    }

    public String getCode(){
        return this.code;
    }

}
