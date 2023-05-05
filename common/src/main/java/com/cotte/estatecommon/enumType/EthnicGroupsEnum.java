package com.cotte.estatecommon.enumType;

/**
 * @ClassName EthnicGroupsEnum
 * @description: 民族
 * @author: ygj
 * @date: 2022-03-29 15:28
 */
public enum  EthnicGroupsEnum {

    HANZU("汉族"),
    ZHUANGZU("壮族"),
    HUIZU("回族"),
    MANZU("满族"),
    WEIWUERZU("维吾尔族"),
    MIAOZU("苗族"),
    YIZU("彝族"),
    TUJIAZU("土家族"),
    ZANGZU("藏族"),
    MENGGUZU("蒙古族"),
    DONGZU("侗族"),
    BUYIZU("布依族"),
    YAOZU("瑶族"),
    BAIZU("白族"),
    CHAOXIANZU("朝鲜族"),
    HANIZU("哈尼族"),
    LIZU("黎族"),
    HASAKEZU("哈萨克族"),
    DAIZU("傣族"),
    SEZU("畲族"),
    LISUZU("傈僳族"),
    DONGXIANGZU("东乡族"),
    GELAOZU("仡佬族"),
    LAHUZU("拉祜族"),
    WAZU("佤族"),
    SHUIZU("水族"),
    NAXIZU("纳西族"),
    QIANGZU("羌族"),
    TUZU("土族"),
    MULAOZU("仫佬族"),
    XIBOZU("锡伯族"),
    KEERKEZIZU("柯尔克孜族"),
    JINGPOZU("景颇族"),
    DAWOERZU("达斡尔族"),
    SALAZU("撒拉族"),
    BULANGZU("布朗族"),
    MAONANZU("毛南族"),
    TAJIKEZU("塔吉克族"),
    PUMIZU("普米族"),
    ACHANGZU("阿昌族"),
    NUZU("怒族"),
    EWENKEZU("鄂温克族"),
    JINGZU("京族"),
    JINUOZU("基诺族"),
    DEANGZU("德昂族"),
    BAOANZU("保安族"),
    ELUOSIZU("俄罗斯族"),
    YUGUZU("裕固族"),
    WUZIBIEKEZU("乌孜别克族"),
    MENBAZU("门巴族"),
    ELUNCHUNZU("鄂伦春族"),
    DULONGZU("独龙族"),
    HEZHEZU("赫哲族"),
    GAOSHANZU("高山族"),
    LUOBAZU("珞巴族"),
    TATAERZU("塔塔尔族");



    private   String describe;

    EthnicGroupsEnum(String describe){
        this.describe = describe;

    }

    public String getDes(){
        return this.describe;
    }



}
