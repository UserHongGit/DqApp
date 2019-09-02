package com.hong.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.hong.R;
import com.hong.inject.component.AppComponent;
import com.hong.inject.component.DaggerFragmentComponent;
import com.hong.inject.module.FragmentModule;
import com.hong.mvp.contract.IRepositoriesContract.View;
import com.hong.mvp.model.Repository;
import com.hong.mvp.model.TrendingLanguage;
import com.hong.mvp.presenter.RepositoriesPresenter;
import com.hong.ui.activity.BuildBanbaoSearch;
import com.hong.ui.activity.RepositoryActivity;
import com.hong.ui.activity.TrendingActivity.LanguageUpdateListener;
import com.hong.ui.adapter.RepositoriesAdapter;
import com.hong.ui.fragment.base.ListFragment;
import com.hong.ui.fragment.base.OnDrawerSelectedListener;
import com.hong.util.BundleHelper;

import java.util.ArrayList;

public class RepositoriesFragment extends ListFragment<RepositoriesPresenter, RepositoriesAdapter> implements View, OnDrawerSelectedListener, LanguageUpdateListener {
    private static String TAG = RepositoriesFragment.class.getSimpleName()+"========";

    public enum RepositoriesType {
        OWNED,
        PUBLIC,
        STARRED,
        TRENDING,
        SEARCH,
        FORKS,
        TRACE,
        BOOKMARK,
        COLLECTION,
        TOPIC
    }


    public static RepositoriesFragment create(@NonNull RepositoriesType type, @NonNull String user) {
        Log.i(TAG, "create: ");
        RepositoriesFragment fragment = new RepositoriesFragment();
        fragment.setArguments(BundleHelper.builder().put("type", type)
                .put("user", user).build());
        return fragment;
    }

    public static RepositoriesFragment createForForks(@NonNull String user, @NonNull String repo) {
        Log.i(TAG, "createForForks: ");
        RepositoriesFragment fragment = new RepositoriesFragment();
        fragment.setArguments(BundleHelper.builder()
                .put("type", RepositoriesType.FORKS)
                .put("user", user)
                .put("repo", repo)
                .build());
        return fragment;
    }

    public static RepositoriesFragment createForTrace() {
        Log.i(TAG, "createForTrace: ");
        RepositoriesFragment fragment = new RepositoriesFragment();
        fragment.setArguments(BundleHelper.builder().put("type", RepositoriesType.TRACE).build());
        return fragment;
    }

    public static RepositoriesFragment createForBookmark() {
        Log.i(TAG, "createForBookmark: ");
        RepositoriesFragment fragment = new RepositoriesFragment();
        fragment.setArguments(BundleHelper.builder().put("type", RepositoriesType.BOOKMARK).build());
        return fragment;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
        setHasOptionsMenu(true);
    }

    public void showRepositories(ArrayList<Repository> repositoryList) {
        Log.i(TAG, "showRepositories: ");
        ((RepositoriesAdapter) this.adapter).setData(repositoryList);
        postNotifyDataSetChanged();
    }

    /* access modifiers changed from: protected */
    public int getLayoutId() {
        Log.i(TAG, "getLayoutId: ");
        return R.layout.fragment_list;
    }

    /* access modifiers changed from: protected */
    public void setupFragmentComponent(AppComponent appComponent) {
        Log.i(TAG, "setupFragmentComponent: ");
        DaggerFragmentComponent.builder().appComponent(appComponent).fragmentModule(new FragmentModule(this)).build().inject(this);
    }

    /* access modifiers changed from: protected */
    public void initFragment(Bundle savedInstanceState) {
        super.initFragment(savedInstanceState);
        Log.i(TAG, "initFragment: ");
        setLoadMoreEnable(!RepositoriesType.COLLECTION.equals(mPresenter.getType()));
    }

    /* access modifiers changed from: protected */
    public void onReLoadData() {
        Log.i(TAG, "onReLoadData: ");
        ((RepositoriesPresenter) this.mPresenter).loadRepositories(true, 1);
    }

    /* access modifiers changed from: protected */
    public String getEmptyTip() {
        Log.i(TAG, "getEmptyTip: ");
        if(RepositoriesType.TRENDING.equals(mPresenter.getType())){
            return String.format(getString(R.string.no_trending_repos), mPresenter.getLanguage().getName());
        }
        return getString(R.string.no_repository);
    }

    public void onItemClick(int position, @NonNull android.view.View view) {
        super.onItemClick(position, view);
        Log.i(TAG, "onItemClick: ");
        if(RepositoriesType.TRENDING.equals(mPresenter.getType())
                || RepositoriesType.TRACE.equals(mPresenter.getType())
                || RepositoriesType.BOOKMARK.equals(mPresenter.getType())
                || RepositoriesType.COLLECTION.equals(mPresenter.getType())){
            RepositoryActivity.show(getActivity(), adapter.getData().get(position).getOwner().getLogin(),
                    adapter.getData().get(position).getName());
        } else {
            RepositoryActivity.show(getActivity(), adapter.getData().get(position));
        }
    }

    /* access modifiers changed from: protected */
    public void onLoadMore(int page) {
        super.onLoadMore(page);
        Log.i(TAG, "onLoadMore: ");
        ((RepositoriesPresenter) this.mPresenter).loadRepositories(false, page);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.i(TAG, "onCreateOptionsMenu: ");
            inflater.inflate(R.menu.menu_my_sjcj, menu);
//                menu.findItem(R.id.action_wiki).setVisible(false);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected: ______");
        Intent intent = new Intent(getContext(), BuildBanbaoSearch.class);
        this.startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    public void onFragmentShowed() {
        super.onFragmentShowed();
        Log.i(TAG, "onFragmentShowed: ");
        if (this.mPresenter != null) {
            ((RepositoriesPresenter) this.mPresenter).prepareLoadData();
        }
    }

    public void onDrawerSelected(@NonNull NavigationView navView, @NonNull MenuItem item) {
        Log.i(TAG, "onDrawerSelected: ");
    }

    public void onLanguageUpdate(TrendingLanguage language) {
        Log.i(TAG, "onLanguageUpdate: ");
        if (this.mPresenter != null) {
            ((RepositoriesPresenter) this.mPresenter).setLanguage(language);
            ((RepositoriesPresenter) this.mPresenter).setLoaded(false);
            ((RepositoriesPresenter) this.mPresenter).prepareLoadData();
            return;
        }
        getArguments().putParcelable("language", language);
    }
}
