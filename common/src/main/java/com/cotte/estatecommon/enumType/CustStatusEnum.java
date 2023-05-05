package com.cotte.estatecommon.enumType;

/**
 * @ClassName CustStatusEnum
 * @description: 客户状态
 * @author: ygj
 * @date: 2022-03-28 16:48
 */
public enum  CustStatusEnum {

    //当前
    PERSENT("0","当前住户"),
    //历史
    HISTORY("1","历史住户");

    private   String code;

    private   String describe;

    CustStatusEnum(String code,String describe){
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
