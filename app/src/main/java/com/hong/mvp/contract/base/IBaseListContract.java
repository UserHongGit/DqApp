package com.hong.mvp.contract.base;

/**
 * Created by upc_jxzy on 2017/9/22 10:47:52
 */

public interface IBaseListContract {

    interface View {
        void showLoadError(String errorMsg);
        void showLoadError2(String aticle);

        void setCanLoadMore(boolean canLoadMore);
    }

}
