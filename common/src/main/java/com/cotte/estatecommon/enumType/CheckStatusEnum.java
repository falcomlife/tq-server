package com.cotte.estatecommon.enumType;

/**
 * @ClassName CheckStatusEnum
 * @description: 审核状态
 * @author: ygj
 * @date: 2022-03-31 10:59
 */
public enum  CheckStatusEnum {
    //待审核
    UNCHECKED("0","待审核"),
    //审核通过
    SUCCESS("1","已通过"),
    //审核不通过
    FAIL("2","已驳回");


    private   String code;

    private   String describe;

    CheckStatusEnum(String code,String describe){
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
