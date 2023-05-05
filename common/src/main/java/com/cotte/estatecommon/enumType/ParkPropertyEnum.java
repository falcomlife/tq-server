package com.cotte.estatecommon.enumType;
/**
 * @ClassName CheckStatusEnum
 * @description: 车厂类型
 * @author: tgt
 * @date: 2022-04-01 10:59
 */
public enum ParkPropertyEnum {

    //地下
    LOWER("0","地下"),

    //地上
    UPPER("1","地上");

    private   String code;

    private   String describe;

    ParkPropertyEnum(String code, String describe){
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
