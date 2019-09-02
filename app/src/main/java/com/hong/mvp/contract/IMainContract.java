

package com.hong.mvp.contract;

import android.support.annotation.NonNull;


import com.hong.dao.AuthUser;
import com.hong.mvp.contract.base.IBaseContract;

import java.util.List;

/**
 * Created on 2019/5/18.
 *
 * @author upc_jxzy
 */

public interface IMainContract {

    interface View extends IBaseContract.View{
        void restartApp();
    }

    interface Presenter extends IBaseContract.Presenter<IMainContract.View>{
        boolean isFirstUseAndNoNewsUser();
        List<AuthUser> getLoggedUserList();
        void toggleAccount(@NonNull String loginId);
        void logout();
    }

}
