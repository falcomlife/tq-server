package com.cotte.estatecommon.enumType;

/**
 * @ClassName TSTypeEnum
 * @description: 咨询投诉的投诉类型
 * @author: qhs
 * @date: 2022-04-01 17:42
 */
public enum  TSTypeEnum {

    //设备设施Equipment and facilities
    EQUIPMENT("0","设备设施"),
    //管理服务management service
    MANAGEMENT("1","管理服务"),
    //收费投诉Charge complaint
    COMPLAINT("1","收费投诉"),
    //突发事件Emergency
    EMERGENCY("1","突发事件"),
    //房屋质量House quality
    QUALITY("1","房屋质量"),
    //公共空间public space
    PUBLICSPACE("1","公共空间"),
    //景观绿化Landscape greening
    LANDSCAPE("1","景观绿化"),
    //噪音污染noise pollution
    NOISE("1","噪音污染");

    private String code;

    private String describe;

    TSTypeEnum(String code,String describe){
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
