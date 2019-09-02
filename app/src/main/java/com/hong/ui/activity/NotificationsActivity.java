

package com.hong.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.hong.R;
import com.hong.inject.component.AppComponent;
import com.hong.ui.activity.base.PagerActivity;
import com.hong.ui.adapter.base.FragmentPagerModel;
import com.hong.ui.adapter.base.FragmentViewPagerAdapter;
import com.hong.ui.fragment.NotificationsFragment;


/**
 * Created by ThirtyDegreesRay on 2017/11/6 17:38:52
 */

public class NotificationsActivity extends PagerActivity {
    private static String TAG = NotificationsActivity.class.getSimpleName()+"=================";

    public static void show(@NonNull Activity activity){
        Log.i(TAG, "show: ");
        Intent intent = new Intent(activity, NotificationsActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        Log.i(TAG, "setupActivityComponent: ");

    }

    @Override
    protected void initActivity() {
        super.initActivity();
        Log.i(TAG, "initActivity: ");
        pagerAdapter =  new FragmentViewPagerAdapter(getSupportFragmentManager());
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        Log.i(TAG, "initView: ");
        setToolbarScrollAble(true);
        setToolbarBackEnable();
        setToolbarTitle(getString(R.string.notifications));

        pagerAdapter.setPagerList(FragmentPagerModel.createNotificationsPagerList(getActivity(), getFragments()));
        tabLayout.setVisibility(View.VISIBLE);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(pagerAdapter);
        showFirstPager();
    }

    @Override
    protected int getContentView() {
        Log.i(TAG, "getContentView: ");
        return R.layout.activity_view_pager;
    }

    @Override
    public int getPagerSize() {
        Log.i(TAG, "getPagerSize: ");
        return 3;
    }

    @Override
    protected int getFragmentPosition(Fragment fragment) {
        Log.i(TAG, "getFragmentPosition: ");
        if(fragment instanceof NotificationsFragment){
            NotificationsFragment.NotificationsType type
                    = (NotificationsFragment.NotificationsType) fragment.getArguments().get("type");
            if(NotificationsFragment.NotificationsType.Unread.equals(type)){
                Log.i(TAG, "getFragmentPosition: 000000000000");
                return 0;
            } else if(NotificationsFragment.NotificationsType.Participating.equals(type)){
                Log.i(TAG, "getFragmentPosition: 111111111111111111");
                return 1;
            } else if(NotificationsFragment.NotificationsType.All.equals(type)){
                Log.i(TAG, "getFragmentPosition: 22222222222222");
                return 2;
            } else{
                Log.i(TAG, "getFragmentPosition: -1-1-1-1-1-1-1-1-1-1");
                return -1;
            }
        } else
            return -1;
    }

}
