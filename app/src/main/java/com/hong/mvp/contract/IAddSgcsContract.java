package com.hong.mvp.contract;

import com.hong.mvp.contract.base.IBaseContract;
import com.hong.mvp.model.sgcs.RbEntity;
import com.hong.mvp.model.sgcs.SgcsReturn;

import java.util.HashMap;

public interface IAddSgcsContract {

    interface View extends IBaseContract.View {

        void renderSgcsActivity(SgcsReturn body);

        void isOver(HashMap<String, String> body);
    }

    interface Presenter extends IBaseContract.Presenter<IAddSgcsContract.View> {
        void getSgcsDesc(String did,String orderClasses,String reportTime,String spid,String pdid);

        void savePPData(RbEntity rbEntity);
    }

}
