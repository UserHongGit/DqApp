

package com.hong.mvp.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;


import com.google.gson.Gson;
import com.hong.AppData;
import com.hong.dao.AuthUser;
import com.hong.dao.AuthUserDao;
import com.hong.dao.DaoSession;
import com.hong.http.core.HttpObserver;
import com.hong.http.core.HttpResponse;
import com.hong.http.core.HttpSubscriber;
import com.hong.http.model.UMenu;
import com.hong.mvp.contract.IWebContract;
import com.hong.mvp.model.BasicToken;
import com.hong.mvp.model.User;
import com.hong.mvp.presenter.base.BasePresenter;
import com.hong.util.PrefUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created on 2019/5/18.
 *
 * @author upc_jxzy
 */

public class WebPresenter extends BasePresenter<IWebContract.View>
        implements IWebContract.Presenter{

    @Inject
    public WebPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public boolean isFirstUseAndNoNewsUser() {
        User user = AppData.INSTANCE.getLoggedUser();
        if(user.getFollowing() == 0
                && user.getPublicRepos() == 0 && user.getPublicGists() == 0
                && PrefUtils.isFirstUse()){
            PrefUtils.set(PrefUtils.FIRST_USE, false);
            return true;
        }
        return false;
    }

    @Override
    public List<AuthUser> getLoggedUserList() {
        List<AuthUser> users = daoSession.getAuthUserDao().loadAll();
        if(users != null){
            for(AuthUser user : users){
                if(AppData.INSTANCE.getLoggedUser().getLogin().equals(user.getLoginId())){
                    users.remove(user);
                    break;
                }
            }
        }
        return users;
    }


    @Override
    public void getMenu(String token) {
        Log.i("----", "getMenu: 请求______________");
        HttpSubscriber<HashMap<String, ArrayList<UMenu>>> subscriber = new HttpSubscriber<>(
                new HttpObserver<HashMap<String,ArrayList<UMenu>>>() {
                    @Override
                    public void onError(Throwable error) {
                        Log.i("============>", "getMenu onError: token-----------");
                        mView.showErrorToast(getErrorTip(error));
                    }

                    @Override
                    public void onSuccess(HttpResponse<HashMap<String,ArrayList<UMenu>>> response) {
                        Log.i("============>", "getMenu onSuccess: token+++++++++++" + response.body().toString() );
                        ArrayList<UMenu> rows = response.body().get("rows");

                        AppData.isLogin = false;
                        mView.renderMenu(rows);
                    }
                }
        );
        Observable<Response<HashMap<String, ArrayList<UMenu>>>> observable = getUserService(token).
                getMenu(token);
        generalRxHttpExecute(observable, subscriber);

    }


    @Override
    public void toggleAccount(@NonNull String loginId) {
        String removeSelectSql = "UPDATE " + daoSession.getAuthUserDao().getTablename()
                + " SET " + AuthUserDao.Properties.Selected.columnName + " = 0 "
                + " WHERE " + AuthUserDao.Properties.LoginId.columnName
                + " ='" + AppData.INSTANCE.getLoggedUser().getLogin() + "'";
        String selectSql = "UPDATE " + daoSession.getAuthUserDao().getTablename()
                + " SET " + AuthUserDao.Properties.Selected.columnName + " = 1"
                + " WHERE " + AuthUserDao.Properties.LoginId.columnName
                + " ='" + loginId + "'";
        daoSession.getAuthUserDao().getDatabase().execSQL(removeSelectSql);
        daoSession.getAuthUserDao().getDatabase().execSQL(selectSql);
        AppData.INSTANCE.setAuthUser(null);
        AppData.INSTANCE.setLoggedUser(null);
        mView.restartApp();
    }

    @Override
    public void logout() {

        daoSession.getAuthUserDao().delete(AppData.INSTANCE.getAuthUser());
        AppData.INSTANCE.setAuthUser(null);
        AppData.INSTANCE.setLoggedUser(null);
        mView.restartApp();
    }

}
