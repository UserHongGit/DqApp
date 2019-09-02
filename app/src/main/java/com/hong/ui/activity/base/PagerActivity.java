

package com.hong.ui.activity.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;

import com.hong.R;
import com.hong.mvp.contract.base.IBaseContract;
import com.hong.mvp.presenter.base.BasePresenter;
import com.hong.ui.adapter.base.FragmentViewPagerAdapter;
import com.hong.ui.fragment.base.BaseFragment;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by ThirtyDegreesRay on 2017/8/14 17:51:00
 */

public abstract class PagerActivity<P extends BasePresenter> extends BaseDrawerActivity<P>
        implements IBaseContract.View,
        ViewPager.OnPageChangeListener,
        TabLayout.OnTabSelectedListener{
    private static String TAG = PagerActivity.class.getSimpleName()+"=============";

    @Inject protected FragmentViewPagerAdapter pagerAdapter;

    @BindView(R.id.view_pager) protected ViewPager viewPager;
    @BindView(R.id.tab_layout) protected TabLayout tabLayout;

    private ArrayList<Fragment> fragments ;

    private int prePosition = 0;

    @Override
    protected void initActivity() {
        super.initActivity();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        Log.i(TAG, "initView: ");
        viewPager.addOnPageChangeListener(this);
        tabLayout.addOnTabSelectedListener(this);
    }

    @Override
    @Deprecated
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyDown: ");
        Fragment fragment = pagerAdapter.getCurFragment();
        if(fragment != null
                && fragment instanceof IFragmentKeyListener
                && ((IFragmentKeyListener)fragment).onKeyDown(keyCode, event)){
            return true;
        }
        return onMainKeyDown(keyCode, event);
    }

    @Override
    protected void onToolbarDoubleClick() {
        super.onToolbarDoubleClick();
        Log.i(TAG, "onToolbarDoubleClick: ");
        scrollToTop();
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        Log.i(TAG, "onTabReselected: ");
        scrollToTop();
    }

    private void scrollToTop(){
        Log.i(TAG, "scrollToTop: ");
        Fragment fragment = pagerAdapter.getCurFragment();
        if(fragment != null && fragment instanceof BaseFragment){
            ((BaseFragment)fragment).scrollToTop();
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Log.i(TAG, "onTabSelected: ");
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        Log.i(TAG, "onTabUnselected: ");
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }

    protected boolean onMainKeyDown(int keyCode, KeyEvent event){
        Log.i(TAG, "onMainKeyDown: ");
        return super.onKeyDown(keyCode, event);
    }

    public interface IFragmentKeyListener {
        boolean onKeyDown(int keyCode, KeyEvent event);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.i(TAG, "onPageScrolled: ");
    }

    @Override
    public void onPageSelected(final int position) {
        Log.i(TAG, "onPageSelected: ");
        postNotifyFragmentStatus(prePosition, false, 100);
        postNotifyFragmentStatus(position, true, 500);
        prePosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.i(TAG, "onPageScrollStateChanged: ");
    }

    /**
     * Notify first pager selected, only for first launch
     */
    protected void showFirstPager(){
        Log.i(TAG, "showFirstPager: ");
        prePosition = 0;
        postNotifyFragmentStatus(0, true, 100);

    }

    private void postNotifyFragmentStatus(final int position, final boolean isShow, long delay){
        Log.i(TAG, "postNotifyFragmentStatus: "+pagerAdapter.getPagerList().size()+"////");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isShow){
                    pagerAdapter.getPagerList().get(position).getFragment().onFragmentShowed();
                }else{
                    pagerAdapter.getPagerList().get(position).getFragment().onFragmentHided();
                }
            }
        }, delay);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        int fragmentPosition = getFragmentPosition(fragment);
        Log.i(TAG, "onAttachFragment: -----"+fragmentPosition);
        if(fragmentPosition != -1) getFragments().set(fragmentPosition, fragment);
    }

    @NonNull
    public ArrayList<Fragment> getFragments() {

        if(fragments == null){
            fragments = new ArrayList<>();
            for(int i = 0; i < getPagerSize(); i++){
                fragments.add(null);
            }
        }
        Log.i(TAG, "getFragments: "+getPagerSize()+"/////"+fragments.size());
        return fragments;
    }

    public abstract int getPagerSize();

    protected abstract int getFragmentPosition(Fragment fragment);


}
