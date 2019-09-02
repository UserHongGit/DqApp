package com.hong.util;

import android.widget.Toast;
import com.hong.AppApplication;

public class ToastUtils {
    private static Toast mToast;

    private ToastUtils() {
    }

    public static void show(String str) {
        Toast toast = mToast;
        if (toast == null) {
            mToast = Toast.makeText(AppApplication.get(), str, 0);
        } else {
            toast.setText(str);
        }
        mToast.show();
    }
}
