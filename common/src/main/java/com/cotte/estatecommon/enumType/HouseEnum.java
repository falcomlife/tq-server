package com.cotte.estatecommon.enumType;

/**
 * @ClassName HouseEnum
 * @description: TODO
 * @author: qhs
 * @date: 2022-04-01 08:04
 */
public enum HouseEnum {

    //业主自住
    OWNERCCCUPIED("0","业主自住"),
    //物业办公室
    OWNEROFFICE("1","物业办公室"),
    //委员会办公室
    COMMITTEEOFFICE("2","委员会办公室"),
    //其他物业用房
    OTHERPROPERTIES("3","其他物业用房"),
    //虚拟房屋
    VIRTUALREALESTATE("4","虚拟房产");


    private String code;

    private   String describe;

    HouseEnum(String code,String describe){
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
