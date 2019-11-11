

package com.hong.mvp.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;


import com.google.gson.Gson;
import com.hong.AppApplication;
import com.hong.AppConfig;
import com.hong.AppData;
import com.hong.dao.AuthUser;
import com.hong.dao.AuthUserDao;
import com.hong.dao.DaoSession;
import com.hong.http.core.HttpObserver;
import com.hong.http.core.HttpResponse;
import com.hong.http.core.HttpSubscriber;
import com.hong.http.model.AuthRequestModel;
import com.hong.http.model.UMenu;
import com.hong.mvp.contract.ILoginContract;
import com.hong.mvp.model.BasicToken;
import com.hong.mvp.model.OauthToken;
import com.hong.mvp.model.User;
import com.hong.mvp.presenter.base.BasePresenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import javax.inject.Inject;

import okhttp3.Credentials;
import retrofit2.Response;
import rx.Observable;

/**
 * Created on 2019/5/12.
 *
 * @author upc_jxzy
 */

public class LoginPresenter extends BasePresenter<ILoginContract.View>
        implements ILoginContract.Presenter {

    @Inject
    public LoginPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void getToken(String code, String state) {
        Observable<Response<OauthToken>> observable = getLoginService().getAccessToken(AppConfig.OPENHUB_CLIENT_ID, AppConfig.OPENHUB_CLIENT_SECRET, code, state);
        HttpSubscriber<OauthToken> subscriber =
                new HttpSubscriber<>(
                        new HttpObserver<OauthToken>() {
                            @Override
                            public void onError(@NonNull Throwable error) {
                                Log.i("=============>", "62 - LoginPresenter onError+++++++++++++");
                                mView.dismissProgressDialog();
                                mView.showErrorToast(getErrorTip(error));
                            }

                            @Override
                            public void onSuccess(@NonNull HttpResponse<OauthToken> response) {
                                OauthToken token = response.body();
                                if (token != null) {
                                    Log.i("=============>", "61 - LoginPresenter onSuccess+++++++++++++");
                                    mView.onGetTokenSuccess(BasicToken.generateFromOauthToken(token));
                                } else {
                                    Log.i("=============>", "61 - LoginPresenter onSuccess-------------");
                                    mView.onGetTokenError(response.getOriResponse().message());
                                }
                            }
                        }
                );
        generalRxHttpExecute(observable, subscriber);
        mView.showProgressDialog(getLoadTip());
    }

    @NonNull
    @Override
    public String getOAuth2Url() {
        String randomState = UUID.randomUUID().toString();
        return AppConfig.OAUTH2_URL +
                "?client_id=" + AppConfig.OPENHUB_CLIENT_ID +
                "&scope=" + AppConfig.OAUTH2_SCOPE +
                "&state=" + randomState;
    }

    @Override
    public void basicLogin(String userName, String password) {
        AuthRequestModel authRequestModel = AuthRequestModel.generate();
        String token = Credentials.basic(userName, password);
        Log.i("==========", "basicLogin: __"+token);

        Observable<Response<BasicToken>> observable =
                getLoginService(token).authorizations(authRequestModel);
        HttpSubscriber<BasicToken> subscriber = new HttpSubscriber<>(
                new HttpObserver<BasicToken>() {
                    @Override
                    public void onError(@NonNull Throwable error) {
                        //在这里调用就不能重置
                        Log.i("============>", "onError: token______"+error);
                        mView.onGetTokenError(getErrorTip(error));
                    }

                    @Override
                    public void onSuccess(@NonNull HttpResponse<BasicToken> response) {
                        Log.i("====", "onSuccess: 登录成功___"+response+"___"+response.toString()+"_____"+response.body());
                        BasicToken token = response.body();
                        if (token != null) {
                            Log.i("============>", "onSuccess: token+++++++++++" + token.getToken());
                            mView.onGetTokenSuccess(token);
                        } else {
                            //在这里调用就不能重置
                            Log.i("============>", "onSuccess: token null");
                            mView.onGetTokenError("密码错误");
                        }

                    }
                }
        );
        generalRxHttpExecute(observable, subscriber);
    }

    @Override
    public void handleOauth(Intent intent) {
        Uri uri = intent.getData();
        if (uri != null) {
            String code = uri.getQueryParameter("code");
            String state = uri.getQueryParameter("state");
            getToken(code, state);
        }
    }

    @Override
    public void getUserInfo(final BasicToken basicToken) {
        Log.i("____", "getUserInfo: 查询用户_____");
        HttpSubscriber<User> subscriber = new HttpSubscriber<>(
                new HttpObserver<User>() {
                    @Override
                    public void onError(Throwable error) {
                        Log.i("============>", "getUserInfo onError: token-----------");
//                        mView.dismissProgressDialog();
                        mView.showErrorToast(getErrorTip(error));
                    }

                    @Override
                    public void onSuccess(HttpResponse<User> response) {
                        Log.i("============>", "getUserInfo onSuccess: token+++++++++++" + response.body().toString() + response.body().getEmail());
//                        mView.dismissProgressDialog();
                        Log.i("===============", "onSuccess: ________________________-___--"+ AppData.INSTANCE.getAccessToken());
                        saveAuthUser(basicToken, response.body());
                        mView.onLoginComplete();
                    }
                }
        );
        Observable<Response<User>> observable = getUserService(basicToken.getToken()).
                getPersonInfo(true);
        generalRxHttpExecute(observable, subscriber);
//        mView.showProgressDialog(getLoadTip());

    }

    @Override
    public void getMenu(String username) {
        Log.i("----", "getMenu: 根据username请求______________");
        HttpSubscriber<HashMap<String,ArrayList<UMenu>>> subscriber = new HttpSubscriber<>(
                new HttpObserver<HashMap<String,ArrayList<UMenu>>>() {
                    @Override
                    public void onError(Throwable error) {
                        Log.i("============>", "getMenu 根据username请求onError: _)___----------");
                        mView.dismissProgressDialog();
                        mView.showErrorToast(getErrorTip(error));
                    }

                    @Override
                    public void onSuccess(HttpResponse<HashMap<String,ArrayList<UMenu>>> response) {
                        Log.i("============>", "getMenu 根据username请求onSuccess: +++++++++" + response.body().toString() );
                        ArrayList<UMenu> rows = response.body().get("rows");
                        AppData.menus = rows;
//                        if(rows.size() > 0){
//                            SharedPreferences sp = getContext().getSharedPreferences("menu", Context.MODE_PRIVATE);
//                            SharedPreferences.Editor edit = sp.edit();
//                            Gson menuGson = new Gson();
//                            edit.putString("MENU_KEY",menuGson.toJson(rows));
//                            edit.commit();
//                        }
                    }
                }
        );
        Observable<Response<HashMap<String, ArrayList<UMenu>>>> observable = getLoginService().getMenu(username);
        generalRxHttpExecute(observable, subscriber);
        mView.showProgressDialog(getLoadTip());
    }

//    public void getMenu(BasicToken basicToken) {
//        Log.i("----", "getMenu: 请求______________");
//        HttpSubscriber<HashMap<String,ArrayList<UMenu>>> subscriber = new HttpSubscriber<>(
//                new HttpObserver<HashMap<String,ArrayList<UMenu>>>() {
//                    @Override
//                    public void onError(Throwable error) {
//                        Log.i("============>", "getMenu onError: token-__)___----------");
//                        mView.dismissProgressDialog();
//                        mView.showErrorToast(getErrorTip(error));
//                    }
//
//                    @Override
//                    public void onSuccess(HttpResponse<HashMap<String,ArrayList<UMenu>>> response) {
//                        Log.i("============>", "getMenu onSuccess: token+++++++++++" + response.body().toString() );
//                        ArrayList<UMenu> rows = response.body().get("rows");
//                        AppData.menus = rows;
//
////                        for (UMenu u : rows){
////                            System.out.println(u.getMurl()+"///"+u.getMname());
////                        }
//                    }
//                }
//        );
////        Observable<Response<HashMap<String, ArrayList<UMenu>>>> observable = getUserService(basicToken.getToken()).
////                getMenu(true,1);
////        generalRxHttpExecute(observable, subscriber);
////        mView.showProgressDialog(getLoadTip());
//
//    }

    private void saveAuthUser(BasicToken basicToken, User userInfo) {
        String updateSql = "UPDATE " + daoSession.getAuthUserDao().getTablename() + " SET " + AuthUserDao.Properties.Selected.columnName + " = 0";
        daoSession.getAuthUserDao().getDatabase().execSQL(updateSql);
        String deleteExistsSql = "DELETE FROM " + daoSession.getAuthUserDao().getTablename() + " WHERE " + AuthUserDao.Properties.LoginId.columnName + " = '" + userInfo.getLogin() + "'";
        daoSession.getAuthUserDao().getDatabase().execSQL(deleteExistsSql);
        AuthUser authUser = new AuthUser();
        Log.i("============>", "saveAuthUser0" + basicToken.getScopes());
//        String scope = StringUtils.listToString(basicToken.getScopes(), ",");
        String scope = "user, repo, gist, notifications";
        Log.i("============>", "saveAuthUser scope" + scope);
        Date date = new Date();
        Log.i("============>", "saveAuthUser00[]" + basicToken.getToken());
        authUser.setAccessToken(basicToken.getToken());
        //authUser.setAccessToken("basicToken...");
        authUser.setScope(scope);
        authUser.setAuthTime(date);
        authUser.setExpireIn(360 * 24 * 60 * 60);
        authUser.setSelected(true);
        authUser.setLoginId(userInfo.getLogin());
        Log.i("============>", "saveAuthUser000");
        authUser.setName(userInfo.getName());
        authUser.setAvatar(userInfo.getAvatarUrl());
        daoSession.getAuthUserDao().insert(authUser);
        Log.i("============>", "saveAuthUser1");
        AppData.INSTANCE.setAuthUser(authUser);
        AppData.INSTANCE.setLoggedUser(userInfo);
        Log.i("============>", "saveAuthUser2");
    }


}
