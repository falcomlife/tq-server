package com.cotte.estatecommon.utils;

/**
 * @ClassName CornUtil
 * @description: corn表达式生成器
 * @author: qhs
 * @date: 2022-12-01 14:11
 */
public class CornUtil {

    /**
     * 方法摘要：构建Cron表达式
     *
     * @param type
     * @param dayOfMon  天
     * @param hour      小时 0-23
     * @param min       分钟 0-59
     * @param month     月份
     * @param dayOfWeek 周
     * @return
     */
    public static String genCron(int type, String min, String hour, String dayOfMon, String month, String dayOfWeek) {
        String cron = "";
        switch (type) {
            case 0:// 每年month月的dayOfMonth号的hour点min分00秒执行
                cron = "0 " + min + " " + hour + " " + dayOfMon + " " + month + " ?";
                break;
            case 1:// 每月的dayOfMon号的hour点param3分执行
                cron = "0 " + min + " " + hour + " " + dayOfMon + " * ?";
                break;
            case 2:// 每周的dayOfWeek的hour时，min分执行
                cron = "0 " + min + " " + hour + " ? * " + dayOfWeek;
                break;
            case 3:// 每天的hour时，min分执行
                cron = "0 " + min + " " + hour + " ? * *";
                break;
        }
        return cron;
    }

    /**
     * 方法摘要：构建Cron描述
     *
     * @param type
     * @param dayOfMon  天
     * @param hour      小时 0-23
     * @param min       分钟 0-59
     * @param month     月份
     * @param dayOfWeek 周
     * @return
     */
    public static String genCronDesc(int type, String min, String hour, String dayOfMon, String month, String dayOfWeek) {
        String desc = "";
        switch (type) {
            case 0://   每年month月的dayOfMon号的hour时，min分执行
                desc = "每年" + month + "月" + dayOfMon + "号的" + hour + "点" + min + "分触发一次任务";
                break;
            case 1:// 每月的dayOfMon号的hour时，min分执行
                desc = "每月" + dayOfMon + "号的" + hour + "点" + min + "分触发一次任务";
                break;
            case 2:// 每周的dayOfWeek的hour时，min分执行
                desc = "每周" + (Integer.valueOf(dayOfWeek) - 1) + "的" + hour + "点" + min + "分触发一次任务";
                break;
            case 3:// 每天的hour时，min分执行
                desc = "每天" + hour + "点" + min + "分触发一次任务";
                break;
        }
        return desc;
    }

    // 参考例子
    public static void main(String[] args) {
        System.out.println(genCron(0, "01", "23", "2", "1,4,7,10", ""));
        System.out.println(genCronDesc(0, "01", "23", "2", "1,4,7,10", ""));
    }
}
