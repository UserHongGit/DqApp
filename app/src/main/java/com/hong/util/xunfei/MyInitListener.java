package com.hong.util.xunfei;

import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;

public class MyInitListener implements InitListener {



    @Override
    public void onInit(int code) {
        if (code != ErrorCode.SUCCESS) {
            Log.i("========", "onInit:初始化失败___-");
//            showTip("初始化失败 ");
        }

    }
}
