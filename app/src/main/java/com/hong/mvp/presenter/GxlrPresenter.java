package com.hong.mvp.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hong.dao.DaoSession;
import com.hong.http.core.HttpObserver;
import com.hong.http.core.HttpResponse;
import com.hong.mvp.contract.IGxlrContract;
import com.hong.mvp.model.ReportWork;
import com.hong.mvp.presenter.base.BasePresenter;

import java.util.HashMap;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

public class GxlrPresenter extends BasePresenter<IGxlrContract.View>
        implements IGxlrContract.Presenter {
    private static final String TAG = GxlrPresenter.class.getSimpleName()+"_____________-";

    @Inject
    public GxlrPresenter(DaoSession daoSession) {
        super(daoSession);
    }


    /**
     * 返回对象标准写法日
     */
    @Override
    public void getBaseData(String did, String banbaoType, String reportTime, String orderClasses) {
        mView.showLoading();

        HttpObserver<ReportWork> httpObserver =
                new HttpObserver<ReportWork>() {
                    @Override
                    public void onError(@NonNull Throwable error) {
                        error.printStackTrace();
                        mView.hideLoading();
                    }

                    @Override
                    public void onSuccess(@NonNull HttpResponse<ReportWork> response) {
                        Log.i(TAG, TAG+"ooooooooooooooooooooo==___"+response.body().getIntelligenceCode()+"////"+response.body().getReportTime3());
                        mView.hideLoading();
                        mView.renderBaseData(response.body());
                    }
                };
        generalRxHttpExecute(new IObservableCreator<ReportWork>() {
            @Nullable
            @Override
            public Observable<Response<ReportWork>> createObservable(boolean forceNetWork) {
                return getGxlrService().getBaseData(did,banbaoType,reportTime,orderClasses);
            }
        }, httpObserver);


    }

    @Override
    public void sgbbSave(ReportWork re) {


        mView.showLoading();
        HttpObserver<HashMap<String,String>> httpObserver =
                new HttpObserver<HashMap<String,String>>() {
                    @Override
                    public void onError(@NonNull Throwable error) {
                        Log.i(TAG, TAG+"sgbbSave   onError: 出错了草!");
                        error.printStackTrace();
                        mView.hideLoading();
                    }

                    @Override
                    public void onSuccess(@NonNull HttpResponse<HashMap<String,String>> response) {
                        HashMap<String, String> map = response.body();
                        Log.i(TAG, TAG+"sgbbSave   onSuccess: 这个方法真烦__"+response.body()+"///"+map.get("code")+"///"+map.get("msg"));
                        mView.hideLoading();
                        mView.saveOk(response.body());
                    }
                };
        generalRxHttpExecute(new IObservableCreator<HashMap<String,String>>() {
            @Nullable
            @Override
            public Observable<Response<HashMap<String,String>>> createObservable(boolean forceNetWork) {
                return getGxlrService().sgbbSave(re);
            }
        }, httpObserver);



    }
}
