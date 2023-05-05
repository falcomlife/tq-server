package com.cotte.estatecommon.enumType;

/**
 * @ClassName StationEnum
 * @description: 岗位枚举
 * @author: 田新宇
 * @date: 2022-03-31
 */
public enum StationEnum {
    //工程经理
    MANAGER("0","工程经理"),
    //工程监督
    OVERSEER("1","工程监督"),
    //工程师
    ENGINEER("2","工程师");


    private   String code;

    private   String describe;

    StationEnum(String code,String describe){
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
