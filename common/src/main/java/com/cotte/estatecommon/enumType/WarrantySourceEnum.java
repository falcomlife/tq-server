package com.cotte.estatecommon.enumType;

/**
 * @ClassName WarrantySourceEnum
 * @description: 保修来源
 * @author: qhs
 * @date: 2022-04-02 08:26
 */
public enum  WarrantySourceEnum {

    //业主小程序
    OWNERAPPLET("0","业主小程序"),

    //物业小程序
    PROPERTYAPPLET("1","物业小程序"),

    //PC端发起
    PC("2","PC端发起");

    private String code;

    private String describe;

    WarrantySourceEnum(String code, String describe){
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
