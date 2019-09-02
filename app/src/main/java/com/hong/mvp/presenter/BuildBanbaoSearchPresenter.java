package com.hong.mvp.presenter;

import android.util.Log;
import com.hong.AppData;
import com.hong.dao.DaoSession;
import com.hong.http.core.HttpObserver;
import com.hong.http.core.HttpResponse;
import com.hong.http.core.HttpSubscriber;
import com.hong.mvp.contract.IBuildBanbaoSearchContract.Presenter;
import com.hong.mvp.contract.IBuildBanbaoSearchContract.View;
import com.hong.mvp.model.GxrbModel;
import com.hong.mvp.presenter.base.BasePresenter;
import java.util.ArrayList;
import java.util.Iterator;
import javax.inject.Inject;
import rx.Observable;

public class BuildBanbaoSearchPresenter extends BasePresenter<View> implements Presenter {
    @Inject
    public BuildBanbaoSearchPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    public void getWell(String oiledId) {
        generalRxHttpExecute((Observable) getGxlrService().getWellService(oiledId), (HttpSubscriber) new HttpSubscriber(new HttpObserver<ArrayList<GxrbModel>>() {
            public void onError(Throwable error) {
                ((View) BuildBanbaoSearchPresenter.this.mView).dismissProgressDialog();
                ((View) BuildBanbaoSearchPresenter.this.mView).showErrorToast(BuildBanbaoSearchPresenter.this.getErrorTip(error));
            }

            public void onSuccess(HttpResponse<ArrayList<GxrbModel>> response) {
                Iterator it = ((ArrayList) response.body()).iterator();
                while (it.hasNext()) {
                    GxrbModel g = (GxrbModel) it.next();
                    GxrbModel gxrbModel = new GxrbModel();
                    gxrbModel.setWellCommonName(g.getWellCommonName());
                    gxrbModel.setDid(g.getDid());
                    AppData.searchWells.add(gxrbModel);
                }
            }
        }));
    }

    public void isUpload(String did, String cl, String date) {
        generalRxHttpExecute((Observable) getGxlrService().getWellService(did, cl, date), (HttpSubscriber) new HttpSubscriber(new HttpObserver<String>() {
            public void onError(Throwable error) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("getWell() 错误--------");
                stringBuilder.append(error);
                Log.i("============>", stringBuilder.toString());
                ((View) BuildBanbaoSearchPresenter.this.mView).dismissProgressDialog();
                ((View) BuildBanbaoSearchPresenter.this.mView).showErrorToast(BuildBanbaoSearchPresenter.this.getErrorTip(error));
            }

            public void onSuccess(HttpResponse<String> response) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onSuccess: __");
                stringBuilder.append((String) response.body());
                Log.i("=====", stringBuilder.toString());
                ((View) BuildBanbaoSearchPresenter.this.mView).isOpenActivity(Integer.parseInt((String) response.body()));
            }
        }));
    }
}
