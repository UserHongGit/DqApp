package com.hong.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import com.hong.R;
import com.hong.inject.component.AppComponent;
import com.hong.inject.component.DaggerFragmentComponent;
import com.hong.inject.module.FragmentModule;
import com.hong.mvp.contract.IActivityContract.View;
import com.hong.mvp.model.Event;
import com.hong.mvp.presenter.ActivityPresenter;
import com.hong.ui.adapter.ActivitiesAdapter;
import com.hong.ui.fragment.base.ListFragment;
import com.hong.util.BundleHelper;
import com.hong.util.PrefUtils;
import java.io.Serializable;
import java.util.ArrayList;

public class ActivityFragment extends ListFragment<ActivityPresenter, ActivitiesAdapter> implements View {
    private static String TAG;

    public enum ActivityType {
        News,
        User,
        Repository,
        PublicNews
    }

    static {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ActivityFragment.class.getSimpleName());
        stringBuilder.append("===============");
        TAG = stringBuilder.toString();
    }

    public static ActivityFragment create(@NonNull ActivityType type, @NonNull String user) {
        Log.i(TAG, "create: 2参数");
        return create(type, user, null);
    }

    public static ActivityFragment create(@NonNull ActivityType type, @NonNull String user, @Nullable String repo) {
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("create:3参数 +++++++++++++++++++");
        stringBuilder.append(user);
        stringBuilder.append("//");
        stringBuilder.append(repo);
        Log.i(str, stringBuilder.toString());
        ActivityFragment fragment = new ActivityFragment();
        fragment.setArguments(BundleHelper.builder().put("type", (Serializable) type).put("user", user).put("repo", repo).build());
        return fragment;
    }

    /* access modifiers changed from: protected */
    public int getLayoutId() {
        return R.layout.fragment_list;
    }

    /* access modifiers changed from: protected */
    public void setupFragmentComponent(AppComponent appComponent) {
        Log.i(TAG, "setupFragmentComponent: ");
        DaggerFragmentComponent.builder().appComponent(appComponent).fragmentModule(new FragmentModule(this)).build().inject(this);
    }

    /* access modifiers changed from: protected */
    public void initFragment(Bundle savedInstanceState) {
        Log.i(TAG, "initFragment: ");
        super.initFragment(savedInstanceState);
        setLoadMoreEnable(true);
        registerForContextMenu(this.recyclerView);
    }

    /* access modifiers changed from: protected */
    public void onReLoadData() {
        Log.i(TAG, "onReLoadData: ");
        ((ActivityPresenter) this.mPresenter).loadEvents(true, 1);
    }

    /* access modifiers changed from: protected */
    public String getEmptyTip() {
        return getString(R.string.no_activity);
    }

    public void onItemClick(int position, @NonNull android.view.View view) {
        super.onItemClick(position, view);
        Log.i(TAG, "onItemClick: ");
    }

    public boolean onItemLongClick(int position, @NonNull android.view.View view) {
        Log.i(TAG, "onItemLongClick: ");
        PrefUtils.set(PrefUtils.ACTIVITY_LONG_CLICK_TIP_ABLE, Boolean.valueOf(false));
        return super.onItemLongClick(position, view);
    }

    public void onCreateContextMenu(ContextMenu menu, android.view.View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        Log.i(TAG, "onCreateContextMenu: ");
    }

    public boolean onContextItemSelected(MenuItem item) {
        Log.i(TAG, "onContextItemSelected: ");
        if (item.getIntent() != null) {
            startActivity(item.getIntent());
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onLoadMore(int page) {
        super.onLoadMore(page);
        Log.i(TAG, "onLoadMore: ");
        ((ActivityPresenter) this.mPresenter).loadEvents(false, page);
    }

    public void showEvents(ArrayList<Event> events) {
        Log.i(TAG, "showEvents: ");
        ((ActivitiesAdapter) this.adapter).setData(events);
        postNotifyDataSetChanged();
        if (getCurPage() == 2 && PrefUtils.isActivityLongClickTipAble()) {
            showOperationTip(R.string.activity_click_tip);
            PrefUtils.set(PrefUtils.ACTIVITY_LONG_CLICK_TIP_ABLE, Boolean.valueOf(false));
        }
    }

    public void onFragmentShowed() {
        Log.i(TAG, "onFragmentShowed: ");
        super.onFragmentShowed();
        if (this.mPresenter != null) {
            ((ActivityPresenter) this.mPresenter).prepareLoadData();
        }
    }
}
