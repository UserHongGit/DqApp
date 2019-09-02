

package com.hong.mvp.contract;


import com.hong.mvp.contract.base.IBaseContract;
import com.hong.mvp.contract.base.IBaseListContract;
import com.hong.mvp.contract.base.IBasePagerContract;
import com.hong.mvp.model.Repository;
import com.hong.mvp.model.filter.RepositoriesFilter;

import java.util.ArrayList;

/**
 * Created on 2017/7/18.
 *
 * @author ThirtyDegreesRay
 */

public interface IRepositoriesContract {

    interface View extends IBaseContract.View, IBasePagerContract.View, IBaseListContract.View {

        void showRepositories(ArrayList<Repository> repositoryList);

    }

    interface Presenter extends IBasePagerContract.Presenter<IRepositoriesContract.View> {
        void loadRepositories(boolean isReLoad, int page);
        void loadRepositories(RepositoriesFilter filter);
    }

}
