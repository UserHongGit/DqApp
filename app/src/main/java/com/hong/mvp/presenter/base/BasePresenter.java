package com.hong.mvp.presenter.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.hong.AppConfig;
import com.hong.AppData;
import com.hong.R;
import com.hong.common.AppEventBus;
import com.hong.dao.DaoSession;
import com.hong.http.IssueService;
import com.hong.http.LoginService;
import com.hong.http.NotificationsService;
import com.hong.http.RepoService;
import com.hong.http.SearchService;
import com.hong.http.UserService;
import com.hong.http.ViewerService;
import com.hong.http.core.AppRetrofit;
import com.hong.http.core.HttpObserver;
import com.hong.http.core.HttpProgressSubscriber;
import com.hong.http.core.HttpResponse;
import com.hong.http.core.HttpSubscriber;
import com.hong.http.error.HttpError;
import com.hong.http.error.HttpErrorCode;
import com.hong.http.error.HttpPageNoFoundError;
import com.hong.http.error.UnauthorizedError;
import com.hong.mvp.contract.base.IBaseContract;
import com.hong.util.NetHelper;
import com.hong.util.PrefUtils;
import com.hong.util.StringUtils;
import com.orhanobut.logger.Logger;
import com.thirtydegreesray.dataautoaccess.DataAutoAccess;

import org.apache.http.conn.ConnectTimeoutException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import rx.android.schedulers.AndroidSchedulers;
/**
 * BasePresenter
 * Created by ThirtyDegreesRay on 2016/7/13 18:01
 */
public abstract class BasePresenter<V extends IBaseContract.View> implements IBaseContract.Presenter<V> {

    private final String TAG = "BasePresenter";

    //View
    protected V mView;
    //db Dao
    protected DaoSession daoSession;

    private ArrayList<Subscriber<?>> subscribers;
    private boolean isEventSubscriber = false;
    private boolean isViewInitialized = false;

    private boolean isAttached = false;

    public BasePresenter(DaoSession daoSession) {
        this.daoSession = daoSession;
        subscribers = new ArrayList<>();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        DataAutoAccess.saveData(this, outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle outState) {
        if (outState == null) return;
        DataAutoAccess.getData(this, outState);
    }

    /**
     * 绑定View
     *
     * @param view view
     */
    @Override
    public void attachView(@NonNull V view) {
        mView = view;
        if (isEventSubscriber) AppEventBus.INSTANCE.getEventBus().register(this);
        onViewAttached();
        isAttached = true;
    }

    /**
     * 取消View绑定
     */
    @Override
    public void detachView() {
        mView = null;
        //view 取消绑定时，把请求取消订阅
        for (Subscriber subscriber : subscribers) {
            if (subscriber != null && !subscriber.isUnsubscribed()) {
                subscriber.unsubscribe();
                Logger.d(TAG, "unsubscribe:" + subscriber.toString());
            }
        }
        if (isEventSubscriber) AppEventBus.INSTANCE.getEventBus().unregister(this);
    }

    @Override
    public void onViewInitialized() {
        isViewInitialized = true;
    }

    protected boolean isViewInitialized() {
        return isViewInitialized;
    }

    /**
     * Retrofit
     *
     * @return Retrofit
     */


    protected LoginService getLoginService() {
        return AppRetrofit.INSTANCE
                .getRetrofit(AppConfig.GITHUB_BASE_URL, null)
                .create(LoginService.class);
    }

    protected LoginService getLoginService(String token) {
        return AppRetrofit.INSTANCE
                .getRetrofit(AppConfig.UPC_API_BASE_URL, token)
                .create(LoginService.class);
    }

    protected UserService getUserService(String token) {
        Log.i(TAG, "getUserService: _________");
        return AppRetrofit.INSTANCE
                .getRetrofit(AppConfig.UPC_API_BASE_URL, token)
                .create(UserService.class);
    }

    protected UserService getUserService() {
        return getUserService(AppData.INSTANCE.getAccessToken());
    }

    protected RepoService getRepoService() {
        return getServices(RepoService.class);
    }

    protected SearchService getSearchService() {
        return getServices(SearchService.class);
    }

    protected ViewerService getViewerService(String token) {
        return AppRetrofit.INSTANCE
                .getRetrofit(AppConfig.UPC_API_BASE_URL, token)
                .create(ViewerService.class);
    }

    protected IssueService getIssueService() {
        return getServices(IssueService.class);
    }


    protected NotificationsService getNotificationsService() {
        return getServices(NotificationsService.class);
    }


    private <T> T getServices(Class<T> serviceClass){
        return getServices(serviceClass, AppConfig.UPC_API_BASE_URL, true);
    }

    protected  <T> T getServices(Class<T> serviceClass, String baseUrl, boolean isJson){
        return AppRetrofit.INSTANCE
                .getRetrofit(baseUrl, AppData.INSTANCE.getAccessToken(), isJson)
                .create(serviceClass);
    }

    /**
     * 获取上下文，需在onViewAttached()后调用
     *
     * @return
     */
    @NonNull
    @Override
    public Context getContext() {
        if (mView instanceof Context) {
            return (Context) mView;
        } else if (mView instanceof Fragment) {
            return ((Fragment) mView).getContext();
        } else {
            throw new NullPointerException("BasePresenter:mView is't instance of Context,can't use getContext() method.");
        }
    }

    /**
     * presenter和view绑定成功
     */
    @CallSuper
    protected void onViewAttached() {

    }

    protected interface IObservableCreator<T> {
        Observable<Response<T>> createObservable(boolean forceNetWork);
    }


    /**
     * 一般的rx http请求执行
     *
     * @param observable
     * @param subscriber null 表明不管数据回调
     * @param <T>
     */
    protected <T> void generalRxHttpExecute(
            @NonNull Observable<Response<T>> observable, @Nullable HttpSubscriber<T> subscriber) {

        if (subscriber != null) {
            subscribers.add(subscriber);
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        } else {
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new HttpSubscriber<T>());
        }
    }

    protected <T> void generalRxHttpExecute(@NonNull IObservableCreator<T> observableCreator
            , @NonNull HttpObserver<T> httpObserver) {
        generalRxHttpExecute(observableCreator, httpObserver, false);
    }

    protected <T> void generalRxHttpExecute(@NonNull IObservableCreator<T> observableCreator
            , @NonNull HttpObserver<T> httpObserver, final boolean readCacheFirst) {
        generalRxHttpExecute(observableCreator, httpObserver, readCacheFirst, null);
    }

    //防止死循环
    private Map<String, Integer> requestTimesMap = new HashMap<>();

    protected <T> void generalRxHttpExecute(@NonNull final IObservableCreator<T> observableCreator
            , @NonNull final HttpObserver<T> httpObserver, final boolean readCacheFirst
            , @Nullable final ProgressDialog progressDialog) {
        requestTimesMap.put(observableCreator.toString(), 1);

        final HttpObserver<T> tempObserver = new HttpObserver<T>() {
            @Override
            public void onError(Throwable error) {
                if(!checkIsUnauthorized(error)){
                    httpObserver.onError(error);
                }
            }

            @Override
            public void onSuccess(@NonNull HttpResponse<T> response) {
                if (response.isSuccessful()) {
                    if (readCacheFirst && response.isFromCache()
                            && NetHelper.INSTANCE.getNetEnabled()
                            && requestTimesMap.get(observableCreator.toString()) < 2) {
                        requestTimesMap.put(observableCreator.toString(), 2);
                        generalRxHttpExecute(observableCreator.createObservable(true),
                                getHttpSubscriber(this, progressDialog));
                    }
                    httpObserver.onSuccess(response);
                } else if(response.getOriResponse().code() == 404){
                    onError(new HttpPageNoFoundError());
                } else if(response.getOriResponse().code() == 504){
                    onError(new HttpError(HttpErrorCode.NO_CACHE_AND_NETWORK));
                } else if(response.getOriResponse().code() == 401){
                    onError(new UnauthorizedError());
                } else {
                    onError(new Error(response.getOriResponse().message()));
                }

            }
        };

        boolean cacheFirstEnable = PrefUtils.isCacheFirstEnable();
//        cacheFirstEnable = cacheFirstEnable || !NetHelper.INSTANCE.getNetEnabled();
        generalRxHttpExecute(observableCreator.createObservable(!cacheFirstEnable || !readCacheFirst),
                getHttpSubscriber(tempObserver, progressDialog));
    }

    private <T> HttpSubscriber<T> getHttpSubscriber(HttpObserver<T> httpObserver, ProgressDialog progressDialog) {
        if(progressDialog == null)
            return new HttpSubscriber<>(httpObserver);
        else
            return new HttpProgressSubscriber<>(progressDialog, httpObserver);
    }

    private boolean checkIsUnauthorized(Throwable error){
        if(error instanceof UnauthorizedError){
            mView.showErrorToast(error.getMessage());
            daoSession.getAuthUserDao().delete(AppData.INSTANCE.getAuthUser());
            AppData.INSTANCE.setAuthUser(null);
            AppData.INSTANCE.setLoggedUser(null);
            mView.showLoginPage();
            return true;
        }
        return false;
    }

    public void rxDBExecute(@NonNull Runnable runnable){
        daoSession.rxTx().run(runnable).subscribe();
    }


    @NonNull
    protected String getLoadTip() {
        return getContext().getString(R.string.loading).concat("...");
    }

    protected boolean isLastResponse(@NonNull HttpResponse response) {
        return response.isFromNetWork() ||
                !NetHelper.INSTANCE.getNetEnabled();
    }

    @NonNull
    protected String getErrorTip(@NonNull Throwable error) {
        String errorTip = null;
        if(error == null){
            return errorTip;
        }
        if(error instanceof UnknownHostException){
            errorTip = getString(R.string.no_network_tip);
        } else if (error instanceof SocketTimeoutException || error instanceof ConnectTimeoutException) {
            errorTip = getString(R.string.load_timeout_tip);
        } else if (error instanceof HttpError) {
            errorTip = error.getMessage();
        } else {
            errorTip = StringUtils.isBlank(error.getMessage()) ? error.toString() : error.getMessage();
        }
        return errorTip;
    }


    @NonNull
    protected String getString(@StringRes int resId) {
        return getContext().getResources().getString(resId);
    }

    public void setEventSubscriber(boolean eventSubscriber) {
        isEventSubscriber = eventSubscriber;
        if(isEventSubscriber && isAttached && !AppEventBus.INSTANCE.getEventBus().isRegistered(this)){
            AppEventBus.INSTANCE.getEventBus().register(this);
        }
    }

    protected void executeSimpleRequest(@NonNull final Observable<Response<ResponseBody>> observable) {
        HttpObserver<ResponseBody> httpObserver = new HttpObserver<ResponseBody>() {
            @Override
            public void onError(Throwable error) {
                mView.showErrorToast(getErrorTip(error));
            }

            @Override
            public void onSuccess(HttpResponse<ResponseBody> response) {
            }
        };
        generalRxHttpExecute(new IObservableCreator<ResponseBody>() {
            @Override
            public Observable<Response<ResponseBody>> createObservable(boolean forceNetWork) {
                return observable;
            }
        }, httpObserver);
    }

    protected void checkStatus(@NonNull Observable<Response<ResponseBody>> observable,
                               @NonNull final CheckStatusCallback callback) {
        HttpSubscriber<ResponseBody> httpSubscriber = new HttpSubscriber<>(
                new HttpObserver<ResponseBody>() {
                    @Override
                    public void onError(Throwable error) {
                    }

                    @Override
                    public void onSuccess(HttpResponse<ResponseBody> response) {
                        callback.onChecked(response.isSuccessful());
                    }
                }
        );
        generalRxHttpExecute(observable, httpSubscriber);
    }

    protected interface CheckStatusCallback {
        void onChecked(boolean status);
    }


}
