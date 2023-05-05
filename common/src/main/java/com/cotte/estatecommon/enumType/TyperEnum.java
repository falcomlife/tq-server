package com.cotte.estatecommon.enumType;

/*
*@ClassName TyperEnum
*@description: 楼宇类型
*@author: qhs
*@date: 2022/3/31
*/
public enum TyperEnum {

    //商业
    BUSINESS("0","商业"),
    //住宅
    RESIDENCE("1","住宅");

    private   String code;

    private   String describe;

    TyperEnum(String code,String describe){
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


