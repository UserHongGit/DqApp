//package com.upc.mvp.presenter.base;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.annotation.StringRes;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//
//import com.thirtydegreesray.dataautoaccess.DataAutoAccess;
//import com.upc.AppConfig;
//import com.upc.AppData;
//import com.upc.R;
//import com.upc.common.AppEventBus;
//import com.upc.dao.DaoSession;
//import com.upc.http.GxlrService;
//import com.upc.http.IssueService;
//import com.upc.http.LoginService;
//import com.upc.http.NotificationsService;
//import com.upc.http.RepoService;
//import com.upc.http.SearchService;
//import com.upc.http.UserService;
//import com.upc.http.core.AppRetrofit;
//import com.upc.http.core.HttpObserver;
//import com.upc.http.core.HttpProgressSubscriber;
//import com.upc.http.core.HttpResponse;
//import com.upc.http.core.HttpSubscriber;
//import com.upc.http.error.HttpError;
//import com.upc.http.error.HttpPageNoFoundError;
//import com.upc.http.error.UnauthorizedError;
//import com.upc.mvp.contract.base.IBaseContract.Presenter;
//import com.upc.mvp.contract.base.IBaseContract.View;
//import com.upc.mvp.model.ReportWork;
//import com.upc.util.NetHelper;
//import com.upc.util.PrefUtils;
//import com.upc.util.StringUtils;
//
//import org.apache.http.conn.ConnectTimeoutException;
//
//import java.net.SocketTimeoutException;
//import java.net.UnknownHostException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//import okhttp3.ResponseBody;
//import retrofit2.Response;
//import rx.Observable;
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
//public abstract class BasePresenter1<V extends View> implements Presenter<V> {
//    private final String TAG = "BasePresenter";
//    protected DaoSession daoSession;
//    private boolean isAttached = false;
//    private boolean isEventSubscriber = false;
//    private boolean isViewInitialized = false;
//    protected V mView;
//    private Map<String, Integer> requestTimesMap = new HashMap();
//    private ArrayList<Subscriber<?>> subscribers;
//
//    protected interface CheckStatusCallback {
//        void onChecked(boolean z);
//    }
//
//    protected interface IObservableCreator<T> {
//        Observable<Response<ReportWork>> createObservable(boolean z);
//    }
//
//    public BasePresenter1(DaoSession daoSession) {
//        this.daoSession = daoSession;
//        this.subscribers = new ArrayList();
//    }
//
//    public void onSaveInstanceState(Bundle outState) {
//        DataAutoAccess.saveData(this, outState);
//    }
//
//    public void onRestoreInstanceState(Bundle outState) {
//        if (outState != null) {
//            DataAutoAccess.getData(this, outState);
//        }
//    }
//
//    public void attachView(@NonNull V view) {
//        this.mView = view;
//        this.isAttached = true;
//    }
//
//    public void detachView() {
//    }
//
//    public void onViewInitialized() {
//        this.isViewInitialized = true;
//    }
//
//    /* access modifiers changed from: protected */
//    public boolean isViewInitialized() {
//        return this.isViewInitialized;
//    }
//
//    /* access modifiers changed from: protected */
//    public GxlrService getGxlrService() {
//        Log.i("=============>", "BasePreseneter _____   getGxlService_____自己创建的工序service");
//        return (GxlrService) AppRetrofit.INSTANCE.getRetrofit(AppConfig.UPC_API_BASE_URL, "22", true).create(GxlrService.class);
//    }
//
//    /* access modifiers changed from: protected */
//    public LoginService getLoginService() {
//        return (LoginService) AppRetrofit.INSTANCE.getRetrofit(AppConfig.GITHUB_BASE_URL, null).create(LoginService.class);
//    }
//
//    /* access modifiers changed from: protected */
//    public LoginService getLoginService(String token) {
//        return (LoginService) AppRetrofit.INSTANCE.getRetrofit(AppConfig.UPC_API_BASE_URL, token).create(LoginService.class);
//    }
//
//    /* access modifiers changed from: protected */
//    public UserService getUserService(String token) {
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("501 - baseP getUserService 2");
//        stringBuilder.append(token);
//        Log.i("=============>", stringBuilder.toString());
//        return (UserService) AppRetrofit.INSTANCE.getRetrofit(AppConfig.UPC_API_BASE_URL, token).create(UserService.class);
//    }
//
//    /* access modifiers changed from: protected */
//    public UserService getUserService() {
//        Log.i("BasePresenter", "getUserService: ");
//        return getUserService(AppData.INSTANCE.getAccessToken());
//    }
//
//    /* access modifiers changed from: protected */
//    public RepoService getRepoService() {
//        return (RepoService) getServices(RepoService.class);
//    }
//
//    /* access modifiers changed from: protected */
//    public SearchService getSearchService() {
//        return (SearchService) getServices(SearchService.class);
//    }
//
//    /* access modifiers changed from: protected */
//    public IssueService getIssueService() {
//        return (IssueService) getServices(IssueService.class);
//    }
//
//    /* access modifiers changed from: protected */
//    public NotificationsService getNotificationsService() {
//        return (NotificationsService) getServices(NotificationsService.class);
//    }
//
//    private <T> T getServices(Class<T> serviceClass) {
//        return getServices(serviceClass, AppConfig.UPC_API_BASE_URL, true);
//    }
//
//    /* access modifiers changed from: protected */
//    public <T> T getServices(Class<T> serviceClass, String baseUrl, boolean isJson) {
//        return AppRetrofit.INSTANCE.getRetrofit(baseUrl, AppData.INSTANCE.getAccessToken(), isJson).create(serviceClass);
//    }
//
//    @NonNull
//    public Context getContext() {
//        View view = this.mView;
//        if (view instanceof Context) {
//            return (Context) view;
//        }
//        if (view instanceof Fragment) {
//            return ((Fragment) view).getContext();
//        }
//        throw new NullPointerException("BasePresenter:mView is't instance of Context,can't use getContext() method.");
//    }
//
//    /* access modifiers changed from: protected */
//    public <T> void generalRxHttpExecute(@NonNull Observable<Response<T>> observable, @Nullable HttpSubscriber<T> subscriber) {
//        if (subscriber != null) {
//            this.subscribers.add(subscriber);
//            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe((Subscriber) subscriber);
//            return;
//        }
//        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new HttpSubscriber());
//    }
//
//    /* access modifiers changed from: protected */
//    public <T> void generalRxHttpExecute(@NonNull IObservableCreator<T> observableCreator, @NonNull HttpObserver<T> httpObserver) {
//        generalRxHttpExecute(observableCreator, httpObserver, false);
//    }
//
//    /* access modifiers changed from: protected */
//    public <T> void generalRxHttpExecute(@NonNull IObservableCreator<T> observableCreator, @NonNull HttpObserver<T> httpObserver, boolean readCacheFirst) {
//        generalRxHttpExecute(observableCreator, httpObserver, readCacheFirst, null);
//    }
//
//    /* access modifiers changed from: protected */
//    protected <T> void generalRxHttpExecute(@NonNull final IObservableCreator<T> observableCreator
//            , @NonNull final HttpObserver<T> httpObserver, final boolean readCacheFirst
//            , @Nullable final ProgressDialog progressDialog) {
//        requestTimesMap.put(observableCreator.toString(), 1);
//
//        final HttpObserver<T> tempObserver = new HttpObserver<T>() {
//            @Override
//            public void onError(Throwable error) {
//                if(!checkIsUnauthorized(error)){
//                    httpObserver.onError(error);
//                }
//            }
//
//            @Override
//            public void onSuccess(@NonNull HttpResponse<T> response) {
//                if (response.isSuccessful()) {
//                    if (readCacheFirst && response.isFromCache()
//                            && NetHelper.INSTANCE.getNetEnabled()
//                            && requestTimesMap.get(observableCreator.toString()) < 2) {
//                        requestTimesMap.put(observableCreator.toString(), 2);
//                        generalRxHttpExecute(observableCreator.createObservable(true),
//                                getHttpSubscriber(this, progressDialog));
//                    }
//                    httpObserver.onSuccess(response);
//                } else if(response.getOriResponse().code() == 404){
//                    onError(new HttpPageNoFoundError());
//                } else if(response.getOriResponse().code() == 504){
//                    onError(new HttpError(HttpErrorCode.NO_CACHE_AND_NETWORK));
//                } else if(response.getOriResponse().code() == 401){
//                    onError(new UnauthorizedError());
//                } else {
//                    onError(new Error(response.getOriResponse().message()));
//                }
//
//            }
//        };
//
//        boolean cacheFirstEnable = PrefUtils.isCacheFirstEnable();
////        cacheFirstEnable = cacheFirstEnable || !NetHelper.INSTANCE.getNetEnabled();
//        generalRxHttpExecute(observableCreator.createObservable(!cacheFirstEnable || !readCacheFirst),
//                getHttpSubscriber(tempObserver, progressDialog));
//    }
//
//    private <T> HttpSubscriber<T> getHttpSubscriber(HttpObserver<T> httpObserver, ProgressDialog progressDialog) {
//        if (progressDialog == null) {
//            return new HttpSubscriber(httpObserver);
//        }
//        return new HttpProgressSubscriber(progressDialog, httpObserver);
//    }
//
//    private boolean checkIsUnauthorized(Throwable error) {
//        if (!(error instanceof UnauthorizedError)) {
//            return false;
//        }
//        this.mView.showErrorToast(error.getMessage());
//        this.daoSession.getAuthUserDao().delete(AppData.INSTANCE.getAuthUser());
//        AppData.INSTANCE.setAuthUser(null);
//        AppData.INSTANCE.setLoggedUser(null);
//        this.mView.showLoginPage();
//        return true;
//    }
//
//    /* access modifiers changed from: protected */
//    @NonNull
//    public String getLoadTip() {
//        return getContext().getString(R.string.loading).concat("...");
//    }
//
//    /* access modifiers changed from: protected */
//    public boolean isLastResponse(@NonNull HttpResponse response) {
//        return response.isFromNetWork() || !NetHelper.INSTANCE.getNetEnabled().booleanValue();
//    }
//
//    /* access modifiers changed from: protected */
//    @NonNull
//    public String getErrorTip(@NonNull Throwable error) {
//        if (error == null) {
//            return null;
//        }
//        String errorTip;
//        if (error instanceof UnknownHostException) {
//            errorTip = getString(R.string.no_network_tip);
//        } else if ((error instanceof SocketTimeoutException) || (error instanceof ConnectTimeoutException)) {
//            errorTip = getString(R.string.load_timeout_tip);
//        } else if (error instanceof HttpError) {
//            errorTip = error.getMessage();
//        } else {
//            errorTip = StringUtils.isBlank(error.getMessage()) ? error.toString() : error.getMessage();
//        }
//        return errorTip;
//    }
//
//    /* access modifiers changed from: protected */
//    @NonNull
//    public String getString(@StringRes int resId) {
//        return getContext().getResources().getString(resId);
//    }
//
//    public void setEventSubscriber(boolean eventSubscriber) {
//        this.isEventSubscriber = eventSubscriber;
//        if (this.isEventSubscriber && this.isAttached && !AppEventBus.INSTANCE.getEventBus().isRegistered(this)) {
//            AppEventBus.INSTANCE.getEventBus().register(this);
//        }
//    }
//
//    /* access modifiers changed from: protected */
//    public void executeSimpleRequest(@NonNull final Observable<Response<ResponseBody>> observable) {
//        generalRxHttpExecute(new IObservableCreator<ResponseBody>() {
//            public Observable<Response<ReportWork>> createObservable(boolean forceNetWork) {
//                return observable;
//            }
//        }, new HttpObserver<ResponseBody>() {
//            public void onError(Throwable error) {
//                BasePresenter1.this.mView.showErrorToast(BasePresenter1.this.getErrorTip(error));
//            }
//
//            public void onSuccess(HttpResponse<ResponseBody> httpResponse) {
//            }
//        });
//    }
//
//    /* access modifiers changed from: protected */
//    public void checkStatus(@NonNull Observable<Response<ResponseBody>> observable, @NonNull final CheckStatusCallback callback) {
//        generalRxHttpExecute((Observable) observable, new HttpSubscriber(new HttpObserver<ResponseBody>() {
//            public void onError(Throwable error) {
//            }
//
//            public void onSuccess(HttpResponse<ResponseBody> response) {
//                callback.onChecked(response.isSuccessful());
//            }
//        }));
//    }
//}
