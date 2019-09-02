

package com.hong.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.hong.inject.component.AppComponent;
import com.hong.inject.component.DaggerActivityComponent;
import com.hong.inject.module.ActivityModule;
import com.hong.mvp.contract.ISplashContract;
import com.hong.mvp.presenter.SplashPresenter;
import com.hong.ui.activity.base.BaseActivity;


/**
 * Created on 2019/5/12.
 * 闪屏页  `
 *
 * @author upc_jxzy
 */
public class SplashActivity extends BaseActivity<SplashPresenter> implements ISplashContract.View {

    private final String TAG = "SplashActivity";

    private final int REQUEST_ACCESS_TOKEN = 1;

    @Override
    public void showMainPage() {
        Log.i("=============>","1 - sp showMainPage");
        delayFinish();
        Uri dataUri = getIntent().getData();
        if (dataUri == null) {
            Log.i("=============>","1 - sp showMainPage  if");
//            startActivity(new Intent(getActivity(), MainActivity.class));
            startActivity(new Intent(getActivity(), WebActivity.class));
        } else {
            Log.i("=============>","1 - sp showMainPage  else");
            //BrowserFilterActivity.handleBrowserUri(getActivity(), dataUri);
        }
    }

    @Override
    public void showLoginPage() {
        Log.i("=============>","2");
        delayFinish();
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    /**
     * 依赖注入的入口
     *
     * @param appComponent appComponent
     */
    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        Log.i("=============>","3 - sp setupActivityComponent");
        DaggerActivityComponent.builder()
                .appComponent(appComponent)
                .activityModule(new ActivityModule(getActivity()))
                .build()
                .inject(this);
    }

    /**
     * 获取ContentView id
     *
     * @return
     */
    @Override
    protected int getContentView() {
        Log.i("=============>","4 - sp getContentView");
        return 0;
    }

    /**
     * 初始化activity
     */
    @Override
    protected void initActivity() {
        Log.i("=============>","5 - sp initActivity");
        super.initActivity();
        mPresenter.getUser();//去验证用户，是否打开登录界面
    }

    /**
     * 初始化view
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        Log.i("=============>","6 - sp initView");
        super.initView(savedInstanceState);
    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("=============>","7");
        switch (requestCode) {
            case REQUEST_ACCESS_TOKEN:
                if(resultCode == RESULT_OK){
                    showMainPage();
                }
                break;
            default:
                break;
        }
    }

}
