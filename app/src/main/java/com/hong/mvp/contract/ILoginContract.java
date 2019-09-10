

package com.hong.mvp.contract;

import android.content.Intent;

import com.hong.mvp.contract.base.IBaseContract;
import com.hong.mvp.model.BasicToken;


/**
 * Created on 2019/5/12.
 *
 * @author upc_jxzy
 */

public interface ILoginContract {

    interface View extends IBaseContract.View{

        void onGetTokenSuccess(BasicToken basicToken);

        void onGetTokenError(String errorMsg);

        void onLoginComplete();

    }

    interface Presenter extends IBaseContract.Presenter<ILoginContract.View>{

        void getToken(String code, String state);

        String getOAuth2Url();

        void basicLogin(String userName, String password);

        void handleOauth(Intent intent);

        void getUserInfo(BasicToken basicToken);

//        void getMenu(BasicToken basicToken);
    }

}
