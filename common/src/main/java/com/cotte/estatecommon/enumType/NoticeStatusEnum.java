package com.cotte.estatecommon.enumType;

/**
 * @ClassName NoticeStatusEnum
 * @description: 公告状态枚举
 * @author: 田新宇
 * @date: 2022-04-08
 */
public enum NoticeStatusEnum {

    //草稿
    DRAFT("1","草稿"),
    //已发布
    PUBLISH("2","已发布");

    private   String code;

    private   String describe;

    NoticeStatusEnum(String code,String describe){
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
