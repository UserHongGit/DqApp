package com.hong.mvp.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.hong.dao.DaoSession;
import com.hong.http.core.HttpObserver;
import com.hong.http.core.HttpResponse;
import com.hong.http.error.HttpPageNoFoundError;
import com.hong.mvp.contract.IActivityContract.Presenter;
import com.hong.mvp.contract.IActivityContract.View;
import com.hong.mvp.model.Event;
import com.hong.mvp.model.Repository;
import com.hong.mvp.presenter.base.BasePagerPresenter;
import com.hong.ui.fragment.ActivityFragment;
import com.hong.ui.fragment.ActivityFragment.ActivityType;
import com.hong.util.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;
import retrofit2.Response;
import rx.Observable;

public class ActivityPresenter extends BasePagerPresenter<View> implements Presenter {
    private static String TAG;
    ArrayList<Event> events;
    @AutoAccess
    String repo;
    @AutoAccess(dataName = "repository")
    Repository repository;
    @AutoAccess
    ActivityType type;
    @AutoAccess
    String user;

    @AutoAccess
    int itemId;

    static {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ActivityFragment.class.getSimpleName());
        stringBuilder.append("==============");
        TAG = stringBuilder.toString();
    }

    @Inject
    public ActivityPresenter(DaoSession daoSession) {
        super(daoSession);
        Log.i(TAG, "ActivityPresenter: ");
    }

    public void onViewInitialized() {
        super.onViewInitialized();
        Log.i(TAG, "onViewInitialized: ");
    }

    /* access modifiers changed from: protected */
    public void loadData() {
        Log.i(TAG, "loadData: ");
        if (this.events != null) {
            ((View) this.mView).showEvents(this.events);
            ((View) this.mView).hideLoading();
            return;
        }
        loadEvents(false, 1);
    }

    public void loadData(int itemId) {
        Log.i(TAG, "loadData: ____________"+itemId);
        if (this.events != null) {
            ((View) this.mView).showEvents(this.events);
            ((View) this.mView).hideLoading();
            return;
        }
        loadEvents(false, 1);
    }

    public void loadEvents(final boolean isReload, final int page) {
        Log.i(TAG, "loadEvents: ");
        mView.showLoading();

        final boolean readCacheFirst = !isReload && page == 1;



        HttpObserver<HashMap<String,ArrayList<String>>> httpObserver =
                new HttpObserver<HashMap<String,ArrayList<String>>>() {
                    @Override
                    public void onError(@NonNull Throwable error) {
                        Log.i("---", "onError: 出错了草!");
                        error.printStackTrace();
                        mView.hideLoading();
                    }

                    @Override
                    public void onSuccess(@NonNull HttpResponse<HashMap<String,ArrayList<String>>> response) {
                        Log.i("___-", "onSuccess: =-===___"+response.body()+"////"+response.body());
//                        for (HashMap<String,String> x : li){
//                            Log.i("______", "hash____"+x);
//                        }
                        mView.hideLoading();
                        ArrayList<String> rows = response.body().get("rows");
                        mView.showEvents2(rows);
                    }
                };
        generalRxHttpExecute(new IObservableCreator<HashMap<String,ArrayList<String>>>() {
            @Nullable
            @Override
            public Observable<Response<HashMap<String,ArrayList<String>>>>  createObservable(boolean forceNetWork) {
                return getUserService().getPublicEvent(forceNetWork, page);
            }
        }, httpObserver);

//        HttpObserver<ArrayList<Event>> httpObserver = new HttpObserver<ArrayList<Event>>() {
//            @Override
//            public void onError(Throwable error) {
//                mView.hideLoading();
//                if(!StringUtils.isBlankList(events)){
//                    mView.showErrorToast(getErrorTip(error));
//                } else if(error instanceof HttpPageNoFoundError){
//                    mView.showEvents(new ArrayList<Event>());
//                }else{
//                    mView.showLoadError(getErrorTip(error));
//                }
//            }
//
//            @Override
//            public void onSuccess(HttpResponse<ArrayList<Event>> response) {
//                mView.hideLoading();
//                correctEvent(response.body());
//                if(events == null || isReload || readCacheFirst){
//                    events = response.body();
//                } else {
//                    events.addAll(response.body());
//                }
//                if(response.body().size() == 0 && events.size() != 0){
//                    mView.setCanLoadMore(false);
//                } else {
//                    mView.showEvents(events);
//                }
//            }
//        };


//        generalRxHttpExecute(new IObservableCreator<ArrayList<Event>>() {
//            @Override
//            public Observable<Response<ArrayList<Event>>> createObservable(boolean forceNetWork) {
//                return getObservable(forceNetWork, page);
//            }
//        }, httpObserver, readCacheFirst);
    }

    private Observable<Response<ArrayList<Event>>> getObservable(boolean forceNetWork, int page) {
        Log.i(TAG, "getObservable: FragmentPagerModel类中createRepoPagerList()方法传递过来的_______"+user+"///"+repo+"//////"+itemId);
        if (this.type.equals(ActivityType.News)) {
            Log.i(TAG, "getObservable: 走的请求111111");
            return getUserService().getNewsEvent(forceNetWork, this.user, page);
        } else if (this.type.equals(ActivityType.User)) {
            Log.i(TAG, "getObservable: 走的请求2222222222");
            return getUserService().getUserEvents(forceNetWork, this.user, page);
        } else if (this.type.equals(ActivityType.Repository)) {
            return getRepoService().getRepoEvent(forceNetWork, this.repo);
        } else {
//            if (this.type.equals(ActivityType.PublicNews)) {
//                Log.i(TAG, "getObservable: 走的请求33333333333333");
//                return getUserService().getPublicEvent(forceNetWork, page);
//            }

            Log.i(TAG, "getObservable: 走的请求4444444444444");
            return null;
        }
    }

    private void correctEvent(ArrayList<Event> arrayList) {
        Log.i(TAG, "correctEvent: ");
    }
}
