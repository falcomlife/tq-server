package com.cotte.estatecommon.enumType;

/**
 * @ClassName SexEnum
 * @description: 性别枚举
 * @author: ygj
 * @date: 2022-03-28 16:29
 */
public enum  SexEnum {

    //男
    MAN("0","男"),
    //女
    FEMALE("1","女");

    private   String code;

    private   String describe;

    SexEnum(String code,String describe){
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
