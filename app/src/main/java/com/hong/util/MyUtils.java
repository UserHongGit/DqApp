package com.hong.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyUtils {
    public static String dateConvertStr(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
}
