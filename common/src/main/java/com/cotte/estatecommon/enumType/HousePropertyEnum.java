package com.cotte.estatecommon.enumType;

/**
 * @ClassName HousePropertyEnum
 * @description: TODO
 * @author: qhs
 * @date: 2022-04-01 08:04
 */
public enum  HousePropertyEnum {

    //高层
    HIGHLEVEL("0","高层"),
    //小高层
    SMALLHIGHLEVEL("1","小高层"),
    //多层
    MULTISTOREY("2","多层"),
    //别墅
    VILLA("3","别墅"),
    //商业住宅
    DWELLINGHOUSE("4","商业住宅");

    private String code;

    private   String describe;

    HousePropertyEnum(String code,String describe){
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
