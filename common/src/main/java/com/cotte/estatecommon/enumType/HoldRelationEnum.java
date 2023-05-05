package com.cotte.estatecommon.enumType;

/**
 * @ClassName HoldRelationEnum
 * @description: 与业主的关系枚举
 * @author: ygj
 * @date: 2022-03-31 13:03
 */
public enum  HoldRelationEnum {

    //配偶
    COUPLE("0","夫妻"),
    //子女
    CHILD("1","子女"),

    //父母
    PARENT("2","父母"),

    //亲戚
    RELATIVE("3","亲戚"),

    //租客
    TENANT("4","租客");


    private   String code;

    private   String describe;

    HoldRelationEnum(String code,String describe){
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
