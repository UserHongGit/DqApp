package com.hong.util;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyUtils {
    public static String dateConvertStr(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static boolean currentLanguageIsSimpleChinese(Context context) {
        String language;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            language = context.getResources().getConfiguration().getLocales().get(0).getLanguage();
        } else {
            language = context.getResources().getConfiguration().locale.getLanguage();
        }
        Log.e("LanguageUtil", "language = " + language);
        return TextUtils.equals("zh", language);
    }

}
