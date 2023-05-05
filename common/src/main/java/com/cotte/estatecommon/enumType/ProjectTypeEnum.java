package com.cotte.estatecommon.enumType;
/**
 * @ClassName ProjectTypeEnum
 * @description: 项目类型枚举
 * @author: tgt
 * @date: 2022-03-31 10:59
 */
public enum ProjectTypeEnum {

    //商业
    BUSINESS("0","商业"),
    //住宅
    RESIDENCE("1","住宅"),
    //混合
    BLEND("2","混合");


    private   String code;

    private   String describe;

    ProjectTypeEnum(String code,String describe){
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
