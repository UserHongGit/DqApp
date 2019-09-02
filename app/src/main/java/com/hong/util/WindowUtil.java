package com.hong.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;

public class WindowUtil {
    public static int screenHeight = 0;
    public static int screenWidth = 0;

    public static int dipToPx(@NonNull Context context, float dpValue) {
        return (int) ((dpValue * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int pxToDip(@NonNull Context context, float pxValue) {
        return (int) ((pxValue / context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int getStatusHeight(@NonNull Activity activity) {
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        int statusHeight = localRect.top;
        if (statusHeight != 0) {
            return statusHeight;
        }
        try {
            Class<?> localClass = Class.forName("com.android.internal.R$dimen");
            return activity.getResources().getDimensionPixelSize(Integer.parseInt(localClass.getField("status_bar_height").get(localClass.newInstance()).toString()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return statusHeight;
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            return statusHeight;
        } catch (InstantiationException e3) {
            e3.printStackTrace();
            return statusHeight;
        } catch (NumberFormatException e4) {
            e4.printStackTrace();
            return statusHeight;
        } catch (IllegalArgumentException e5) {
            e5.printStackTrace();
            return statusHeight;
        } catch (SecurityException e6) {
            e6.printStackTrace();
            return statusHeight;
        } catch (NoSuchFieldException e7) {
            e7.printStackTrace();
            return statusHeight;
        }
    }
}
