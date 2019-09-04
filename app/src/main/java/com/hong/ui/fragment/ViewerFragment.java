

package com.hong.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.hong.AppConfig;
import com.hong.AppData;
import com.hong.R;
import com.hong.inject.component.AppComponent;
import com.hong.inject.component.DaggerFragmentComponent;
import com.hong.inject.module.FragmentModule;
import com.hong.mvp.contract.IViewerContract;
import com.hong.mvp.presenter.ViewerPresenter;
import com.hong.ui.fragment.base.BaseFragment;
import com.hong.ui.widget.webview.ProgressWebView;
import com.hong.util.BundleHelper;
import com.hong.util.NullHelper;
import com.hong.util.PrefUtils;
import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;

import butterknife.BindView;

/**
 * Created by upc_jxzy on 2017/8/19 15:59:55
 */

public class ViewerFragment extends BaseFragment<ViewerPresenter> implements IViewerContract.View, ProgressWebView.ContentChangedListener {



    public static ViewerFragment toUrl(String url, String user) {
        Log.i("=============->", "show: ====================-toUrl");
        ViewerFragment fragment = new ViewerFragment();
        fragment.setArguments(BundleHelper.builder().put("url", url).put("user", user).build());
        return fragment;
    }
    @BindView(R.id.web_view) ProgressWebView webView;
    @BindView(R.id.loader) ProgressBar loader;

    @AutoAccess boolean wrap = false;
    //store scroll y percent, recover position when needed
    @AutoAccess float scrollYPercent = 0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_viewer;
    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {
        Log.i("=============->", "show: ====================-ViewerFragment");
        DaggerFragmentComponent.builder()
                .appComponent(appComponent)
                .fragmentModule(new FragmentModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initFragment(Bundle savedInstanceState) {
        wrap = PrefUtils.isCodeWrap();
        loader.setVisibility(View.VISIBLE);
        loader.setIndeterminate(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
//        MenuItem menuItem = menu.findItem(R.id.action_wrap_lines);
//        MenuItem menuItemDownload = menu.findItem(R.id.action_download);
//        MenuItem menuItemViewFile = menu.findItem(R.id.action_view_file);
//        MenuItem menuItemRefresh = menu.findItem(R.id.action_refresh);
//        if(ViewerActivity.ViewerType.RepoFile.equals(mPresenter.getViewerType())) {
//            menuItem.setVisible(mPresenter.isCode() && !StringUtils.isBlank(mPresenter.getDownloadSource()));
//            menuItem.setChecked(wrap);
//            menuItemDownload.setVisible(!StringUtils.isBlank(mPresenter.getFileModel().getDownloadUrl()));
//            menuItemViewFile.setVisible(false);
//            menuItemRefresh.setVisible(true);
//        } else if(ViewerActivity.ViewerType.DiffFile.equals(mPresenter.getViewerType())){
//            menuItem.setVisible(true);
//            menuItem.setChecked(wrap);
//            menuItemDownload.setVisible(false);
//            menuItemViewFile.setVisible(true);
//            menuItemRefresh.setVisible(false);
//        } else if(ViewerActivity.ViewerType.Image.equals(mPresenter.getViewerType())) {
//            menuItem.setVisible(false);
//            menuItemViewFile.setVisible(false);
//            menuItemRefresh.setVisible(false);
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == R.id.action_wrap_lines){
//            item.setChecked(!item.isChecked());
//            wrap = item.isChecked();
//            PrefUtils.set(PrefUtils.CODE_WRAP, wrap);
//            if(ViewerActivity.ViewerType.RepoFile.equals(mPresenter.getViewerType())){
//                loadCode(mPresenter.getDownloadSource(), mPresenter.getExtension());
//            } else if(ViewerActivity.ViewerType.DiffFile.equals(mPresenter.getViewerType())){
//                loadDiffFile(mPresenter.getCommitFile().getPatch());
//            }
//            return true;
//        } else if(item.getItemId() == R.id.action_refresh){
//            refresh();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadLuUrl(@NonNull String url) {
        Log.i("=====", "loadLuUrl: ____________"+AppData.isLogin);
        if(!AppData.isLogin){
//            System.out.println("查询用户id+"+AppData.INSTANCE.getAuthUser().getName()+"////"+AppData.INSTANCE.getAuthUser().getLoginId());
//            System.out.println("查询用户id+"+AppData.INSTANCE.getLoggedUser().getOilfield()+"////"+AppData.INSTANCE.getLoggedUser().getLogin());
            webView.loadUrl(AppConfig.UPC_API_BASE_URL+"sggl/LoginActionTemp!initLogin?username="+ AppData.INSTANCE.getLoggedUser().getLogin());
            webView.setContentChangedListener(this);
            AppData.isLogin = true;
            url = NullHelper.isNull(url);
            loadLuUrl(url);
            Log.i("====", "loadLuUrl: 载入验证完成__");
        }else{
            Log.i("=============->", "show: ====================-loadLuUrl" + url);
            url = NullHelper.isNull(url);
            loader.setVisibility(View.VISIBLE);
            loader.setIndeterminate(false);
            webView.loadUrl(url);
            webView.setContentChangedListener(this);
        }


    }

    public void refresh() {
        mPresenter.load(true);
    }

    @Override
    public void onContentChanged(int progress) {
        Log.i("=============->", "show: ====================-onContentChanged");
        if (loader != null) {
            loader.setProgress(progress);
            if (progress == 100) {
                loader.setVisibility(View.GONE);
                //delay 300 mills, in case of content height unavailable
                webView.postDelayed(() -> {
                    if (webView == null) return;
                    int scrollY = (int) (webView.getContentHeight() * scrollYPercent);
                    webView.scrollTo(0, scrollY);
                }, 300);
            }
        }
    }

    @Override
    public void onScrollChanged(boolean reachedTop, int scroll) {

    }

    @Override
    public void scrollToTop() {
        super.scrollToTop();
        webView.scrollTo(0, 0);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (webView != null) {
            int scrollY = webView.getScrollY();
            scrollYPercent = scrollY * 1.0f / webView.getContentHeight();
        }
    }
}
