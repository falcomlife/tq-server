package com.cotte.estatecommon.enumType;

/**
 * @ClassName CustAttrEnum
 * @description: 客户属性
 * @author: ygj
 * @date: 2022-03-28 16:46
 */
public enum  CustAttrEnum {

    //个人
    PERSONAL("0","个人"),
    //企业
    BUSINESS("1","企业");

    private   String code;

    private   String describe;

    CustAttrEnum(String code,String describe){
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
