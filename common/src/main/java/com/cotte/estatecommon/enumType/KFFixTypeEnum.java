package com.cotte.estatecommon.enumType;

/**
 * @ClassName KFFixTypeEnum
 * @description: 客服管理的报修类型
 * @author: 田新宇
 * @date: 2022-04-02
 */
public enum KFFixTypeEnum {
    //水路系统
    WATER("0","水路系统"),
    //电路系统
    ELECTRIC("1","电路系统"),
    //公共设备设施
    EQUIPMENT("2","公共设备设施"),
    //公共环境
    ENVIRONMENT("3","公共环境"),
    //建筑主体
    BUILDING("4","建筑主体"),
    //其他报修
    ELSE("5","其他报修");

    private String code;

    private String describe;

    KFFixTypeEnum(String code, String describe){
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
