package com.hong.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import cn.addapp.pickers.util.ConvertUtils;
import com.hong.R;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StringUtils {
    private static final Map<Locale, String> DATE_REGEX_MAP = new HashMap();

    public static boolean isBlank(@Nullable String str) {
        return str == null || str.trim().equals("");
    }

    public static boolean isBlankList(@Nullable List list) {
        return list == null || list.size() == 0;
    }

    public static List<String> stringToList(@NonNull String str, @NonNull String separator) {
        if (str.contains(separator)) {
            return Arrays.asList(str.split(separator));
        }
        return null;
    }

    public static String listToString(@NonNull List<String> list, @NonNull String separator) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (list.size() == 0 || isBlank(separator)) {
            return stringBuilder.toString();
        }
        for (int i = 0; i < list.size(); i++) {
            stringBuilder.append((String) list.get(i));
            if (i != list.size() - 1) {
                stringBuilder.append(separator);
            }
        }
        return stringBuilder.toString();
    }

    public static String getSizeString(long size) {
        float sizeK;
        if (size < 1024) {
            return String.format(Locale.getDefault(), "%d B", new Object[]{Long.valueOf(size)});
        } else if (size < 1048576) {
            sizeK = ((float) size) / 1024.0f;
            return String.format(Locale.getDefault(), "%.2f KB", new Object[]{Float.valueOf(sizeK)});
        } else if (size < ConvertUtils.GB) {
            sizeK = ((float) size) / 1048576.0f;
            return String.format(Locale.getDefault(), "%.2f MB", new Object[]{Float.valueOf(sizeK)});
        } else if (size / 1024 >= ConvertUtils.GB) {
            return null;
        } else {
            sizeK = ((float) size) / 1.07374182E9f;
            return String.format(Locale.getDefault(), "%.2f GB", new Object[]{Float.valueOf(sizeK)});
        }
    }

    static {
        String str = "yyyy-MM-dd";
        DATE_REGEX_MAP.put(Locale.CHINA, str);
        DATE_REGEX_MAP.put(Locale.TAIWAN, str);
        DATE_REGEX_MAP.put(Locale.ENGLISH, "MMM d, yyyy");
        str = "dd.MM.yyyy";
        DATE_REGEX_MAP.put(Locale.GERMAN, str);
        DATE_REGEX_MAP.put(Locale.GERMANY, str);
    }

    public static String getDateStr(@NonNull Date date) {
        Locale locale = AppUtils.getLocale(PrefUtils.getLanguage());
        return new SimpleDateFormat(DATE_REGEX_MAP.containsKey(locale) ? (String) DATE_REGEX_MAP.get(locale) : "yyyy-MM-dd", locale).format(date);
    }

    public static String getNewsTimeStr(@NonNull Context context, @NonNull Date date) {
        Context context2 = context;
        long subTime = System.currentTimeMillis() - date.getTime();
        if (((double) subTime) < 1000.0d) {
            return context2.getString(R.string.just_now);
        }
        String str = " ";
        double MILLIS_LIMIT;
        if (((double) subTime) < 60000.0d) {
            StringBuilder stringBuilder = new StringBuilder();
            MILLIS_LIMIT = 1000.0d;
            stringBuilder.append(Math.round(((double) subTime) / 1000.0d));
            stringBuilder.append(str);
            stringBuilder.append(context2.getString(R.string.seconds_ago));
            return stringBuilder.toString();
        }
        MILLIS_LIMIT = 1000.0d;
        StringBuilder stringBuilder2;
        double SECONDS_LIMIT;
        if (((double) subTime) < 3600000.0d) {
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(Math.round(((double) subTime) / 60000.0d));
            stringBuilder2.append(str);
            stringBuilder2.append(context2.getString(R.string.minutes_ago));
            return stringBuilder2.toString();
        } else if (((double) subTime) < 8.64E7d) {
            stringBuilder2 = new StringBuilder();
            SECONDS_LIMIT = 60000.0d;
            stringBuilder2.append(Math.round(((double) subTime) / 3600000.0d));
            stringBuilder2.append(str);
            stringBuilder2.append(context2.getString(R.string.hours_ago));
            return stringBuilder2.toString();
        } else {
            SECONDS_LIMIT = 60000.0d;
            if (((double) subTime) >= 2.592E9d) {
                return getDateStr(date);
            }
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(Math.round(((double) subTime) / 8.64E7d));
            stringBuilder2.append(str);
            stringBuilder2.append(context2.getString(R.string.days_ago));
            return stringBuilder2.toString();
        }
    }

    public static String upCaseFirstChar(String str) {
        if (isBlank(str)) {
            return null;
        }
        return str.substring(0, 1).toUpperCase().concat(str.substring(1));
    }

    @NonNull
    public static Date getDateByTime(@NonNull Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTime();
    }

    @NonNull
    public static Date getTodayDate() {
        return getDateByTime(new Date());
    }
}
