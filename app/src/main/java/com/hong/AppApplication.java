

package com.hong;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.hong.inject.component.AppComponent;
import com.hong.inject.component.DaggerAppComponent;
import com.hong.inject.module.AppModule;
import com.hong.service.NetBroadcastReceiver;
import com.hong.util.AppUtils;
import com.hong.util.MyUtils;
import com.hong.util.NetHelper;
import com.hong.util.PrefUtils;
import com.tencent.bugly.Bugly;

import java.util.Locale;

/**
 * AppApplication
 * Created by upc_jxzy on 2016/7/13 14:01
 */
public class AppApplication extends Application {

    private final String TAG = AppApplication.class.getSimpleName()+"==================================";

    private static AppApplication application;
    private AppComponent mAppComponent;
    public static boolean isLogin;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate:  Application创建 ");
        application = this;
        //init application
        long startTime = System.currentTimeMillis();
        AppData.INSTANCE.getSystemDefaultLocal();
        //apply language for application context, bugly used it
        //把这句话注释掉就默认成中文了,如果打开就成英文了,但是我不知道为什么能改
//        AppUtils.updateAppLanguage(getApplicationContext());

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        initNetwork();
        initBugly();
        startTime = System.currentTimeMillis();


    }

    private void initLogger(){
        Log.i(TAG, "initLogger: 初始化Logger?");
//        PrettyFormatStrategy strategy = PrettyFormatStrategy.newBuilder()
//                        .showThreadInfo(false)
//                        .methodCount(0)
//                        .methodOffset(0)
//                        .tag("OpenHub_Logger")
//                        .build();
//        Logger.addLogAdapter(new AndroidLogAdapter(strategy){
//            @Override
//            public boolean isLoggable(int priority, String tag) {
//                return BuildConfig.DEBUG;
//            }
//
//            @Override
//            public void log(int priority, String tag, String message) {
//                super.log(priority, tag, message);
//            }
//        });
    }

    private void initBugly(){
//
//        Beta.initDelay = 6 * 1000;
//        Beta.enableHotfix = false;
//        Beta.canShowUpgradeActs.add(LoginActivity.class);
//        Beta.canShowUpgradeActs.add(MainActivity.class);
//        Beta.canShowUpgradeActs.add(AboutActivity.class);
//        Beta.upgradeListener = UpgradeDialog.INSTANCE;
//
//        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
//        strategy.setAppVersion(BuildConfig.VERSION_NAME);
//        strategy.setAppChannel(getAppChannel());
//        strategy.setAppReportDelay(10 * 1000);
//        Bugly.init(getApplicationContext(), AppConfig.BUGLY_APPID, BuildConfig.DEBUG, strategy);
//        CrashReport.setIsDevelopmentDevice(getApplicationContext(), BuildConfig.DEBUG);
        Bugly.init(getApplicationContext(), "3f8ce1165b", true);

    }

    private void initNetwork(){
        Log.i(TAG, "initNetwork: 初始化网络?");
        NetBroadcastReceiver receiver = new NetBroadcastReceiver();
        IntentFilter filter;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        } else {
            filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        }
        registerReceiver(receiver, filter);

        NetHelper.INSTANCE.init(this);
    }

    public static AppApplication get(){
        return application;
    }

    public AppComponent getAppComponent(){
        return mAppComponent;
    }

    private String getAppChannel(){
        Log.i(TAG, "getAppChannel: ");
        String channel = "normal";
        try {
            ApplicationInfo applicationInfo = getPackageManager()
                    .getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            channel = applicationInfo.metaData.getString("BUGLY_APP_CHANNEL", "normal");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channel;
    }

}
