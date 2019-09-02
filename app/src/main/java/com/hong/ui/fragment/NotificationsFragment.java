

package com.hong.ui.fragment;

import android.app.Notification;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import com.hong.R;
import com.hong.inject.component.AppComponent;
import com.hong.inject.component.DaggerFragmentComponent;
import com.hong.inject.module.FragmentModule;
import com.hong.mvp.contract.INotificationsContract;
import com.hong.mvp.model.Repository;
import com.hong.mvp.presenter.NotificationsPresenter;
import com.hong.ui.activity.RepositoryActivity;
import com.hong.ui.adapter.DoubleTypesModel;
import com.hong.ui.adapter.NotificationsAdapter;
import com.hong.ui.fragment.base.ListFragment;
import com.hong.util.BundleHelper;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/11/6 20:54:31
 */

public class NotificationsFragment extends ListFragment<NotificationsPresenter, NotificationsAdapter>
        implements INotificationsContract.View, NotificationsAdapter.NotificationAdapterListener {
    private static String TAG = NotificationsFragment.class.getSimpleName()+"======================";

    public enum NotificationsType{
        Unread, Participating, All
    }

    public static Fragment create(NotificationsType type){
        Log.i(TAG, "create: ");
        NotificationsFragment fragment = new NotificationsFragment();
        fragment.setArguments(BundleHelper.builder().put("type", type).build());
        return fragment;
    }

    @Override
    protected void initFragment(Bundle savedInstanceState) {
        super.initFragment(savedInstanceState);
        Log.i(TAG, "initFragment: ");
        setLoadMoreEnable(true);
        setHasOptionsMenu(NotificationsType.Unread.equals(mPresenter.getType()));
        adapter.setListener(this);
    }

    @Override
    public void showNotifications(ArrayList<DoubleTypesModel<Repository, Notification>> notifications) {
        adapter.setData(notifications);
        Log.i(TAG, "showNotifications: ");
        postNotifyDataSetChanged();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    protected int getLayoutId() {
        Log.i(TAG, "getLayoutId: ");
        return R.layout.fragment_list;
    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {
        DaggerFragmentComponent.builder()
                .appComponent(appComponent)
                .fragmentModule(new FragmentModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onReLoadData() {
        Log.i(TAG, "onReLoadData: ");
        mPresenter.loadNotifications(1, true);
    }

    @Override
    protected String getEmptyTip() {
        Log.i(TAG, "getEmptyTip: ");
        return getString(R.string.no_notifications_tip);
    }

    @Override
    public void onFragmentShowed() {
        super.onFragmentShowed();
        Log.i(TAG, "onFragmentShowed: ");
        if(mPresenter != null) mPresenter.prepareLoadData();
    }

    @Override
    public void onItemClick(int position, @NonNull View view) {
        super.onItemClick(position, view);
        Log.i(TAG, "onItemClick: ");
        Notification notification = adapter.getData().get(position).getM2();
        Repository repository = adapter.getData().get(position).getM1();
        if(adapter.getItemViewType(position) == 0){
            RepositoryActivity.show(getActivity(), repository.getOwner().getLogin(), repository.getName());
        }else{
//            String url = notification.getSubject().getUrl();
//            switch (notification.getSubject().getType()){
//                case Issue:
//                    IssueDetailActivity.show(getActivity(), url);
//                    break;
//                case CommitwriteToParcel:
//                    CommitDetailActivity.show(getActivity(), url);
//                    break;
//                case PullRequest:
//                    showInfoToast(getString(R.string.developing));
//                    break;
//            }
//
//            if(notification.isUnread() &&
//                    !notification.getSubject().getType().equals(NotificationSubject.Type.PullRequest)){
//                mPresenter.markNotificationAsRead(notification.getId());
//                notification.setUnread(false);
//                adapter.notifyItemChanged(position);
//                getActivity().invalidateOptionsMenu();
//            }

        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.i(TAG, "onCreateOptionsMenu: ");
        if(!mPresenter.isNotificationsAllRead()){
            inflater.inflate(R.menu.menu_notifications, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected: ");
        if(item.getItemId() == R.id.action_mark_all_as_read){
            mPresenter.markAllNotificationsAsRead();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRepoMarkAsReadClicked(@NonNull Repository repository) {
        Log.i(TAG, "onRepoMarkAsReadClicked: ");
        mPresenter.markRepoNotificationsAsRead(repository);
    }

}
