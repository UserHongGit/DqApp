package com.hong.mvp.contract;

import com.hong.mvp.contract.base.IBaseContract;

import java.util.ArrayList;
import java.util.HashMap;

public interface IAddTxtContract {

    interface View extends IBaseContract.View {

        void renderCs(ArrayList<HashMap<String, String>> li);
    }

    interface Presenter extends IBaseContract.Presenter<IAddTxtContract.View> {
        void getCs1(String did,String gxType,String oilfield);


    }

}
