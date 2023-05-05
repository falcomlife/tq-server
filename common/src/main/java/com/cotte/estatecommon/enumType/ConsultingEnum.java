package com.cotte.estatecommon.enumType;
/*
*@ClassName ConsultingEnum
*@description: 咨询投诉选择类型
*@author: qhs
*@date: 2022/4/2
*/
public enum ConsultingEnum {

    //咨询
    CONCULTING("0","咨询"),
    //投诉
    COMPLAINT("1","投诉");

    private String code;

    private String describe;

    ConsultingEnum(String code,String describe){
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
