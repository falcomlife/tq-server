package com.cotte.estatecommon.enumType;

/**
 * @ClassName ProcessEnum
 * @description: 事务状态
 * @author: ygj
 * @date: 2022-04-01 18:33
 */
public enum  ProcessStatusEnum {

    //进行中
    PROGRESS("0","进行中"),
    //待验收
    ACCEPTED("1","待验收"),
    //已完成
    COMPLETE("2","已完成");

    private   String code;

    private   String describe;

    ProcessStatusEnum(String code,String describe){
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
