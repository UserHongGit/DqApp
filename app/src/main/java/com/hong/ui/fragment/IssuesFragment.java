package com.hong.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;


import com.hong.R;
import com.hong.inject.component.AppComponent;
import com.hong.inject.component.DaggerFragmentComponent;
import com.hong.inject.module.FragmentModule;
import com.hong.mvp.contract.IIssuesContract;
import com.hong.mvp.model.Issue;
import com.hong.mvp.model.filter.IssuesFilter;
import com.hong.mvp.presenter.IssuePresenter;
import com.hong.ui.activity.IssuesActivity;
import com.hong.ui.adapter.IssuesAdapter;
import com.hong.ui.fragment.base.ListFragment;
import com.hong.util.BundleHelper;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/9/20 15:00:12
 */

public class IssuesFragment extends ListFragment<IssuePresenter, IssuesAdapter>
        implements IIssuesContract.View, IssuesActivity.IssuesListListener {

    public static IssuesFragment createForRepo(@NonNull Issue.IssueState issueState,
                                               @NonNull String userId, @NonNull String repoName){
        IssuesFragment fragment = new IssuesFragment();
        fragment.setArguments(BundleHelper.builder()
                .put("issuesFilter", new IssuesFilter(IssuesFilter.Type.Repo, issueState))
                .put("userId", userId)
                .put("repoName", repoName).build());
        return fragment;
    }

    public static IssuesFragment createForUser(@NonNull Issue.IssueState issueState){
        IssuesFragment fragment = new IssuesFragment();
        fragment.setArguments(BundleHelper.builder()
                .put("issuesFilter", new IssuesFilter(IssuesFilter.Type.User, issueState))
                .put("issueState", issueState).build());
        return fragment;
    }

    @Override
    protected int getLayoutId() {
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
    protected void initFragment(Bundle savedInstanceState) {
        super.initFragment(savedInstanceState);
        setLoadMoreEnable(true);
        adapter.setUserIssues(mPresenter.getIssuesFilter().getType().equals(IssuesFilter.Type.User));
    }

    @Override
    protected void onReLoadData() {
        mPresenter.loadIssues(1, true);
    }

    @Override
    protected String getEmptyTip() {
        return getString(R.string.no_issues);
    }

    @Override
    public void showIssues(ArrayList<Issue> issues) {
        adapter.setData(issues);
        postNotifyDataSetChanged();
    }

    @Override
    protected void onLoadMore(int page) {
        super.onLoadMore(page);
        mPresenter.loadIssues(page, false);
    }

    @Override
    public void onItemClick(int position, @NonNull View view) {
        super.onItemClick(position, view);
        Issue issue = adapter.getData().get(position);
        View avatarView = view.findViewById(R.id.user_avatar);
        View titleView = view.findViewById(R.id.issue_title);
//        IssueDetailActivity.show(getActivity(), avatarView, titleView, issue);
    }

    @Override
    public void onFragmentShowed() {
        super.onFragmentShowed();
        if(mPresenter != null) mPresenter.prepareLoadData();
    }

    @Override
    public void onIssuesFilterChanged(@NonNull IssuesFilter issuesFilter) {
        mPresenter.loadIssues(issuesFilter, 1, true);
    }

    @Override
    public void onCreateIssue(@NonNull Issue issue) {
        adapter.getData().add(0, issue);
        adapter.notifyItemInserted(0);
    }

}
