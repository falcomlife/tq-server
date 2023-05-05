package com.cotte.estatecommon.enumType;

/**
 * @ClassName YesOrNoEnum
 * @description: 是否
 * @author: ygj
 * @date: 2022-04-01 14:21
 */
public enum  YesOrNoEnum {
    //是
    YES("1","是"),
    //否
    NO("0","否");

    private   String code;

    private   String describe;

    YesOrNoEnum(String code,String describe){
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
