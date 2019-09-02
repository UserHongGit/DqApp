

package com.hong.mvp.presenter.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.hong.R;
import com.hong.inject.component.AppComponent;
import com.hong.mvp.contract.base.IBaseContract;
import com.hong.ui.activity.base.BaseDrawerActivity;
import com.hong.ui.fragment.base.BaseFragment;
import com.orhanobut.logger.Logger;

/**
 * Created by ThirtyDegreesRay on 2017/10/21 10:03:15
 */

public abstract class SingleFragmentActivity<P extends IBaseContract.Presenter, F extends Fragment>
        extends BaseDrawerActivity<P> implements IBaseContract.View{

    private F mFragment;
    private final String FRAGMENT_TAG = "mFragment";

    @Override
    protected int getContentView() {
        return R.layout.activity_single_fragment;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setToolbarBackEnable();
        Fragment temp = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if(temp == null){
            mFragment = createFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, mFragment, FRAGMENT_TAG)
                    .commit();
            Logger.d("temp fragment is null");
        } else {
            mFragment = (F) temp;
            Logger.d("temp fragment not null");
        }
    }

    protected abstract F createFragment();

    protected F getFragment(){
        return mFragment;
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        Logger.d("onAttachFragment");
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected void onToolbarDoubleClick() {
        super.onToolbarDoubleClick();
        if (mFragment instanceof BaseFragment){
            ((BaseFragment)mFragment).scrollToTop();
        }
    }
}
