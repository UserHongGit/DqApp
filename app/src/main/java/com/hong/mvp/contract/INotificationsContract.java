

package com.hong.mvp.contract;

import android.app.Notification;
import android.support.annotation.NonNull;


import com.hong.mvp.contract.base.IBaseContract;
import com.hong.mvp.contract.base.IBaseListContract;
import com.hong.mvp.contract.base.IBasePagerContract;
import com.hong.mvp.model.Repository;
import com.hong.ui.adapter.DoubleTypesModel;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/11/6 17:44:57
 */

public interface INotificationsContract {

    interface View extends IBaseContract.View, IBasePagerContract.View, IBaseListContract.View{
        void showNotifications(ArrayList<DoubleTypesModel<Repository, Notification>> notifications);
    }

    interface Presenter extends IBasePagerContract.Presenter<INotificationsContract.View> {
        void loadNotifications(int page, boolean isReload);
        void markNotificationAsRead(String threadId);
        void markAllNotificationsAsRead();
        boolean isNotificationsAllRead();
        void markRepoNotificationsAsRead(@NonNull Repository repository);
    }

}
