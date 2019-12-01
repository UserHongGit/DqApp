

package com.hong.mvp.presenter;

import android.support.annotation.NonNull;
import android.util.Log;


import com.hong.AppData;
import com.hong.dao.AuthUser;
import com.hong.dao.AuthUserDao;
import com.hong.dao.DaoSession;
import com.hong.http.core.HttpObserver;
import com.hong.http.core.HttpResponse;
import com.hong.mvp.contract.ISplashContract;
import com.hong.mvp.model.User;
import com.hong.mvp.presenter.base.BasePresenter;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * 闪屏页面
 * @author upc_jxzy
 */

public class SplashPresenter extends BasePresenter<ISplashContract.View> implements ISplashContract.Presenter
{

    private final String TAG = "SplashPresenter";

    private AuthUser authUser;
    private boolean isMainPageShowwed = false;

    @Inject
    public SplashPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void getUser() {
        Log.i("=============>","SplashPresenter getUser");
        AuthUserDao authUserDao = daoSession.getAuthUserDao();
        List<AuthUser> users = authUserDao.queryBuilder()
                .where(AuthUserDao.Properties.Selected.eq(true))
                .limit(1)
                .list();

        AuthUser selectedUser = users != null && users.size() > 0 ? users.get(0) : null;

        //if none selected, choose first account
        if(selectedUser == null){
            List<AuthUser> firstAccount = authUserDao.queryBuilder()
                    .limit(1)
                    .list();
            selectedUser = firstAccount != null && firstAccount.size() > 0 ? firstAccount.get(0) : null;
        }

        if (selectedUser != null && isExpired(selectedUser)) {
            authUserDao.delete(selectedUser);
            selectedUser = null;
        }
        if (selectedUser != null) {
            AppData.INSTANCE.setAuthUser(selectedUser);
            getUserInfo(selectedUser.getAccessToken());
        } else {
            mView.showLoginPage();
        }

    }

    @Override
    public void saveAccessToken(String accessToken, String scope, int expireIn) {
        AuthUser authUser = new AuthUser();
        authUser.setSelected(true);
        authUser.setScope(scope);
        authUser.setExpireIn(expireIn);
        authUser.setAuthTime(new Date());
        authUser.setAccessToken(accessToken);
        daoSession.getAuthUserDao().insert(authUser);
        this.authUser = authUser;
    }

    private void getUserInfo(final String accessToken) {
        Log.i("=============>","SplashPresenter getUserInfo+accessToken="+accessToken);
        HttpObserver<User> httpObserver = new HttpObserver<User>() {
            @Override
            public void onError(@NonNull Throwable error) {
                Log.i(TAG, "闪屏页获取用户信息失败!!!"+error);
                daoSession.getAuthUserDao().delete(AppData.INSTANCE.getAuthUser());
                AppData.INSTANCE.setAuthUser(null);
                mView.showErrorToast(getErrorTip(error));
                mView.showLoginPage();
            }

            @Override
            public void onSuccess(@NonNull HttpResponse<User> response) {
                Log.i(TAG, "闪屏页获取用户信息成功!!!"+response.body().getId());
                AppData.INSTANCE.setLoggedUser(response.body());
                if (authUser != null) {
                    authUser.setLoginId(response.body().getLogin());
                    daoSession.getAuthUserDao().update(authUser);
                }
                if(!isMainPageShowwed) {
                    isMainPageShowwed = true;
                    mView.showMainPage();
                }
            }
        };

        generalRxHttpExecute(new IObservableCreator<User>() {
            @Override
            public Observable<Response<User>> createObservable(boolean forceNetWork) {
                return getUserService().getPersonInfo(forceNetWork);
            }
        }, httpObserver, true);

    }

    private boolean isExpired(AuthUser selectedUser){
        return selectedUser.getAuthTime().getTime() + selectedUser.getExpireIn() * 1000
                < System.currentTimeMillis();
    }

}
