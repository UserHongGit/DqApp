package com.hong.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.support.annotation.NonNull;
import android.util.Log;

public enum NetHelper {
    INSTANCE;
    
    public static final int TYPE_DISCONNECT = 0;
    public static final int TYPE_MOBILE = 2;
    public static final int TYPE_WIFI = 1;
    private Context mContext;
    private int mCurNetStatus;

    public void init(Context context) {
        this.mContext = context;
        checkNet();
    }

    public void checkNet() {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) this.mContext.getSystemService("connectivity");
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info == null || !info.isAvailable()) {
                    this.mCurNetStatus = 0;
                } else if (info.getState() == State.CONNECTED) {
                    if (info.getType() == 1) {
                        this.mCurNetStatus = 1;
                    }
                    if (info.getType() == 0) {
                        this.mCurNetStatus = 2;
                    }
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("mCurNetStatus");
            stringBuilder.append(this.mCurNetStatus);
            Log.i("=============>", stringBuilder.toString());
        } catch (Exception e) {
            Log.v("error", e.toString());
            e.printStackTrace();
            this.mCurNetStatus = 0;
        }
    }

    @NonNull
    public Boolean getNetEnabled() {
        int i = this.mCurNetStatus;
        boolean z = true;
        if (!(i == 2 || i == 1)) {
            z = false;
        }
        return Boolean.valueOf(z);
    }

    @NonNull
    public Boolean isMobileStatus() {
        return Boolean.valueOf(this.mCurNetStatus == 2);
    }

    public int getNetStatus() {
        return this.mCurNetStatus;
    }
}
