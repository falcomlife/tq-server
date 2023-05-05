package com.cotte.estatecommon.enumType;
/*
*@ClassName ComplainantEnum
*@description: 咨询投诉的投诉人
*@author: qhs
*@date: 2022/4/2
*/
public enum ComplainantEnum {

    //业主
    OWNER("0","业主"),
    //客服
    CUSTOMER("1","客服");

    private String code;

    private String describe;

    ComplainantEnum(String code,String describe){
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
