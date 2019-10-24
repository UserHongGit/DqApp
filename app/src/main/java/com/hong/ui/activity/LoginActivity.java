

package com.hong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.hong.R;
import com.hong.inject.component.AppComponent;
import com.hong.inject.component.DaggerActivityComponent;
import com.hong.inject.module.ActivityModule;
import com.hong.mvp.contract.ILoginContract;
import com.hong.mvp.model.BasicToken;
import com.hong.mvp.presenter.LoginPresenter;
import com.hong.ui.activity.base.BaseActivity;
import com.hong.util.StringUtils;
import com.hong.util.ViewUtils;
import com.unstoppable.submitbuttonview.SubmitButton;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * Created on 2019/5/12.
 *
 * @author upc_jxzy
 */

public class LoginActivity extends BaseActivity<LoginPresenter> implements ILoginContract.View {

    private final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.user_name_et)
    TextInputEditText userNameEt;
    @BindView(R.id.user_name_layout)
    TextInputLayout userNameLayout;
    @BindView(R.id.password_et)
    TextInputEditText passwordEt;
    @BindView(R.id.password_layout)
    TextInputLayout passwordLayout;
    @BindView(R.id.login_bn)
    SubmitButton loginBn;

    private String userName;
    private String password;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mPresenter.handleOauth(intent);
        setIntent(null);
    }

    @Override
    public void onGetTokenSuccess(BasicToken basicToken) {
        Log.i("============>", "LoginActivity onGetTokenSuccess");
        loginBn.doResult(true);
//        mPresenter.getMenu(basicToken);
        mPresenter.getUserInfo(basicToken);

    }

    @Override
    public void onGetTokenError(String errorMsg) {
        Log.i(TAG, "onGetTokenError: ____1111111_______"+loginBn);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loginBn.doResult(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loginBn.reset();
                        loginBn.setEnabled(true);
                    }
                }, 1000);
            }
        });
        Log.i(TAG, "onGetTokenError-: ______2222222_____"+loginBn);
//        Toasty.error(getApplicationContext(), errorMsg).show();
    }

    /**
     * 登录完成之后去主页面
     */
    @Override
    public void onLoginComplete() {
        delayFinish();
        Log.i(TAG, "onLoginComplete: _____用户的id____"+userNameEt.getText().toString());
//        mPresenter.getMenu(userNameEt.getText().toString());
//        startActivity(new Intent(getActivity(), MainActivity.class));
        startActivity(new Intent(getActivity(), WebActivity.class));
    }

    /**
     * 依赖注入的入口
     *
     * @param appComponent appComponent
     */
    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
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
        return R.layout.activity_login;
    }

    /**
     * 初始化view
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        loginBn.setOnResultEndListener(new SubmitButton.OnResultEndListener() {
            @Override
            public void onResultEnd() {

            }
        });


        passwordEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEND
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                        event.getAction() == KeyEvent.ACTION_DOWN)){
                    if(loginBn.isEnabled())
                        ViewUtils.virtualClick(loginBn);
                }
                return false;
            }
        });

    }


    @OnClick(R.id.oauth_login_bn)
    public void onOauthLoginClick(){
//        AppOpener.openInCustomTabsOrBrowser(getActivity(), mPresenter.getOAuth2Url());
    }

    //登录按钮
    @OnClick(R.id.login_bn)
    public void onLoginClick(){
        if(loginCheck()){
            loginBn.setEnabled(false);
            mPresenter.basicLogin(userName, password);
        }else{
            loginBn.reset();
        }
    }

    private boolean loginCheck(){
        boolean valid = true;
        userName = userNameEt.getText().toString();
        password = passwordEt.getText().toString();
        if(StringUtils.isBlank(userName)){
            valid = false;
            userNameLayout.setError(getString(R.string.user_name_warning));
        }else{
            userNameLayout.setErrorEnabled(false);
        }
        if(StringUtils.isBlank(password)){
            valid = false;
            passwordLayout.setError(getString(R.string.password_warning));
        }else{
            passwordLayout.setErrorEnabled(false);
        }
        return valid;
    }
}
