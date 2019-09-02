package com.hong.util;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtil {
    public static String FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.S";
    public static String FORMAT_FULL_CN = "yyyy年MM月dd日  HH时mm分ss秒SSS毫秒";
    public static String FORMAT_LONG = "yyyy-MM-dd HH:mm:ss";
    public static String FORMAT_LONG_CN = "yyyy年MM月dd日  HH时mm分ss秒";
    public static String FORMAT_SHORT = "yyyy-MM-dd";
    public static String FORMAT_SHORT_CN = "yyyy年MM月dd";
    static Calendar calendar = Calendar.getInstance();
    static SimpleDateFormat df = new SimpleDateFormat(FORMAT_FULL);

    public static void main(String[] args) {
        System.out.println(getTimeString());
        PrintStream printStream = System.out;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("返回日期年份:");
        stringBuilder.append(getYear(new Date()));
        printStream.println(stringBuilder.toString());
        printStream = System.out;
        stringBuilder = new StringBuilder();
        stringBuilder.append("返回月份：");
        stringBuilder.append(getMonth(new Date()));
        printStream.println(stringBuilder.toString());
        printStream = System.out;
        stringBuilder = new StringBuilder();
        stringBuilder.append("返回当天日份");
        stringBuilder.append(getDay(new Date()));
        printStream.println(stringBuilder.toString());
        printStream = System.out;
        stringBuilder = new StringBuilder();
        stringBuilder.append("返回当天小时");
        stringBuilder.append(getHour(new Date()));
        printStream.println(stringBuilder.toString());
        printStream = System.out;
        stringBuilder = new StringBuilder();
        stringBuilder.append("返回当天分");
        stringBuilder.append(getMinute(new Date()));
        printStream.println(stringBuilder.toString());
        printStream = System.out;
        stringBuilder = new StringBuilder();
        stringBuilder.append("返回当天秒");
        stringBuilder.append(getSecond(new Date()));
        printStream.println(stringBuilder.toString());
        printStream = System.out;
        stringBuilder = new StringBuilder();
        stringBuilder.append("返回当天毫秒");
        stringBuilder.append(getMillis(new Date()));
        printStream.println(stringBuilder.toString());
        printStream = System.out;
        stringBuilder = new StringBuilder();
        stringBuilder.append("..........");
        stringBuilder.append(getHour(new Date()));
        printStream.println(stringBuilder.toString());
    }

    public static String getTimeString() {
        return df.format(calendar.getTime());
    }

    public static String getYear(Date date) {
        return df.format(date).substring(0, 4);
    }

    public static int getMonth(Date date) {
        calendar.setTime(date);
        return calendar.get(2) + 1;
    }

    public static int getDay(Date date) {
        calendar.setTime(date);
        return calendar.get(5);
    }

    public static int getHour(Date date) {
        calendar.setTime(date);
        return calendar.get(11);
    }

    public static int getMinute(Date date) {
        calendar.setTime(date);
        return calendar.get(12);
    }

    public static int getSecond(Date date) {
        calendar.setTime(date);
        return calendar.get(13);
    }

    public static long getMillis(Date date) {
        calendar.setTime(date);
        return calendar.getTimeInMillis();
    }
}
