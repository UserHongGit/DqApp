

package com.hong.mvp.contract;


import com.hong.mvp.contract.base.IBaseContract;

/**
 * Created on 2019/5/12.
 *
 * @author upc_jxzy
 */

public interface ISplashContract {

    interface View extends IBaseContract.View{
        void showMainPage();
    }

    interface Presenter extends IBaseContract.Presenter<ISplashContract.View>{

        void getUser();

        void saveAccessToken(String accessToken, String scope, int expireIn);

    }

}
