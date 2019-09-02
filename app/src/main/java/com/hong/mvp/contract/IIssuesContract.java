package com.hong.mvp.contract;


import com.hong.mvp.contract.base.IBaseContract;
import com.hong.mvp.contract.base.IBaseListContract;
import com.hong.mvp.contract.base.IBasePagerContract;
import com.hong.mvp.model.Issue;
import com.hong.mvp.model.filter.IssuesFilter;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/9/20 14:55:29
 */

public interface IIssuesContract {

    interface View extends IBaseContract.View, IBasePagerContract.View, IBaseListContract.View{
        void showIssues(ArrayList<Issue> issues);
    }

    interface Presenter extends IBasePagerContract.Presenter<IIssuesContract.View>{
        void loadIssues(int page, boolean isReload);
        void loadIssues(IssuesFilter issuesFilter, int page, boolean isReload);
    }

}
