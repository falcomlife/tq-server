package com.cotte.estatecommon.enumType;

/**
 * @ClassName SexEnum
 * @description: 部门枚举
 * @author: 田新宇
 * @date: 2022-03-31 10：21
 */
public enum DepartmentEnum {
    //工程部
    ENGINDE("0","工程部"),
    //企划部
    PLANDE("1","企划部"),
    //宣传部
    PUBLICITYDE("2","宣传部"),
    //后勤部
    LOGISTICSDE("3","后勤部"),
    //财务部
    FINANCEDE("4","财务部");


    private   String code;

    private   String describe;

    DepartmentEnum(String code,String describe){
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
