package com.cotte.estatecommon.utils;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * @ClassName CodeGenerUtil
 * @description: 编码生成规则工具类
 * @author: ygj
 * @date: 2022-04-01 10:18
 */
public class CodeGenerUtil {


    /**
     * 生成编码
     * @param prefix
     * @param inintCount
     * @return
     */
    public static String getCode(String prefix,int inintCount){
        StringBuilder code = new StringBuilder();
        //时间字符串
        final String yyyyMMdd = DateUtil.format(new Date(), "yyyyMMdd");
        //初始化的count+1 成为新的count
        final int newCount = inintCount + 1;
        //判断newCount 有几位
        final String newCountStr = BaseTypeUtil.safeString(newCount);
        final int length = newCountStr.length();
        String lastStr = null;
        if(length == 1){
            lastStr = "0000"+newCountStr;
        }else if(length == 2){
            lastStr = "000"+newCountStr;
        }else if(length == 3){
            lastStr = "00"+newCountStr;
        }else if(length == 4){
            lastStr = "0"+newCountStr;
        }else if(length == 5){
            lastStr = newCountStr;
        }
        //拼接code
        return  code.append(prefix).append(yyyyMMdd).append(lastStr).toString();
    }

    public static void main(String[] args) {
//        final String bs = getCode("BS", 0);
//        final String bs1 = getCode("BS", 1);
//        final String bs2 = getCode("BS", 9);
//        final String bs3 = getCode("BS", 11);
        final String bs4 = getCode("BS", 9999998);
//        System.out.println(bs);
//        System.out.println(bs1);
//        System.out.println(bs2);
//        System.out.println(bs3);
        System.out.println(bs4);
//        final String s1 = getNo(19);
//        System.out.println(s1);
    }
    public static String getNo(Integer size){
        StringBuilder code = new StringBuilder();
        //初始化的count+1 成为新的count
        final int newCount = 1+size;
        //判断newCount 有几位
        final String newCountStr = BaseTypeUtil.safeString(newCount);
        final int length = newCountStr.length();
        String lastStr = null;
        if(length == 1){
            lastStr = "100000"+newCountStr;
        }else if(length == 2) {
            lastStr = "10000" + newCountStr;
        }
        else if(length == 3) {
            lastStr = "1000" + newCountStr;
        }
        else if(length == 4){
            lastStr = "100"+newCountStr;
        }else if(length == 5){
            lastStr = "10"+newCountStr;
        }
        else if(length == 6){
            lastStr = "1"+newCountStr;
        }
        else if(length == 7 && size==999999){
            lastStr = "2000000";
        }
        else{
            lastStr = 1000000+newCount+"";
        }
        //拼接code
        return  code.append(lastStr).toString();
    }
}
