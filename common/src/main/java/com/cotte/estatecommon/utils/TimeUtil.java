package com.cotte.estatecommon.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * @ClassName TimeUtil
 * @description: 测试工具
 * @author: ygj
 * @date: 2022-03-25 16:09
 */

public class TimeUtil {

    public static String test(){
        return DateUtil.format(new Date(),"yyyy-MM-dd");
    }

    /**
     * 时间格式转换
     * @param time
     * @param oldFormat
     * @param newFormat
     * @return
     */
    public static String parseTime(String time,String oldFormat,String newFormat){
        final DateTime parse = DateUtil.parse(time, oldFormat);
        return  DateUtil.format(parse, newFormat);
    }

    public static void main(String[] args) {
        String time = "2022-04-11 17:37:57";
        final String s = parseTime(time, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd");
        System.out.println(s);
    }
}
