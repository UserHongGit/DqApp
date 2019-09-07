

package com.hong.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hong.AppConfig;
import com.hong.AppData;
import com.hong.R;
import com.hong.inject.component.AppComponent;
import com.hong.inject.component.DaggerFragmentComponent;
import com.hong.inject.module.FragmentModule;
import com.hong.mvp.contract.IViewerContract;
import com.hong.mvp.presenter.ViewerPresenter;
import com.hong.ui.activity.PicSelectActivity;
import com.hong.ui.fragment.base.BaseFragment;
import com.hong.ui.widget.webview.ProgressWebView;
import com.hong.util.BundleHelper;
import com.hong.util.NullHelper;
import com.hong.util.PrefUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;

/**
 * Created by upc_jxzy on 2017/8/19 15:59:55
 */

public class ViewerFragment extends BaseFragment<ViewerPresenter> implements IViewerContract.View, ProgressWebView.ContentChangedListener {


    private static String TAG = ViewerFragment.class.getSimpleName()+"_________-";

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


    //自定义开始
    private ArrayList<String> pathArr;
    private ProgressDialog progressDialog;
    private String jdid,  userName,  prefix;
    //自定义结束





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

        initMyWebView();
    }

    private class JsInterface_2 {
        private Context mContext;

        public JsInterface_2(Context context) {
            this.mContext = context;
        }

        @JavascriptInterface
        public void selectImage(String jdid, String userName, String prefix) {
            Log.i("---------", "selectImage: ____"+jdid+"///////"+userName+"///////"+prefix);
            ViewerFragment.this.jdid = jdid;
            ViewerFragment.this.userName = userName;
            ViewerFragment.this.prefix = prefix;
            startActivityForResult(new Intent(getContext(), PicSelectActivity.class), 1);
        }
        @JavascriptInterface
        public void searchImg(String jdid) {
            Log.i("---------", "searchImg: ____"+jdid);



        }






    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != -1) {
            return;
        }
        if(requestCode == 1){
            pathArr = intent.getStringArrayListExtra("key");
            if(pathArr.size() <= 0){
                Toast.makeText(getContext(),"请选择图片后上传!",Toast.LENGTH_SHORT).show();
                return;
            }
            Iterator<String> iterator = pathArr.iterator();

            while (iterator.hasNext()){
                System.out.println("进来没__________-");
                File file = new File(iterator.next());
                try{
                    Log.i(TAG, "onActivityResult: 文件是否存在_________"+file.exists());
                    if(file.exists() && file.length() > 0){
                        mPresenter.uploadImg(file,file.getName(),userName,jdid,prefix);
                    }

                }catch (Exception e){
                    System.err.println("上传图片出错!");
                    e.printStackTrace();
                }
            }
            while (iterator.hasNext()){
                Log.i(TAG, "图片路径: ____"+iterator.next());
            }

        }



    }

    private void initMyWebView(){
        webView.addJavascriptInterface(new JsInterface_2(getContext()), "AndroidWebView");

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
