

package com.hong.mvp.contract;

import android.support.annotation.NonNull;


import com.hong.dao.AuthUser;
import com.hong.http.model.UMenu;
import com.hong.mvp.contract.base.IBaseContract;
import com.hong.mvp.model.BasicToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2019/5/18.
 *
 * @author upc_jxzy
 */

public interface IWebContract {

    interface View extends IBaseContract.View{
        void restartApp();
        void renderMenu(ArrayList<UMenu> menus);
    }

    interface Presenter extends IBaseContract.Presenter<IWebContract.View>{
        boolean isFirstUseAndNoNewsUser();
        List<AuthUser> getLoggedUserList();
        void toggleAccount(@NonNull String loginId);
        void logout();
        void getMenu(String token);
    }

}
