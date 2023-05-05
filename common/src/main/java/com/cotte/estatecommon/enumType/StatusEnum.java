package com.cotte.estatecommon.enumType;

/**
 * @ClassName StationEnum
 * @description: 状态枚举
 * @author: 田新宇
 * @date: 2022-03-31
 */
public enum StatusEnum {
    //在岗
    ONTHEJOB("0","在岗"),
    //请假
    ASKFORLEAVE("1","请假"),
    //出差
    TRAVEL("2","出差"),
    //离职
    LEAVE("3","离职");

    private   String code;

    private   String describe;

    StatusEnum(String code,String describe){
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
