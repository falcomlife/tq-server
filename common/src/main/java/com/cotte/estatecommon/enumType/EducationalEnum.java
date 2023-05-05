package com.cotte.estatecommon.enumType;

/**
 * @ClassName StationEnum
 * @description: 学历枚举
 * @author: 田新宇
 * @date: 2022-03-31
 */
public enum EducationalEnum {

    //大专
    JUNIORCOLLEGE("0","大专"),
    //大学本科
    UNDERGRADUATE("1","大学本科"),
    //硕士研究生
    MASTERSDEGREE("2","硕士研究生"),
    //博士研究生
    DOCTORALCANDIDATE("3","博士研究生");

    private   String code;

    private   String describe;

    EducationalEnum(String code,String describe){
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
