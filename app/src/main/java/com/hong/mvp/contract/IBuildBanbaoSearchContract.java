package com.hong.mvp.contract;

public interface IBuildBanbaoSearchContract {

    public interface Presenter extends com.hong.mvp.contract.base.IBaseContract.Presenter<View> {
        void getWell(String str);

        void isUpload(String str, String str2, String str3);
    }

    public interface View extends com.hong.mvp.contract.base.IBaseContract.View {
        void isOpenActivity(int i);
    }
}
