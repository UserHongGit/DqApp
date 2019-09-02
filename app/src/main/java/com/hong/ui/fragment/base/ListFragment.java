

package com.hong.ui.fragment.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.hong.R;
import com.hong.mvp.contract.base.IBaseContract;
import com.hong.ui.adapter.base.BaseAdapter;
import com.hong.ui.adapter.base.BaseViewHolder;
import com.hong.ui.adapter.base.CatchableLinearLayoutManager;
import com.hong.util.NetHelper;
import com.hong.util.PrefUtils;
import com.hong.util.ViewUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created on 2019/5/20.
 *
 * @author upc_jxzy
 */

public abstract class ListFragment<P extends IBaseContract.Presenter, A extends BaseAdapter>
        extends BaseFragment<P> implements IBaseContract.View,
        BaseViewHolder.OnItemClickListener,
        BaseViewHolder.OnItemLongClickListener,
        SwipeRefreshLayout.OnRefreshListener{

    private static String TAG = ListFragment.class.getSimpleName()+"============";


    @BindView(R.id.refresh_layout) protected SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recycler_view) protected RecyclerView recyclerView;
    @Inject protected A adapter;
    private RecyclerView.AdapterDataObserver observer;

    @BindView(R.id.lay_tip) LinearLayout layTip;
    @BindView(R.id.retry_bn)
    Button retry_bn;
    @BindView(R.id.tv_tip) TextView tvTip;
    @BindView(R.id.error_image)
    AppCompatImageView errorImage;

    private int curPage = 1;

    private boolean refreshEnable = true;
    private boolean loadMoreEnable = false;
    private boolean canLoadMore = false;
    private boolean autoJudgeCanLoadMoreEnable = true;
    private boolean isLoading = false;
    private final int DEFAULT_PAGE_SIZE = 30;

    @Override
    protected void initFragment(Bundle savedInstanceState) {
        Log.i(TAG, "initFragment: ");
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(ViewUtils.getRefreshLayoutColors(getContext()));

        recyclerView.setLayoutManager(new CatchableLinearLayoutManager(getActivity()));
        adapter.setOnItemLongClickListener(this);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        layTip.setVisibility(View.GONE);

        //adapter 数据观察者，当数据为空时，显示空提示
        observer = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                Log.i(TAG, "onChanged: ");
                int itemCount = adapter.getItemCount();
                if (itemCount == 0) {
                    refreshLayout.setVisibility(View.GONE);
                    layTip.setVisibility(View.VISIBLE);
                    tvTip.setText(getEmptyTip());
                    errorImage.setVisibility(View.GONE);
                } else {
                    refreshLayout.setVisibility(View.VISIBLE);
                    layTip.setVisibility(View.GONE);
                    itemCount -= getHeaderSize();
                    if(loadMoreEnable && autoJudgeCanLoadMoreEnable){
                        canLoadMore = itemCount % getPagerSize() == 0 ;
//                        curPage = itemCount % getPagerSize() == 0 ?
//                                itemCount / getPagerSize() : (itemCount / getPagerSize()) + 1;
                    }
                }
            }
        };
        adapter.registerAdapterDataObserver(observer);
        recyclerView.setOnScrollListener(new ScrollListener());
        refreshLayout.setRefreshing(true);
        initScrollListener();
    }

    private class ScrollListener extends RecyclerView.OnScrollListener{
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            Log.i(TAG, "onScrolled: ");
            if(!loadMoreEnable || !canLoadMore || isLoading ||
                    !NetHelper.INSTANCE.getNetEnabled()) return;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            //only LinearLayoutManager can find last visible
            if(layoutManager instanceof LinearLayoutManager){
                LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                int lastPosition = linearManager.findLastVisibleItemPosition();
                if(lastPosition == adapter.getItemCount() - 1){
                    onLoadMore(++curPage);
                }
            }
        }
    }

    public int getVisibleItemCount(){
        Log.i(TAG, "getVisibleItemCount: ");
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        //only LinearLayoutManager can find last visible
        if(layoutManager instanceof LinearLayoutManager){
            LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
            int firstPosition = linearManager.findFirstVisibleItemPosition();
            int lastPosition = linearManager.findLastVisibleItemPosition();
            return lastPosition - firstPosition + 1;
        }else {
            throw new UnsupportedOperationException("only for Linear RecyclerView ");
        }
    }

    public int getItemCount(){
        return adapter.getItemCount();
    }

    @Override
    public void onItemClick(int position, @NonNull View view) {

    }

    @Override
    public boolean onItemLongClick(int position, @NonNull View view) {
        return false;
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        curPage = 1;
        onReLoadData();
    }

    @OnClick(R.id.retry_bn)
    public void onRetryClick(@NonNull View view) {
        Log.i(TAG, "onRetryClick: ");
        refreshLayout.setVisibility(View.VISIBLE);
        layTip.setVisibility(View.GONE);
        refreshLayout.setRefreshing(true);
        curPage = 1;
        onReLoadData();
    }

    protected void setErrorTip(String errorTip){
        Log.i(TAG, "setErrorTip: ");
        refreshLayout.setVisibility(View.GONE);
        errorImage.setVisibility(View.VISIBLE);
        layTip.setVisibility(View.VISIBLE);
        tvTip.setText(errorTip);
    }
    protected void setErrorTip2(String article){
        Log.i(TAG, "setErrorTip2: 显示文章");
        refreshLayout.setVisibility(View.GONE);
        errorImage.setVisibility(View.GONE);
        layTip.setVisibility(View.VISIBLE);
        tvTip.setText(article);
        retry_bn.setVisibility(View.GONE);
    }

    protected void addVerticalDivider(){
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }

    /**
     * load more switch
     * @param loadMoreEnable flag
     */
    public void setLoadMoreEnable(boolean loadMoreEnable) {
        this.loadMoreEnable = loadMoreEnable;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    public void setRefreshEnable(boolean refreshEnable) {
        this.refreshEnable = refreshEnable;
        refreshLayout.setEnabled(refreshEnable);
    }

    public void setAutoJudgeCanLoadMoreEnable(boolean autoJudgeLoadMoreEnable) {
        this.autoJudgeCanLoadMoreEnable = autoJudgeLoadMoreEnable;
        canLoadMore = !autoJudgeLoadMoreEnable;
    }

    public int getCurPage() {
        return curPage;
    }

    @Override
    public void showLoading() {
        isLoading = true;
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        isLoading = false;
        refreshLayout.setRefreshing(false);
    }

    protected abstract void onReLoadData();

    protected abstract String getEmptyTip();

    protected int getPagerSize(){
        return DEFAULT_PAGE_SIZE;
    }

    protected int getHeaderSize(){
        return 0;
    }

    protected void onLoadMore(int page){
        if(page == 3 && PrefUtils.isDoubleClickTitleTipAble()){
            showOperationTip(R.string.double_click_toolbar_tip);
            PrefUtils.set(PrefUtils.DOUBLE_CLICK_TITLE_TIP_ABLE, false);
        }
    }

    public void showLoadError(String error) {
        setErrorTip(error);
    }

    public void showLoadError2(String article) {
        setErrorTip2(article);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(adapter != null && observer != null)
            adapter.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void scrollToTop() {
        super.scrollToTop();
        if(recyclerView != null) recyclerView.scrollToPosition(0);
    }

    protected void postNotifyDataSetChanged(){
        Log.i("=============>","701 - listfragment  postNotifyDataSetChanged");
        adapter.notifyDataSetChanged();
    }

    private ListScrollListener mListScrollListener;

    public void setListScrollListener(ListScrollListener listScrollListener){
        mListScrollListener = listScrollListener;
        initScrollListener();
    }

    private void initScrollListener(){
        if(recyclerView == null || mListScrollListener == null) return;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0){
                    mListScrollListener.onScrollUp();
                } else {
                    mListScrollListener.onScrollDown();
                }
            }
        });
    }

    public interface ListScrollListener{
        void onScrollUp();

        void onScrollDown();
    }

}
