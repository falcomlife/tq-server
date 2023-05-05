package com.cotte.estatecommon.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * @ClassName DateUtil
 * @description: 获取时间区间内每个日期
 * @author: qhs
 * @date: 2022-09-29 09:03
 */
public class DateUtil {

    /**
     * 获取时间区间内每个日期 yyyy-MM-dd
     * @param begin
     * @param end
     */
    public static List<String> getDates(String begin, String end) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //设置开始时间
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(format.parse(begin));
        //设置结束时间
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(format.parse(end));
        //装返回的日期集合容器
        List<String> Datelist = new ArrayList<>();
        //将第一个月添加里面去
        Datelist.add(format.format(calBegin.getTime()));
        // 每次循环给calBegin日期加一天，直到calBegin.getTime()时间等于dEnd
        while (format.parse(end).after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            Datelist.add(format.format(calBegin.getTime()));
        }
        return Datelist;
    }

    public static List<String> getDayByMonth(int yearParam,int monthParam){
        List<String> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.set(yearParam, monthParam - 1, 1);
        int year = calendar.get(Calendar.YEAR);//年份
        int month = calendar.get(Calendar.MONTH) + 1;//月份
        int day = calendar.getActualMaximum(Calendar.DATE);
        for (int i = 1; i <= day; i++) {
            String date = null;
            if(month<10 && i<10){
                date = String.valueOf(year) + "-0" + month + "-0" + i;
            }
            if(month<10 && i>=10){
                date = String.valueOf(year) + "-0" + month + "-" + i;
            }
            if(month>=10 && i<10){
                date = String.valueOf(year) + "-" + month + "-0" + i;
            }
            if(month>=10 && i>=10){
                date = String.valueOf(year) + "-" + month + "-" + i;
            }
            list.add(date);
        }
        return list;
    }

    //获取当前时间的几天之前的日期thress
    public static String getDaysForThree(Date date,Integer count){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");//格式化一下
        Calendar calendar1 = Calendar.getInstance();//获取对日期操作的类对象
        //两种写法都可以获取到前三天的日期
        // calendar1.set(Calendar.DAY_OF_YEAR,calendar1.get(Calendar.DAY_OF_YEAR) -3);
        //在当前时间的基础上获取前三天的日期 (前天、昨天、今天)
        calendar1.add(Calendar.DATE, count);
        String oldDay = format.format(calendar1.getTime());
        return oldDay;
    }

    //获取当月的1号0点0时0分
    public static String getMonthFirst(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date monthBegin = cal.getTime();
        String monthBeginStr = format.format(monthBegin); //当月月初
        return monthBeginStr;
    }

    //获取当月的最后1天23点59时59分
    public static String getMonthLast(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date monthEnd = cal.getTime();
        String monthEndStr = format.format(monthEnd);  //当月月末
        return monthEndStr;
    }

    /**
     * 获取本月第一天
     */
    public static String getCurrentMonthFirstDay() throws Exception {
        Calendar firstMonthDay = Calendar.getInstance();
        firstMonthDay.add(Calendar.MONTH, 0);
        firstMonthDay.set(Calendar.DAY_OF_MONTH, 1);
        return DateUtil.dateToStr(firstMonthDay.getTime(),"yyyy-MM-dd");
    }

    /**
     * 将日期转换成字符串
     */
    public static String dateToStr(Date date, String format) throws Exception {
        if (date == null) return null;
        return new SimpleDateFormat(format).format(date);
    }

    //获取指定月第一天
    public static String getMonthFirstDay(String month) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date nowDate=sdf.parse(month);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return DateUtil.dateToStr(calendar.getTime(),"yyyy-MM-dd");
    }

    //获取指定月最后一天
    public static String getMonthLastDay(String month) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date nowDate=sdf.parse(month);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return DateUtil.dateToStr(calendar.getTime(),"yyyy-MM-dd");
    }
    /**
     * 获取本年第一天
     */
    public static String getCurrentYearFirstDay() throws Exception {
        Calendar calendar = Calendar.getInstance(); // 获取当前时间
        calendar.set(Calendar.MONTH, 0); // 将月份设置为1月份
        calendar.set(Calendar.DAY_OF_MONTH, 1); // 将天数设置为1号
        Date firstDayOfYear = calendar.getTime(); // 获取时间对象
        return DateUtil.dateToStr(firstDayOfYear,"yyyy-MM-dd");
    }
    /**
     * 获取本年最后一天
     */
    public static String getCurrentYearLastDay() throws Exception {
        Calendar calendar = Calendar.getInstance(); // 获取当前时间
        calendar.set(Calendar.MONTH, 11); // 将月份设置为12月份
        calendar.set(Calendar.DAY_OF_MONTH, 31); // 将天数设置为31号
        Date lastDayOfYear = calendar.getTime(); // 获取时间对象
        return DateUtil.dateToStr(lastDayOfYear,"yyyy-MM-dd");
    }
    /**
     * 获取指定年年第一天
     */
    public static String getFirstDayByYear(String time)throws Exception{
        String year=time.substring(0,4);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(year));
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return DateUtil.dateToStr(calendar.getTime(),"yyyy-MM-dd");
    }
    /**
     * 获取指定年最后一天
     */
    public static String getLastDayByYear(String time)throws Exception{
        String year=time.substring(0,4);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(year));
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        return DateUtil.dateToStr(calendar.getTime(),"yyyy-MM-dd");
    }
    /**
     * @param
     * @param date 需计算的时间
     * @param month 要加几个月
     * @return String
     **/
    public static String dateAdd(String date,int month)throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate=sdf.parse(date);
        Calendar newDate=Calendar.getInstance();
        newDate.setTime(nowDate);
        newDate.add(Calendar.MONTH,month);
        return DateUtil.dateToStr(newDate.getTime(),"yyyy-MM-dd");
    }

    public static Map<String,String> getTodayAndTomorrow(){
        // 获取今天的日期
        LocalDate today = LocalDate.now();
        // 获取明天的日期
        LocalDate tomorrow = today.plusDays(1);
        String todayString=today.toString().concat(" 00:00:00");
        String tomorrowString=tomorrow.toString().concat(" 00:00:00");
        Map<String,String> map=new HashMap<>();
        map.put("todayString",todayString);
        map.put("tomorrowString",tomorrowString);
        return map;
    }

    public static void main(String[] args) throws ParseException {
        //List<String> dates = getDates("2020-03-01", "2020-03-01");
        //List<String> dayByMonth = getDayByMonth(2020, 03);
        //System.out.println(dayByMonth);
//        String daysForThree = getDaysForThree(new Date(),1);
          System.out.println(getMonthFirst());
          System.out.println(getMonthLast());
        //try {
        //    System.out.println(DateUtil.getLastDayByYear("2023-03-01"));
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}
    }

}



