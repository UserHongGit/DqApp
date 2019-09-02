

package com.hong.mvp.presenter;

import com.hong.AppData;
import com.hong.dao.DaoSession;
import com.hong.mvp.contract.ISettingsContract;
import com.hong.mvp.presenter.base.BasePresenter;

import javax.inject.Inject;

/**
 * 设置按钮
 * 切换语言皮肤
 * @author ThirtyDegreesRay
 */

public class SettingsPresenter extends BasePresenter<ISettingsContract.View>
        implements ISettingsContract.Presenter{

    @Inject
    public SettingsPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void logout() {
        daoSession.getAuthUserDao().delete(AppData.INSTANCE.getAuthUser());
        AppData.INSTANCE.setAuthUser(null);
        AppData.INSTANCE.setLoggedUser(null);
        mView.showLoginPage();
    }

}
