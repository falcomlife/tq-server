package com.cotte.estatecommon.enumType;
/**
 * @ClassName CheckStatusEnum
 * @description: 车位管理，车位类型
 * @author: tgt
 * @date: 2022-04-07 10:59
 */
public enum ParkingSpaceTypeEnum {

    //固定车位
    FIXED("0","固定车位"),

    //临时车位
    TEMPORARY("1","临时车位"),

    //月租车位
    MONTH("2","月租车位"),

    //未使用车位
    NEW("3","未使用车位"),

    //虚拟车位
    FICTITIOUS("4","虚拟车位");

    private   String code;

    private   String describe;

    ParkingSpaceTypeEnum(String code, String describe){
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
