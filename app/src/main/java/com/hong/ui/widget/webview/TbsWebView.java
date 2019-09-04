package com.hong.ui.widget.webview;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.hong.AppApplication;
import com.hong.AppConfig;
import com.hong.R;
import com.hong.util.FileManager;
import com.tencent.smtt.export.external.interfaces.GeolocationPermissionsCallback;

import com.tencent.smtt.export.external.interfaces.GeolocationPermissionsCallback;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
//import android.webkit.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
//import android.webkit.WebResourceRequest;
import com.tencent.smtt.sdk.WebViewClient;
/**
 * 带进度条的webview
 *
 * @author lzf
 */
public class TbsWebView extends WebView implements NestedScrollingChild {
    private Context context;
    private int mLastMotionY;
    private final int[] mScrollOffset = new int[2];
    private final int[] mScrollConsumed = new int[2];
    private int mNestedYOffset;
    private NestedScrollingChildHelper mChildHelper;
    private ContentChangedListener contentChangedListener;
    private boolean isError = false;
//    private Fragment mFragment;

//    public void setView(Fragment fragment) {
//        this.mFragment=fragment;
//    }

    public interface ContentChangedListener {
        void onContentChanged(int progress);

        void onScrollChanged(boolean reachedTop, int scroll);

        void loading();

        void setTitle(String title);

        void startActivityForResult(int type);
    }

    public void setContentChangedListener(ContentChangedListener contentChangedListener) {
        this.contentChangedListener = contentChangedListener;
    }

    public TbsWebView(Context context) {
        super(context);
        init(context);
        init();
    }

    public TbsWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        init();
    }

    public TbsWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
    }

    private void init(final Context context) {
        this.context = context;
        setWebChromeClient(new MyWebChromeClient());
        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, final String url) {
                Log.i("========bb", "shouldOverrideUrlLoading1");
                return super.shouldOverrideUrlLoading(view, url);
//                view.loadUrl(url);
//                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.i("========bb", "onPageStarted");
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
//                if (contentChangedListener != null) {
//                    contentChangedListener.loading();
//                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                // 断网或者网络连接超时
                if (errorCode == ERROR_HOST_LOOKUP || errorCode == ERROR_CONNECT || errorCode == ERROR_TIMEOUT) {
                    if (contentChangedListener != null) {
                        Log.i("=============->", "show:增加网络状态检测回调===================-loadLuUrl111c");
                        contentChangedListener.loading();
                        isError = true;
                    }
//                    view.loadUrl("about:blank"); // 避免出现默认的错误界面
//                    view.loadUrl(mErrorUrl);
                }
            }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                // 这个方法在6.0才出现
                int statusCode = errorResponse.getStatusCode();
                System.out.println("onReceivedHttpError code = " + statusCode);
                if (404 == statusCode || 500 == statusCode) {
                    if (contentChangedListener != null) {
                        Log.i("=============->", "show:增加网络状态检测回调===================-loadLuUrl111a");
                        contentChangedListener.loading();
                        isError = true;
                    }
//                    view.loadUrl("about:blank");// 避免出现默认的错误界面
//                    view.loadUrl(mErrorUrl);
                }
            }
        });
        //这里处理返回键事件
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                Log.i("========bb", "setOnKeyListener");
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (canGoBack()) {
                            goBack();
                            return true;
                        }
                    }
                }
                return false;
            }
        });
//        getView().setOnTouchListener(new OnTouchListener() {//这里打开是 滑动隐藏toolbar
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                {
//                    boolean result = false;
//                    MotionEvent trackedEvent = MotionEvent.obtain(event);
//                    final int action = MotionEventCompat.getActionMasked(event);
//                    Log.i("======a", "1");
//                    if (action == MotionEvent.ACTION_DOWN) {
//                        mNestedYOffset = 0;
//                    }
//                    int y = (int) event.getY()+10;
//                    Log.i("========bb110", "setOnKeyListener"+"[]"+y);
//                    event.offsetLocation(0, mNestedYOffset);
//                    switch (action) {
//                        case MotionEvent.ACTION_DOWN:
//                            mLastMotionY = y;
//                            startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
//                            result = v.onTouchEvent(event);
//                            break;
//                        case MotionEvent.ACTION_MOVE:
//                            int deltaY = mLastMotionY - y;
//                            Log.i("========bb110", "setOnKeyListener");
//                            if (dispatchNestedPreScroll(0, deltaY, mScrollConsumed, mScrollOffset)) {
////                                deltaY -= mScrollConsumed[1];
////                                trackedEvent.offsetLocation(0, mScrollOffset[1]);
////                                mNestedYOffset += mScrollOffset[1];
////                                Log.i("========bb111", "setOnKeyListener");
//                            }
//
////                            int oldY = getScrollY();
////                            mLastMotionY = y - mScrollOffset[1];
////                            if (deltaY < 0) {
////                                int newScrollY = Math.max(0, oldY + deltaY);
////                                deltaY -= newScrollY - oldY;
////                                if (dispatchNestedScroll(0, newScrollY - deltaY, 0, deltaY, mScrollOffset)) {
////                                    mLastMotionY -= mScrollOffset[1];
////                                    trackedEvent.offsetLocation(0, mScrollOffset[1]);
////                                    mNestedYOffset += mScrollOffset[1];
////                                }
////                            }
////
////                            trackedEvent.recycle();
//                            result = v.onTouchEvent(trackedEvent);
//                            break;
//                        case MotionEvent.ACTION_POINTER_DOWN:
//                        case MotionEvent.ACTION_UP:
//                        case MotionEvent.ACTION_CANCEL:
//                            Log.i("======a", "16");
//                            stopNestedScroll();
//                            result = v.onTouchEvent(event);
//                            break;
//                    }
//                    return result;
//                }
//            }
//        });
        this.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void pdfView(final String u) {
                Log.i("=============>FDF", AppConfig.UPC_API_BASE_URL + u);
//                Intent it = new Intent(context, PdfActivity.class);
//                it.putExtra("u",u);
//                context.startActivity(it);
            }
            @JavascriptInterface
            public String getBaseUrl() {
                return AppConfig.UPC_API_BASE_URL;
            }
//            @JavascriptInterface
//            public String getBaseUrl_DQ() {
//                return AppConfig.DQ_API_BASE_URL;
//            }
        }, "adjs");
        this.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        this.getView().setClickable(true);
        WebSettings webSetting = this.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setAppCachePath(getContext().getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(getContext().getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(getContext().getDir("geolocation", 0).getPath());
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
//         webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);

        //增加
//        webSetting.setTextSize(WebSettings.TextSize.NORMAL);
        //支持混合模式
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            webSetting.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }
        //接口禁止(直接或反射)调用，避免视频画面无法显示：
//        setLayerType(View.LAYER_TYPE_SOFTWARE,null);
//        setDrawingCacheEnabled(true);
        // this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
        // settings 的设计
        CookieSyncManager.createInstance(getContext());
        CookieSyncManager.getInstance().sync();

//        WebSettings webSettings = getSettings();
//        webSettings.setAllowFileAccess(true);// 设置允许访问文件数据
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setDatabaseEnabled(true);
//
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        // 屏幕自适应
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
//        webSettings.setUseWideViewPort(true);
//        webSettings.setLoadWithOverviewMode(true);
//        webSettings.setSupportZoom(true);
//        webSettings.setJavaScriptEnabled(true);
    }

    private Uri fileUri;
    public static final int TYPE_REQUEST_PERMISSION = 3;
    public static final int TYPE_CAMERA = 1;
    public static final int TYPE_GALLERY = 2;

    /**
     * 包含拍照和相册选择
     */
    public void showOptions() {
        Log.i("========bb", "包含拍照和相册选择1");
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setOnCancelListener(new ReOnCancelListener());
        alertDialog.setTitle("请选择图片来源");
        alertDialog.setItems(R.array.options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Log.i("========bb", "包含拍照和相册选择2");
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // 申请WRITE_EXTERNAL_STORAGE权限
                        Log.i("========bb", "包含拍照和相册选择3");
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, TYPE_REQUEST_PERMISSION);
                    } else {
                        toCamera();

                    }
                } else {
                    contentChangedListener.startActivityForResult(TYPE_GALLERY);
//                    Log.i("========bb","包含拍照和相册选择4");
//                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// 调用android的图库
//                    mFragment.startActivityForResult(i, TYPE_GALLERY);
                }
            }
        });
        alertDialog.show();
    }

    /**
     * 图片选择回调
     */
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    public static final int SELECT_PIC_BY_TACK_PHOTO = 100;

    class MyWebChromeClient extends WebChromeClient {
        // 配置权限（同样在WebChromeClient中实现）
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissionsCallback callback) {
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            Log.i("=============>", "aa - onReceivedTitle" + title);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                if (title.contains("404") || title.contains("500") || title.contains("Error") || title.contains("无法打开")) {
                    if (contentChangedListener != null) {
                        Log.i("=============->", "show:增加网络状态检测回调===================-loadLuUrl111b");
                        contentChangedListener.loading();
                        isError = true;
                    }
//                    view.loadUrl("about:blank");// 避免出现默认的错误界面
//                    view.loadUrl(mErrorUrl);
                }
            }
            if (title != null) {
                if (contentChangedListener != null) {
                    if (isError) {
                        title = "网络连接错误";
                        contentChangedListener.setTitle(title);
                    }
                }
            }
        }

        @Override
        public void onProgressChanged(WebView view, int progress) {
            super.onProgressChanged(view, progress);
            if (contentChangedListener != null) {
                contentChangedListener.onContentChanged(progress);
            }
        }

        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUploadMessage = uploadMsg;
            showOptions();
        }

        // For Android > 4.1.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUploadMessage = uploadMsg;
            showOptions();
        }

        // For Android > 5.0支持多张上传
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg, FileChooserParams fileChooserParams) {
            mUploadCallbackAboveL = uploadMsg;
            Log.i("========bb", "包含拍照和相册选择0");
            showOptions();
            return true;
        }
    }

    private static Uri getOutputMediaFileUri() {
        return Uri.fromFile(FileManager.getImgFile(AppApplication.get().getApplicationContext()));
    }

//    @Override
//    public void setOnKeyListener(OnKeyListener l) {
//        Log.i("========bb", "setOnKeyListener111");
//        super.setOnKeyListener(l);
//    }

    private class ReOnCancelListener implements DialogInterface.OnCancelListener {

        @Override
        public void onCancel(DialogInterface dialogInterface) {
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(null);
                mUploadMessage = null;
            }
            if (mUploadCallbackAboveL != null) {
                mUploadCallbackAboveL.onReceiveValue(null);
                mUploadCallbackAboveL = null;
            }
        }
    }

    // 请求拍照
    public void toCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 调用android的相机
        // 创建一个文件保存图片
        fileUri = getOutputMediaFileUri();
        Log.d("MainActivity", "fileUri=" + fileUri);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        contentChangedListener.startActivityForResult(TYPE_CAMERA);
//        mFragment.startActivityForResult(intent, TYPE_CAMERA);
    }

    /**
     * 回调到网页
     *
     * @param isCamera
     * @param uri
     */
    public void onActivityCallBack(boolean isCamera, Uri uri) {
        if (isCamera) {
            uri = fileUri;
        }

        if (mUploadCallbackAboveL != null) {
            Uri[] uris = new Uri[]{uri};
            mUploadCallbackAboveL.onReceiveValue(uris);
            mUploadCallbackAboveL = null;
        } else if (mUploadMessage != null) {
            mUploadMessage.onReceiveValue(uri);
            mUploadMessage = null;
        } else {
            Toast.makeText(context, "无法获取数据", Toast.LENGTH_LONG).show();
        }
    }
//
//    // NestedScrollingChild
//
//    @Override
//    public void setNestedScrollingEnabled(boolean enabled) {
//        mChildHelper.setNestedScrollingEnabled(enabled);
//    }
//
//    @Override
//    public boolean isNestedScrollingEnabled() {
//        return mChildHelper.isNestedScrollingEnabled();
//    }
//
//    @Override
//    public boolean startNestedScroll(int axes) {
//        return mChildHelper.startNestedScroll(axes);
//    }
//
//    @Override
//    public void stopNestedScroll() {
//        mChildHelper.stopNestedScroll();
//    }
//
//    @Override
//    public boolean hasNestedScrollingParent() {
//        return mChildHelper.hasNestedScrollingParent();
//    }
//
//    @Override
//    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
//        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
//    }
//
//    @Override
//    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
//        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
//    }
//
//    @Override
//    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
//        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
//    }
//
//    @Override
//    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
//        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
//    }

}
