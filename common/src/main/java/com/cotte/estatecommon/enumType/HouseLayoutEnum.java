package com.cotte.estatecommon.enumType;

/**
 * @ClassName HouseLayoutEnum
 * @description: 户型枚举
 * @author: 田新宇
 * @date: 2022-04-01
 */
public enum HouseLayoutEnum {
    //中间户
    EAST("A","中间户"),
    //西户
    WAST("B","西户"),
    //东户
    CENTER("C","东户"),
    //南户
    SOUTH("D","南户"),
    //北户
    NORTH("E","北户");

    private   String code;

    private   String describe;

    HouseLayoutEnum(String code,String describe){
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
