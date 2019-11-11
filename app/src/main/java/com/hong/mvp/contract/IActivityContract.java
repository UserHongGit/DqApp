

package com.hong.mvp.contract;


import com.hong.mvp.contract.base.IBaseContract;
import com.hong.mvp.contract.base.IBaseListContract;
import com.hong.mvp.contract.base.IBasePagerContract;
import com.hong.mvp.model.Event;

import java.util.ArrayList;

/**
 * Created by upc_jxzy on 2017/8/23 21:51:44
 */

public interface IActivityContract {

    interface View extends IBaseContract.View, IBasePagerContract.View, IBaseListContract.View {
        void showEvents(ArrayList<Event> events);
        void showEvents2(ArrayList<String> events);
    }

    interface Presenter extends IBasePagerContract.Presenter<View>{
        void loadEvents(boolean isReload, int page);
//        ArrayList<ActivityRedirectionModel> getRedirectionList(@NonNull Event event);
    }

}
