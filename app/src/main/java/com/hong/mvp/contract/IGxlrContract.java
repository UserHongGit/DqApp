package com.hong.mvp.contract;

import com.hong.mvp.contract.base.IBaseContract;
import com.hong.mvp.model.ReportWork;

import java.util.HashMap;

public interface IGxlrContract {

    interface View extends IBaseContract.View {
        void renderBaseData(ReportWork report);

        void saveOk(HashMap<String, String> body);
    }

    interface Presenter extends IBaseContract.Presenter<IGxlrContract.View> {
        void getBaseData(String did,String banbaoType,String reportTime,String orderClasses);

        void sgbbSave(ReportWork re);
    }

}
