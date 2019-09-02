package com.hong.mvp.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hong.AppData;
import com.hong.dao.DaoSession;
import com.hong.http.core.HttpObserver;
import com.hong.http.core.HttpResponse;
import com.hong.mvp.contract.IAddSgcsContract;
import com.hong.mvp.model.sgcs.GxEntity;
import com.hong.mvp.model.sgcs.RbEntity;
import com.hong.mvp.model.sgcs.SgcsReturn;
import com.hong.mvp.presenter.base.BasePresenter;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * 增加工序行
 */

public class AddSgcsPresenter extends BasePresenter<IAddSgcsContract.View>
        implements IAddSgcsContract.Presenter {

    private static final String TAG = AddSgcsPresenter.class.getSimpleName()+"____";

    @Inject
    public AddSgcsPresenter(DaoSession daoSession) {
        super(daoSession);
    }


    @Override
    public void getSgcsDesc(String did, String orderClasses, String reportTime, String spid, String pdid) {

        mView.showLoading();

        HttpObserver<SgcsReturn> httpObserver =
                new HttpObserver<SgcsReturn>() {
                    @Override
                    public void onError(@NonNull Throwable error) {
                        error.printStackTrace();
                        mView.hideLoading();
                    }

                    @Override
                    public void onSuccess(@NonNull HttpResponse<SgcsReturn> response) {
                        List<GxEntity> li = response.body().getEntity_param_list();
//                        for (GxEntity g : li){
//                            Log.i(TAG, "哦吼猛男, _____"+g.getParam()+"//"+g.getDatatype());
//                        }
                        mView.renderSgcsActivity(response.body());
                        mView.hideLoading();
                    }
                };
        generalRxHttpExecute(new IObservableCreator<SgcsReturn>() {
            @Nullable
            @Override
            public Observable<Response<SgcsReturn>> createObservable(boolean forceNetWork) {
                return getAddSgcsService().getSgcsDesc(did,orderClasses,reportTime,spid,pdid,AppData.INSTANCE.getLoggedUser().getOilfield(), AppData.INSTANCE.getLoggedUser().getId() );
            }
        }, httpObserver);




    }

    @Override
    public void savePPData(RbEntity rbEntity) {
//        System.out.println("需要____"+rbEntity.getPdid()+"////"+rbEntity.getDid()+"//"+rbEntity.getSpid()+"//"+rbEntity.getReport_time()+"//"+rbEntity.getOrder_classes());
//        for (RbEntity r : rbEntity.getEntityList()){
//            System.out.println("需要____"+r.getPro_param_id()+"///"+r.getParamname()+"//"+r.getContent()+"///"+r.getUnit()+"___________-niupi");
//        }

        mView.showLoading();
        HttpObserver<HashMap<String,String>> httpObserver =
                new HttpObserver<HashMap<String,String>>() {
                    @Override
                    public void onError(@NonNull Throwable error) {
                        Log.i(TAG, TAG+"savePPData   onError: 出错了草!");
                        error.printStackTrace();
                        mView.hideLoading();
                    }

                    @Override
                    public void onSuccess(@NonNull HttpResponse<HashMap<String,String>> response) {
                        Log.i(TAG, TAG+"onSuccess: 这个方法真烦__"+response.body());
                       mView.isOver(response.body());
                        mView.hideLoading();
                    }
                };
        generalRxHttpExecute(new IObservableCreator<HashMap<String,String>>() {
            @Nullable
            @Override
            public Observable<Response<HashMap<String,String>>> createObservable(boolean forceNetWork) {
                return getAddSgcsService().savePPData(rbEntity);
            }
        }, httpObserver);


    }
}
