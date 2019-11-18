

package com.hong.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hong.AppConfig;
import com.hong.AppData;
import com.hong.R;
import com.hong.http.model.ZyjdPicEntity;
import com.hong.inject.component.AppComponent;
import com.hong.inject.component.DaggerFragmentComponent;
import com.hong.inject.module.FragmentModule;
import com.hong.mvp.contract.IViewerContract;
import com.hong.mvp.model.imgUpload.ImgConvertName;
import com.hong.mvp.model.imgUpload.SgjdjcUploadEntity;
import com.hong.mvp.presenter.ViewerPresenter;
import com.hong.ui.activity.PicSelectActivity;
import com.hong.ui.fragment.base.BaseFragment;
import com.hong.ui.widget.webview.ProgressWebView;
import com.hong.ui.widget.webview.TbsWebView;
import com.hong.util.BundleHelper;
import com.hong.util.NullHelper;
import com.hong.util.PrefUtils;
import com.hong.util.photoBrowser.ImageBrowseIntent;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;

/**
 * Created by upc_jxzy on 2017/8/19 15:59:55
 */

public class ViewerFragment extends BaseFragment<ViewerPresenter> implements IViewerContract.View, ProgressWebView.ContentChangedListener, TbsWebView.ContentChangedListener {


    private static final int CBS_UPLOAD_CODE = 0x99;
    private static final int DANGER_UPLOAD_CODE = 0x88;
    private static final int JXKC_UPLOAD_CODE = 0x77;
    private static String TAG = ViewerFragment.class.getSimpleName() + "_________-";
    private boolean isError = false;


    public static ViewerFragment toUrl(String url, String user) {
        Log.i("=============->", "show: ====================-toUrl");
        ViewerFragment fragment = new ViewerFragment();
        fragment.setArguments(BundleHelper.builder().put("url", url).put("user", user).build());
        return fragment;
    }

    @BindView(R.id.web_view)
    ProgressWebView webView;
    @BindView(R.id.loader)
    ProgressBar loader;

    @AutoAccess
    boolean wrap = false;
    //store scroll y percent, recover position when needed
    @AutoAccess
    float scrollYPercent = 0;

    @BindView(R.id.ll_control_error)
    LinearLayout control_error;
    @BindView(R.id.loading_over)
    RelativeLayout loading_over;


    //自定义开始
    private ArrayList<String> pathArr;
    private ProgressDialog progressDialog;
    private String jdid, userName, prefix;
    //照片名字转换
    private ImgConvertName imgConvert;
    //HSE隐患上传图片 于金涛
    private String picId,username;
    //勘察上传图片  王志伟
    private String kcid;
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
            Log.i("---------", "selectImage: ____" + jdid + "///////" + userName + "///////" + prefix);
            ViewerFragment.this.jdid = jdid;
            ViewerFragment.this.userName = userName;
            ViewerFragment.this.prefix = prefix;
            startActivityForResult(new Intent(getContext(), PicSelectActivity.class), 1);
        }

        @JavascriptInterface
        public void openImg(String jcid, String jcxm1, String jcxm2, String jcxm3, String tab, String prefix) {
            Log.i("---------", "selectImage: 承包商上传个照片啊____" + jcid + "///////" + jcxm1 + "///////" + prefix);
            imgConvert = new ImgConvertName(jcid, jcxm1, jcxm2, jcxm3, tab, prefix);
            startActivityForResult(new Intent(getContext(), PicSelectActivity.class), CBS_UPLOAD_CODE);
        }

        @JavascriptInterface
        public void danger_upload(String picId,String username) {
            Log.i("---------", "danger_upload: 胖子danger___上传个照片啊____" + picId + "___" + username );
            ViewerFragment.this.picId = picId;
            ViewerFragment.this.username = username;
            startActivityForResult(new Intent(getContext(), PicSelectActivity.class), DANGER_UPLOAD_CODE);
        }

        @JavascriptInterface
        public void jxkc_upload(String picId,String kcid) {
            Log.i("---------", "danger_upload: 王志伟jxkc___上传个照片啊____" + picId + "___" + username );
            ViewerFragment.this.picId = picId;
            ViewerFragment.this.kcid = kcid;
            startActivityForResult(new Intent(getContext(), PicSelectActivity.class), JXKC_UPLOAD_CODE);
        }


        @JavascriptInterface
        public void searchImg(String jdid) {
            Log.i("---------", "searchImg: ____" + jdid);
            mPresenter.searcImages(jdid);
            //这个地方会查询出网络的图片   到时候可能是一个String的list
//            ArrayList<String> imageList = new ArrayList<>();
//            imageList.add("http://img3.duitang.com/uploads/item/201607/15/20160715171249_fmztu.gif");
//            imageList.add("http://i0.hdslb.com/bfs/archive/dfd38947e9b971e06d113425a863e4e7b5715335.jpg");
//            imageList.add("http://npic7.edushi.com/cn/zixun/zh-chs/2017-07/24/4050488-2017072415380279.jpg");
//            ImageBrowseIntent.showUrlImageBrowse(mContext,imageList,0);
        }
        @JavascriptInterface
        public void showPic(String str) {
            Log.i("---------", "searchImg: ____" + str);
            Gson gson = new Gson();
            ArrayList<SgjdjcUploadEntity> li = gson.fromJson(str,new TypeToken<ArrayList<SgjdjcUploadEntity>>(){}.getType());
            ArrayList<String> imageList = new ArrayList<>();

            for (SgjdjcUploadEntity u : li){
                String string = u.getFileuri();
                String substring = string.substring((string.lastIndexOf("\\")+1),string.length());
                imageList.add(AppConfig.UPC_API_BASE_URL+"appImages/"+substring);
            }
            ImageBrowseIntent.showUrlImageBrowse(mContext,imageList,0);
            //这个地方会查询出网络的图片   到时候可能是一个String的list
//            ArrayList<String> imageList = new ArrayList<>();
//            imageList.add("http://img3.duitang.com/uploads/item/201607/15/20160715171249_fmztu.gif");
//            imageList.add("http://i0.hdslb.com/bfs/archive/dfd38947e9b971e06d113425a863e4e7b5715335.jpg");
//            imageList.add("http://npic7.edushi.com/cn/zixun/zh-chs/2017-07/24/4050488-2017072415380279.jpg");
//            ImageBrowseIntent.showUrlImageBrowse(mContext,imageList,0);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != -1) {
            return;
        }
        Iterator<String> itear = null ;
        switch (requestCode) {
            case 1:
                pathArr = intent.getStringArrayListExtra("key");
                if (pathArr.size() <= 0) {
                    Toast.makeText(getContext(), "请选择图片后上传!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Iterator<String> iterator = pathArr.iterator();

                while (iterator.hasNext()) {
                    File file = new File(iterator.next());
                    try {
                        Log.i(TAG, "onActivityResult: 文件是否存在_________" + file.exists());
                        if (file.exists() && file.length() > 0) {
                            mPresenter.uploadImg(file, file.getName(), userName, jdid, prefix);
                        }

                    } catch (Exception e) {
                        System.err.println("上传图片出错!");
                        e.printStackTrace();
                    }
                }
                while (iterator.hasNext()) {
                    Log.i(TAG, "图片路径: ____" + iterator.next());
                }
                break;
            case CBS_UPLOAD_CODE:
                pathArr = intent.getStringArrayListExtra("key");
                if (pathArr.size() <= 0) {
                    Toast.makeText(getContext(), "请选择图片后上传!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Iterator<String> iterator2 = pathArr.iterator();

                while (iterator2.hasNext()) {
                    File file = new File(iterator2.next());
                    try {
                        Log.i(TAG, "onActivityResult: 文件是否存在_________" + file.exists());
                        if (file.exists() && file.length() > 0) {
                            mPresenter.cbs_upload(file, file.getName(), userName, imgConvert.getJcid(), imgConvert.getJcxm1(), imgConvert.getJcxm2(), imgConvert.getJcxm3(), imgConvert.getTab(), imgConvert.getPrefix());
                        }

                    } catch (Exception e) {
                        System.err.println("上传图片出错!");
                        e.printStackTrace();
                    }
                }
                while (iterator2.hasNext()) {
                    Log.i(TAG, "图片路径: ____" + iterator2.next());
                }
                break;
            case DANGER_UPLOAD_CODE:
                pathArr = intent.getStringArrayListExtra("key");
                if (pathArr.size() <= 0) {
                    Toast.makeText(getContext(), "请选择图片后上传!", Toast.LENGTH_SHORT).show();
                    return;
                }

                itear = pathArr.iterator();
                while (itear.hasNext()) {
                    File file = new File(itear.next());
                    try {
                        Log.i(TAG, "onActivityResult: 文件是否存在_________" + file.exists());
                        if (file.exists() && file.length() > 0) {
                            mPresenter.danger_upload(file, file.getName(),picId,username);
                        }

                    } catch (Exception e) {
                        System.err.println("上传图片出错!");
                        e.printStackTrace();
                    }
                }
                while (itear.hasNext()) {
                    Log.i(TAG, "图片路径: ____" + itear.next());
                }
                break;
            case JXKC_UPLOAD_CODE:
                pathArr = intent.getStringArrayListExtra("key");
                if (pathArr.size() <= 0) {
                    Toast.makeText(getContext(), "请选择图片后上传!", Toast.LENGTH_SHORT).show();
                    return;
                }

                itear = pathArr.iterator();
                while (itear.hasNext()) {
                    File file = new File(itear.next());
                    try {
                        Log.i(TAG, "onActivityResult: 文件是否存在_________" + file.exists());
                        if (file.exists() && file.length() > 0) {
                            mPresenter.jxkc_upload(file, file.getName(),picId,kcid);
                        }

                    } catch (Exception e) {
                        System.err.println("上传图片出错!");
                        e.printStackTrace();
                    }
                }
                while (itear.hasNext()) {
                    Log.i(TAG, "图片路径: ____" + itear.next());
                }
                break;

        }


    }

    private void initMyWebView() {
        webView.addJavascriptInterface(new JsInterface_2(getContext()), "AndroidWebView");

    }

    @Override
    public void loading() {
        Log.i("=============->", "show:增加网络状态检测回调===================-loadLuUrl111" + isError);
//        if (!isError) {
//            isSuccess = true;
//            // 回调成功后的相关操作
//            loading_over.setVisibility(View.GONE);
//            control_error.setVisibility(View.GONE);
//            webView.setVisibility(View.VISIBLE);
//        } else {
//            isError = false;
//            loading_over.setVisibility(View.GONE);
        control_error.setVisibility(View.VISIBLE);
        loading_over.setVisibility(View.GONE);
        webView.setVisibility(View.GONE);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("网络连接错误");
//        }
    }

    @Override
    public void setTitle(String title) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
    }

    @Override
    public void startActivityForResult(int type) {
        Log.i("========bb", "包含拍照和相册选择4");
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// 调用android的图库
        startActivityForResult(i, type);
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
        Log.i(TAG, "loadLuUrl: 加载的url是_____" + url);
        loader.setVisibility(View.VISIBLE);
        loader.setIndeterminate(false);

        RelativeLayout online_error = (RelativeLayout) control_error.findViewById(R.id.online_error_btn_retry);
        online_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control_error.setVisibility(View.GONE);
                loading_over.setVisibility(View.VISIBLE);
                Log.i("=============->", "show: ====================-reload" + url);
                webView.reload();
            }
        });

        try {
            if (Build.VERSION.SDK_INT >= 16) {
                Class<?> clazz = webView.getSettings().getClass();
                Method method = clazz.getMethod(
                        "setAllowUniversalAccessFromFileURLs", boolean.class);//利用反射机制去修改设置对象
                if (method != null) {
                    method.invoke(webView.getSettings(), true);//修改设置
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        webView.loadUrl(url);
        webView.setContentChangedListener(this);
    }

    @Override
    public void showInfoToast2(String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toasty.info(getActivity(),message).show();
                    }
                }, 1000);
            }
        });
    }
//      原项目加载
//    @Override
//    public void loadLuUrl(@NonNull String url) {
//        Log.i("=====", "loadLuUrl: ____________"+AppData.isLogin);
//        if(!AppData.isLogin){
////            System.out.println("查询用户id+"+AppData.INSTANCE.getAuthUser().getName()+"////"+AppData.INSTANCE.getAuthUser().getLoginId());
////            System.out.println("查询用户id+"+AppData.INSTANCE.getLoggedUser().getOilfield()+"////"+AppData.INSTANCE.getLoggedUser().getLogin());
//            webView.loadUrl(AppConfig.UPC_API_BASE_URL+"sggl/LoginActionTemp!initLogin?username="+ AppData.INSTANCE.getLoggedUser().getLogin());
//            webView.setContentChangedListener(this);
//            AppData.isLogin = true;
//            loadLuUrl(url);
//            Log.i("====", "loadLuUrl: 载入验证完成__");
//        }else{
//            String newUrl = url;
//            if(url.lastIndexOf("null")!=-1){
//                newUrl = url.replace("null", "sggl/LoginActionTemp!initLogin?username=" + AppData.INSTANCE.getLoggedUser().getLogin());
//            }
//            Log.i("=============->", "show: ====================-loadLuUrl" + newUrl+"////"+AppData.INSTANCE.getLoggedUser().getLogin()+"///"+AppData.INSTANCE.getLoggedUser().getName()+"//"+AppData.INSTANCE.getLoggedUser().getId());
//
//            loader.setVisibility(View.VISIBLE);
//            loader.setIndeterminate(false);
//            webView.loadUrl(newUrl);
//            webView.setContentChangedListener(this);
//        }
//    }

    @Override
    public void showPic(ArrayList<ZyjdPicEntity> rows) {
        if(rows.size()<=0){
            Toasty.normal(getContext(),"暂无图片记录!").show();
            return;
        }
        ArrayList<String> imageList = new ArrayList<>();
        for (ZyjdPicEntity r : rows) {
            imageList.add(AppConfig.UPC_API_BASE_URL + "appImages/" + r.getPresentpicname());
        }
//        imageList.add("http://img3.duitang.com/uploads/item/201607/15/20160715171249_fmztu.gif");
//        imageList.add("http://i0.hdslb.com/bfs/archive/dfd38947e9b971e06d113425a863e4e7b5715335.jpg");
//        imageList.add("http://npic7.edushi.com/cn/zixun/zh-chs/2017-07/24/4050488-2017072415380279.jpg");
        ImageBrowseIntent.showUrlImageBrowse(getContext(), imageList, 0);


    }

    public void refresh() {
        mPresenter.load(true);
    }

    @Override
    public void onContentChanged(int progress) {
        Log.i("=============->", "show: ====================-onContentChanged");
        if (loader != null) {
            loader.setProgress(progress);
            if (progress > 10) {
                loading_over.setVisibility(View.GONE);
            } else {
                loader.setVisibility(View.VISIBLE);
            }
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
