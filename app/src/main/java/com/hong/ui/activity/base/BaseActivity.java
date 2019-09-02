

package com.hong.ui.activity.base;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.hong.AppApplication;
import com.hong.AppData;
import com.hong.R;
import com.hong.inject.component.AppComponent;
import com.hong.mvp.contract.base.IBaseContract;
import com.hong.ui.activity.LoginActivity;
import com.hong.ui.activity.SplashActivity;
import com.hong.ui.widget.DoubleClickHandler;
import com.hong.util.AppUtils;
import com.hong.util.PrefUtils;
import com.hong.util.ThemeHelper;
import com.hong.util.WindowUtil;
import com.thirtydegreesray.dataautoaccess.DataAutoAccess;
import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

/**
 * This is base activity to set some common things.
 * Created by upc_jxzy on 2016/7/13 18:13
 */
public abstract class BaseActivity<P extends IBaseContract.Presenter> extends AppCompatActivity implements IBaseContract.View {
    private static String TAG = BaseActivity.class.getSimpleName()+"============";

    @Inject
    protected P mPresenter;
    private ProgressDialog mProgressDialog;
    private static BaseActivity curActivity;

    protected boolean isAlive = true;
    @BindView(R.id.toolbar)
    @Nullable
    protected Toolbar toolbar;
    @BindView(R.id.toolbar_layout) @Nullable protected CollapsingToolbarLayout toolbarLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: ");
        //非登录页及展示页时检查用户登录
//        if ((AppData.INSTANCE.getAuthUser() == null || AppData.INSTANCE.getLoggedUser() == null)
        if (( AppData.INSTANCE.getLoggedUser() == null)
                && !this.getClass().equals(SplashActivity.class)
                && !this.getClass().equals(LoginActivity.class)) {
            super.onCreate(savedInstanceState);
            finishAffinity();
            startActivity(new Intent(getActivity(), SplashActivity.class));
            return;
        }

        ThemeHelper.apply(this);
        AppUtils.updateAppLanguage(getActivity());//语言
        super.onCreate(savedInstanceState);
        isAlive = true;
        setupActivityComponent(getAppComponent());
        DataAutoAccess.getData(this, savedInstanceState);
        if (mPresenter != null) {
            mPresenter.onRestoreInstanceState(savedInstanceState == null ?
                    getIntent().getExtras() : savedInstanceState);
            mPresenter.attachView(this);
        }
//        if (savedInstanceState != null && AppData.INSTANCE.getAuthUser() == null) {
         if (savedInstanceState != null) {
            DataAutoAccess.getData(AppData.INSTANCE, savedInstanceState);
        }
        getScreenSize();

        if (getContentView() != 0) {
            setContentView(getContentView());
            ButterKnife.bind(getActivity());
        }

        initActivity();
        initView(savedInstanceState);
        if (mPresenter != null) mPresenter.onViewInitialized();
        if (isFullScreen) {
            intoFullScreen();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState: ");
        //系统由于内存不足而杀死activity，此时保存数据
        DataAutoAccess.saveData(this, outState);
        if (mPresenter != null) mPresenter.onSaveInstanceState(outState);
        if (curActivity.equals(this)) {
            DataAutoAccess.saveData(AppData.INSTANCE, outState);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.i(TAG, "onPostCreate: ");
    }

    /**
     * 依赖注入的入口
     *
     * @param appComponent appComponent
     */
    protected abstract void setupActivityComponent(AppComponent appComponent);

    /**
     * 获取ContentView id
     *
     * @return
     */
    @LayoutRes
    protected abstract int getContentView();

    /**
     * 初始化activity
     */
    @CallSuper
    protected void initActivity() {
        Log.i(TAG, "initActivity: ");
    }

    /**
     * 初始化view
     */
    @CallSuper
    protected void initView(Bundle savedInstanceState) {
        Log.i(TAG, "initView: ");
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            DoubleClickHandler.setDoubleClickListener(toolbar, new DoubleClickHandler.DoubleClickListener() {
                @Override
                public void onDoubleClick(View view) {
                    onToolbarDoubleClick();
                }
            });
        }
    }

    protected void onToolbarDoubleClick() {
        Log.i(TAG, "onToolbarDoubleClick: ");
        PrefUtils.set(PrefUtils.DOUBLE_CLICK_TITLE_TIP_ABLE, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        curActivity = getActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        if (mPresenter != null) mPresenter.detachView();
        if (this.equals(curActivity)) curActivity = null;
        isAlive = false;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.i(TAG, "onTrimMemory: ");
        Glide.with(this).onTrimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.i(TAG, "onLowMemory: ");
        Glide.with(this).onLowMemory();
    }

    protected void setToolbarBackEnable() {
        Log.i(TAG, "setToolbarBackEnable: ");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void setToolbarTitle(String title) {
        Log.i(TAG, "setToolbarTitle: ");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
        if (toolbarLayout != null) {
            toolbarLayout.setTitle(title);
        }
    }

    protected void setToolbarTitle(String title, String subTitle) {
        Log.i(TAG, "setToolbarTitle: ");
        setToolbarTitle(title);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(subTitle);
        }
    }

    protected void setToolbarSubTitle(String subTitle) {
        Log.i(TAG, "setToolbarSubTitle: ");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(subTitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected: ");
        if (item.getItemId() == android.R.id.home) {
            finishActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void finishActivity() {
        Log.i(TAG, "finishActivity: ");
        finish();
    }

    protected Fragment getVisibleFragment() {
        Log.i(TAG, "getVisibleFragment: ");
        @SuppressLint("RestrictedApi")
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null) {
            for (Fragment fragment : fragmentList) {
                if (fragment != null && fragment.isVisible()) {
                    return fragment;
                }
            }
        }
        return null;
    }

    @Override
    public void showLoading() {
        Log.i(TAG, "showLoading: ");
    }

    @Override
    public void hideLoading() {
        Log.i(TAG, "hideLoading: ");
    }

    @Override
    public void showProgressDialog(String content) {
        Log.i(TAG, "showProgressDialog: ");
        getProgressDialog(content);
        mProgressDialog.show();
    }

    @Override
    public void dismissProgressDialog() {
        Log.i(TAG, "dismissProgressDialog: ");
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        } else {
            throw new NullPointerException("dismissProgressDialog: can't dismiss a null dialog, must showForRepo dialog first!");
        }
    }

    @Override
    public ProgressDialog getProgressDialog(String content) {
        Log.i(TAG, "getProgressDialog: ");
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.setMessage(content);
        return mProgressDialog;
    }

    @Override
    public void showToast(String message) {
        Log.i(TAG, "showToast: ");
        Toasty.normal(getActivity(), message).show();
    }

    @Override
    public void showInfoToast(String message) {
        Log.i(TAG, "showInfoToast: ");
        Toasty.info(getActivity(), message).show();
    }

    @Override
    public void showSuccessToast(String message) {
        Log.i(TAG, "showSuccessToast: ");
        Toasty.success(getActivity(), message).show();
    }

    @Override
    public void showErrorToast(String message) {
        Log.i(TAG, "showErrorToast: ");
        Toasty.error(getActivity(), message).show();
    }

    @Override
    public void showWarningToast(String message) {
        Log.i(TAG, "showWarningToast: ");
        Toasty.warning(getActivity(), message).show();
    }

    @Override
    public void showTipDialog(String content) {
        Log.i(TAG, "showTipDialog: ");
        new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setTitle("提示")
                .setMessage(content)
                .setCancelable(true)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    @Override
    public void showConfirmDialog(String msn, String title, String confirmText
            , DialogInterface.OnClickListener confirmListener) {
        Log.i(TAG, "showConfirmDialog: ");
        new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setTitle(title)
                .setMessage(msn)
                .setCancelable(true)
                .setPositiveButton(confirmText, confirmListener)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    protected void postDelayFinish(int time) {
        Log.i(TAG, "postDelayFinish: ");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, time);
    }

    public static BaseActivity getCurActivity() {
        Log.i(TAG, "getCurActivity: ");
        return curActivity;
    }


    @NonNull
    protected BaseActivity getActivity() {
        Log.i(TAG, "getActivity: ");
        return this;
    }

    @NonNull
    protected AppApplication getAppApplication() {
        Log.i(TAG, "getAppApplication: ");
        return (AppApplication) getApplication();
    }

    protected AppComponent getAppComponent() {
        Log.i(TAG, "getAppComponent: ");
        return getAppApplication().getAppComponent();
    }

//    protected DaoSession getDaoSession() {
//        Log.i("=============>","43");
//        return getAppComponent().getDaoSession();
//    }

    public void onRefreshWebPage() {
        Log.i(TAG, "onRefreshWebPage: ");
    }

    /**
     * @param id  viewId
     * @param <T> View
     * @return View
     * @Description: 优化activity的方法，添加类型自动转换
     * @author: Yuyunhao
     */
    @Nullable
    protected <T extends View> T findViewByViewId(@IdRes int id) {
        return (T) findViewById(id);
    }

    private void getScreenSize() {
        Log.i(TAG, "getScreenSize: ");
        if (WindowUtil.screenHeight == 0 ||
                WindowUtil.screenWidth == 0) {
            Display display = getWindowManager().getDefaultDisplay();
            WindowUtil.screenWidth = display.getWidth();
            WindowUtil.screenHeight = display.getHeight();
        }
    }

    public String getAppVersionName() {
        Log.i(TAG, "getAppVersionName: ");
        String versionName = "";
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 引用资源文件中的字符串
     *
     * @param strId
     * @see [类、类#方法、类#成员]
     */
    @NonNull
    protected String getResuceString(int strId) {
        Log.i(TAG, "getResuceString: ");
        return getResources().getString(strId);
    }

    protected void delayFinish() {
        delayFinish(1000);
    }

    protected void delayFinish(int mills) {
        Log.i(TAG, "delayFinish: ");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, mills);
    }

    protected void setToolbarScrollAble(boolean scrollAble) {
        Log.i(TAG, "setToolbarScrollAble: ");
        if (toolbar == null) return;
        int flags = scrollAble ? (AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                | AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP) : 0;
        AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        layoutParams.setScrollFlags(flags);
        toolbar.setLayoutParams(layoutParams);
    }

    protected void setTransparentStatusBar() {
        Log.i(TAG, "setTransparentStatusBar: ");
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }


    @Override
    public void showLoginPage() {
        Log.i(TAG, "showLoginPage: ");
        getActivity().finishAffinity();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    public boolean isAlive() {
        Log.i(TAG, "isAlive: ");
        return isAlive;
    }

    @AutoAccess
    boolean isFullScreen = false;

    protected void exitFullScreen() {
        Log.i(TAG, "exitFullScreen: ");
        showStatusBar();
        if (toolbar != null) toolbar.setVisibility(View.VISIBLE);
        isFullScreen = false;
    }

    protected void intoFullScreen() {
        Log.i(TAG, "intoFullScreen: ");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (toolbar != null) toolbar.setVisibility(View.GONE);
        isFullScreen = true;
    }

    private void showStatusBar() {
        Log.i(TAG, "showStatusBar: ");
        final WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(attrs);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed: ");
        if (isFullScreen) {
            exitFullScreen();
        } else {
            super.onBackPressed();
        }
    }
}