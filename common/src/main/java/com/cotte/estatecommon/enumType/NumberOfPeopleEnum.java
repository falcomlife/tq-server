package com.cotte.estatecommon.enumType;
/**
 * @ClassName NumberOfPeople
 * @description: 人数规模
 * @author: tgt
 * @date: 2022-04-01 10:59
 */
public enum NumberOfPeopleEnum {

    //0-100
    ONE("0", "0-100"),

    //100-200
    TWO("1", "100-200"),

    // 200-300
    THREE("2", "200-300");
    private String code;

    private String describe;

    NumberOfPeopleEnum(String code, String describe) {
        this.code = code;
        this.describe = describe;

    }

    public String getDes() {
        return this.describe;
    }

    public String getCode() {
        return this.code;
    }
}

