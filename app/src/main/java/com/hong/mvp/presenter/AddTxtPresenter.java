package com.hong.mvp.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hong.dao.DaoSession;
import com.hong.http.core.HttpObserver;
import com.hong.http.core.HttpResponse;
import com.hong.mvp.contract.IAddTxtContract;
import com.hong.mvp.presenter.base.BasePresenter;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * 增加工序行
 */

public class AddTxtPresenter extends BasePresenter<IAddTxtContract.View>
        implements IAddTxtContract.Presenter {
    @Inject
    public AddTxtPresenter(DaoSession daoSession) {
        super(daoSession);
    }


    /**
     * 返回对象标准写法日
     */
    @Override
    public void getCs1(String did,String gxType,String oilfield) {
        mView.showLoading();

        HttpObserver<ArrayList<HashMap<String,String>>> httpObserver =
                new HttpObserver<ArrayList<HashMap<String,String>>>() {
                    @Override
                    public void onError(@NonNull Throwable error) {
                        Log.i("---", "onError: 出错了草!");
                        error.printStackTrace();
                        mView.hideLoading();
                    }

                    @Override
                    public void onSuccess(@NonNull HttpResponse<ArrayList<HashMap<String,String>>> response) {
                        Log.i("___-", "onSuccess: =-===___"+response.body()+"////"+response.body());
                        ArrayList<HashMap<String, String>> li = response.body();
//                        for (HashMap<String,String> x : li){
//                            Log.i("______", "hash____"+x);
//                        }
                        mView.hideLoading();
                        mView.renderCs(li);
                    }
                };
        generalRxHttpExecute(new IObservableCreator<ArrayList<HashMap<String,String>>>() {
            @Nullable
            @Override
            public Observable<Response<ArrayList<HashMap<String,String>>>>  createObservable(boolean forceNetWork) {
                return getAddTxtService().getCs1(did,gxType,oilfield);
            }
        }, httpObserver);

    }
}
