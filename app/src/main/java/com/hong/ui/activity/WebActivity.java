package com.hong.ui.activity;

import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;

import com.bumptech.glide.Glide;
import com.hong.AppConfig;
import com.hong.http.model.UMenu;
import com.hong.ui.fragment.ViewerFragment;
import com.hong.ui.widget.webview.ProgressWebView;
import com.hong.util.photoBrowser.GlideHelper;
import com.tencent.bugly.beta.Beta;
import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.hong.AppData;
import com.hong.R;
//import com.hong.common.GlideApp;
import com.hong.inject.component.AppComponent;
import com.hong.inject.component.DaggerActivityComponent;
import com.hong.inject.module.ActivityModule;
import com.hong.mvp.contract.IWebContract.View;
import com.hong.mvp.model.User;
import com.hong.mvp.model.filter.RepositoriesFilter;
import com.hong.mvp.presenter.WebPresenter;
import com.hong.ui.activity.base.BaseDrawerActivity;
import com.hong.ui.fragment.ActivityFragment;
import com.hong.ui.fragment.ActivityFragment.ActivityType;
import com.hong.ui.fragment.RepositoriesFragment;
import com.hong.ui.fragment.RepositoriesFragment.RepositoriesType;
import com.hong.util.PrefUtils;
import com.hong.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebActivity extends BaseDrawerActivity<WebPresenter> implements View {
    private final List<Integer> FRAGMENT_NAV_ID_LIST = Arrays.asList(new Integer[]{ Integer.valueOf(R.id.nav_public_news)});
    private final List<String> FRAGMENT_TAG_LIST = Arrays.asList(new String[]{ActivityType.News.name(), "web_view1", "web_view2", "nav_profile2"});
    private final List<Integer> FRAGMENT_TITLE_LIST = Arrays.asList(new Integer[]{Integer.valueOf(R.string.news), Integer.valueOf(R.string.web_view1), Integer.valueOf(R.string.web_view2)});
    private final int SETTINGS_REQUEST_CODE = 100;
    private String TAG;
    private final Map<Integer, String> TAG_MAP = new HashMap();
    @BindView(R.id.frame_layout_content)
    FrameLayout frameLayoutContent;
    private boolean isAccountsAdded = false;
    private boolean isManageAccount;
    @AutoAccess
    int selectedPage;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    private AppCompatImageView toggleAccountBn;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.menu_item_setting)
    LinearLayout setting;
    @BindView(R.id.menu_item_exit)
    LinearLayout exit;
    private ProgressWebView mWebView;

    public enum ViewerType {
        RepoFile,
        MarkDown,
        DiffFile,
        Image,
        HtmlSource
    }

    public WebActivity() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(WebActivity.class.getSimpleName());
        stringBuilder.append("===========");
        this.TAG = stringBuilder.toString();
        for (int i = 0; i < this.FRAGMENT_NAV_ID_LIST.size(); i++) {
            this.TAG_MAP.put(this.FRAGMENT_NAV_ID_LIST.get(i), this.FRAGMENT_TAG_LIST.get(i));
        }
        this.isManageAccount = false;
    }

    /* access modifiers changed from: protected */
    public void setupActivityComponent(AppComponent appComponent) {
        Log.i("=============>", "8 - web setupActivityComponent");
        DaggerActivityComponent.builder()
                .appComponent(appComponent)
                .activityModule(new ActivityModule(getActivity()))
                .build()
                .inject(this);
    }

    /* access modifiers changed from: protected */
    public void initActivity() {
        Log.i("=============>", "9 - web initActivity");
        super.initActivity();


        AppData.INSTANCE.getLoggedUser();
        setStartDrawerEnable(true);
        setEndDrawerEnable(true);
    }

    /* access modifiers changed from: protected */
    public int getContentView() {
        Log.i("=============>", "10 - web getContentView");
        return R.layout.activity_web;
    }

    /* access modifiers changed from: protected */
    public void initView(Bundle savedInstanceState) {
        Log.i(TAG, "11 - web initView"+AppData.INSTANCE.getAccessToken());
        super.initView(savedInstanceState);
//        动态查询菜单
//        mPresenter.getMenu(AppData.INSTANCE.getAccessToken());

        //初始化setting按钮
        setting.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                closeDrawer();
                Log.i(TAG, "onClick: 给按钮Setting设置监听____");
                SettingsActivity.show(getActivity(),SETTINGS_REQUEST_CODE);
            }
        });
        exit.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                closeDrawer();
                Log.i(TAG, "onClick: 给按钮Exit设置监听");
                logout();
            }
        });


        setToolbarScrollAble(true);//打开toolbar 向上滑动隐藏
        //左侧滑动菜单通过这个方法渲染,activity_web_drawer存储的菜单的列表名称
//        updateStartDrawerContent(R.layout.activity_web_drawer);
//        removeEndDrawer();
        if (mPresenter.isFirstUseAndNoNewsUser()) {//第一次打开时
//            this.selectedPage = R.id.nav_public_news;
            Log.i(TAG, "initView: 第一次打开___-");
            selectedPage = 10000;
            updateFragmentByNavId(this.selectedPage,null);
        } else {
            int i = this.selectedPage;
            if (i != 0) {
                Log.i(TAG, "initView: _____不等于00");
                updateFragmentByNavId(i,null);
            }else if(selectedPage != 0 ){
                Log.i(TAG, "initView:  web FRAGMENT_NAV_ID_LIST selectedPage != 0");
                updateFragmentByNavId(selectedPage,null);
            } else {
//                int startPageIndex = Arrays.asList(getResources().getStringArray(R.array.start_pages_id)).indexOf(PrefUtils.getStartPage());
//                TypedArray typedArray = getResources().obtainTypedArray(R.array.start_pages_nav_id);
//                int startPageNavId = typedArray.getResourceId(startPageIndex, 0);
//                typedArray.recycle();
//                if (this.FRAGMENT_NAV_ID_LIST.contains(Integer.valueOf(startPageNavId))) {
//                    Log.i(TAG, "11 - web FRAGMENT_NAV_ID_LIST.contains(startPageNavId)");
//                    this.selectedPage = startPageNavId;
//                    updateFragmentByNavId(this.selectedPage,null);
//                } else {
                String startPageId = PrefUtils.getStartPage();
                int startPageIndex = Arrays.asList(getResources().getStringArray(R.array.start_pages_id))
                        .indexOf(startPageId);
                TypedArray typedArray = getResources().obtainTypedArray(R.array.start_pages_nav_id);
                int startPageNavId = typedArray.getResourceId(startPageIndex, 0);
                typedArray.recycle();
                    Log.i(TAG, "11 - web !FRAGMENT_NAV_ID_LIST.contains(startPageNavId)");
                    this.selectedPage = 1;
                    updateFragmentByNavId(this.selectedPage,"main");
//                    updateFragmentByNavId(startPageNavId,null);
//                }
            }
        }

        TextView mail = (TextView)navViewStart.findViewById(R.id.mail);
        TextView name = (TextView)navViewStart.findViewById(R.id.name);
        ImageView avatar = (ImageView)navViewStart.findViewById(R.id.avatar);
        mWebView = navViewStart.findViewById(R.id.webview_menu);

        try {
            if (Build.VERSION.SDK_INT >= 16) {
                Class<?> clazz = mWebView.getSettings().getClass();
                Method method = clazz.getMethod(
                        "setAllowUniversalAccessFromFileURLs", boolean.class);//利用反射机制去修改设置对象
                if (method != null) {
                    method.invoke(mWebView.getSettings(), true);//修改设置
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mWebView.loadUrl("file:///android_asset/web/left.html");
//        mWebView.loadUrl(AppConfig.UPC_API_BASE_URL+"JumpController/left?user="+AppData.INSTANCE.getAuthUser().getLoginId());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
//        mWebView.loadUrl("javascript:test()");
        mWebView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public String getParam() {
                return AppData.INSTANCE.getLoggedUser().getLogin();
            }

            @JavascriptInterface
            public void action(final String u,final String title) {
                Log.i("=============>", "9 - web JavascriptInterface" + u);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setToolbarTitle(title);
                        updateFragmentByNavId(0,u);
                        drawerLayout.closeDrawers();
//                        mWebView.loadUrl("javascript:test()");//调用页面js的方法
                    }
                });

            }
            @JavascriptInterface
            public String getBaseUrl() {
                return AppConfig.UPC_API_BASE_URL;
            }
        }, "adjs");


        this.toggleAccountBn = (AppCompatImageView) this.navViewStart.getHeaderView(0).findViewById(R.id.toggle_account_bn);
        toggleAccountBn.setOnClickListener(v -> {
            toggleAccountLay();
        });
        toggleAccountBn.setVisibility(android.view.View.GONE);

        User loginUser = AppData.INSTANCE.getLoggedUser();

        Glide.with(getActivity()).asBitmap().load(AppConfig.UPC_API_BASE_URL+"appImages/userAv_noAv.jpg").into(avatar);
        name.setText(StringUtils.isBlank(loginUser.getName()) ? loginUser.getLogin() : loginUser.getName());
        Calendar c = Calendar.getInstance();
        SimpleDateFormat f = new SimpleDateFormat("yy.MM.dd");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(f.format(c.getTime()));
        stringBuilder.append("  "+c.get(Calendar.HOUR_OF_DAY)+":"+ c.get(Calendar.MINUTE));
        mail.setText(StringUtils.isBlank(loginUser.getBio()) ? "登录时间: ".concat(stringBuilder.toString()) : loginUser.getBio());
        this.tabLayout.setVisibility(android.view.View.GONE);

        //        检测更新
        Beta.checkUpgrade();
    }

    public /* synthetic */ void lambda$initView$0$WebActivity(android.view.View v) {
        toggleAccountLay();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(this.TAG, "onCreateOptionsMenu: 初始化右上角菜单");
//        getMenuInflater().inflate(R.menu.menu_sort, menu);
//        MenuItem menuItem = menu.findItem(R.id.nav_sort);
        int i = this.selectedPage;
//        boolean z = i == R.id.nav_owned || i == R.id.nav_starred;
//        menuItem.setVisible(z);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.i(this.TAG, "onPrepareOptionsMenu: ");
        invalidateMainMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    /* access modifiers changed from: protected */
    public boolean isEndDrawerMultiSelect() {
        Log.i(this.TAG, "isEndDrawerMultiSelect: ");
        return true;
    }

    /* access modifiers changed from: protected */
    public int getEndDrawerToggleMenuItemId() {
        Log.i(this.TAG, "getEndDrawerToggleMenuItemId: ");
        return R.id.nav_sort;
    }

    /* access modifiers changed from: protected */
    public void onNavItemSelected(@NonNull MenuItem item, boolean isStartDrawer) {
        super.onNavItemSelected(item, isStartDrawer);
        String url = "";
        if(null!=item.getIntent()){
             url = item.getIntent().getStringExtra("url");
            Log.i(this.TAG, url+"onNavItemSelected:   这是菜单列表点击时候触发的事件,这个item.getItemId就是我实际自定义对应BaseDrawerAvctivity的menuId"+item.getItemId()+"///"+item.getTitle());
        }
        if (isStartDrawer) {
            updateFragmentByNavId(item.getItemId(),"m/"+url+".jsp");
        } else {
            handlerEndDrawerClick(item);
        }
    }

    private void updateFragmentByNavId(int id,String url) {
        Log.i(this.TAG, "updateFragmentByNavId: "+url+"____"+id+"_____"+AppData.isLogin);
        if(null!=url)
            if(!AppData.isLogin){
//                原始大庆登录
//            url = AppConfig.UPC_API_BASE_URL+"sggl/LoginActionTemp!initLogin?username="+ AppData.INSTANCE.getLoggedUser().getLogin();

                url = AppConfig.UPC_API_BASE_URL+"common/initLogin?username="+ AppData.INSTANCE.getLoggedUser().getLogin();
                AppData.isLogin = true;
            }else{
                Log.i(TAG, "updateFragmentByNavId: 加载的url__"+url);
                if(!url.contains("m.")){
                    Log.i(TAG, "updateFragmentByNavId: 加载本地资源");
                    url="file:///android_asset/web/"+url+".html";
                }else{
                    Log.i(TAG, "updateFragmentByNavId: 网络请求");
                    url=AppConfig.UPC_API_BASE_URL+"JumpController/"+url+"";
                }
            }
        Log.i("=============>", "001 - web updateFragmentByNavId" + id);
        if(url==null){
            url="about:blank";
            return;
        }

        loadFragment(id,url);
//        TEMP
//        if (this.FRAGMENT_NAV_ID_LIST.contains(Integer.valueOf(id))) {
//            Log.i(this.TAG, "updateFragmentByNavId: 三连杀"+id);
//            updateTitle(id);
//            loadFragment(id,null);
//            updateFilter(id);
//            return;
//        }
//        switch (id) {
//            case R.id.nav_add_account /*2131296471*/:
//                showLoginPage();
//                break;
////            case R.id.nav_issues /*2131296478*/:
////                Log.i(this.TAG, "updateFragmentByNavId: 我尼玛,这个才是问题____第四个___");
////                IssuesActivity.showForUser(getActivity());
////                break;
//            case R.id.nav_logout /*2131296480*/:
//                logout();
//                break;
////            case R.id.nav_notifications /*2131296484*/:
////                Log.i(this.TAG, "updateFragmentByNavId: case R.id.nav_notifications:");
////                NotificationsActivity.show(getActivity());
////                break;
////            case R.id.nav_profile /*2131296487*/:
////                StringBuilder stringBuilder = new StringBuilder();
////                stringBuilder.append("updateFragmentByNavId: 呵呵呵呵_____");
////                stringBuilder.append(AppData.INSTANCE.getLoggedUser().getAvatarUrl());
////                stringBuilder.append("/////");
////                stringBuilder.append(AppData.INSTANCE.getLoggedUser().getLogin());
////                Log.i("================", stringBuilder.toString());
////                break;
//            case R.id.nav_settings /*2131296492*/:
//                SettingsActivity.show(getActivity(), 100);
//                break;
//            default:
//                Log.i(this.TAG, "updateFragmentByNavId: default");
//                loadFragment(R.id.nav_search,url);
//                break;
//        }
    }

    private void updateFilter(int itemId) {
        Log.i("=============>", "001 - web updateFilter");
//        if (itemId == R.id.nav_owned) {
//            Log.i(this.TAG, "updateFilter:牛皮__________________");
//            updateEndDrawerContent(R.menu.menu_repositories_filter);
//            RepositoriesFilter.initDrawer(this.navViewEnd, RepositoriesType.OWNED);
//        }
//        else if (itemId == R.id.nav_starred) {
//            updateEndDrawerContent(R.menu.menu_repositories_filter);
//            RepositoriesFilter.initDrawer(this.navViewEnd, RepositoriesType.STARRED);
//        }
//        else {
//            removeEndDrawer();
//        }
        invalidateOptionsMenu();
    }

    private void updateTitle(int itemId) {
        Log.i("=============>", "003 - web updateTitle");
        int titleId = FRAGMENT_TITLE_LIST.get(FRAGMENT_NAV_ID_LIST.indexOf(itemId));

        setToolbarTitle(getString(titleId));
    }

    private void loadFragment(int itemId,String url) {
        Log.i(this.TAG,  "loadFragment: "+itemId+"????"+url);
        this.selectedPage = itemId;
//        String fragmentTag = (String) this.TAG_MAP.get(Integer.valueOf(itemId));
        String fragmentTag = TAG_MAP.get(itemId);
        if(url!=null){
            fragmentTag=url;
        }
        //这里固定一个tag,让每个连接打开时重新加载web页面。
        Fragment showFragment = getSupportFragmentManager().findFragmentByTag("tbswebview");
        boolean isExist = true;
        if (showFragment == null) {
            isExist = false;
            showFragment = getFragment(itemId,url);
        }
        if (showFragment.isVisible()) {
            return;
        }
        Fragment visibleFragment = getVisibleFragment();
        if (isExist) {
            showAndHideFragment(showFragment, visibleFragment);
        } else {
            addAndHideFragment(showFragment, visibleFragment, fragmentTag);
        }

//        if (!showFragment.isVisible()) {
//            Fragment visibleFragment = getVisibleFragment();
//            if (isExist) {
//                showAndHideFragment(showFragment, visibleFragment);
//            } else {
//                addAndHideFragment(showFragment, visibleFragment, fragmentTag);
//            }
//        }
    }

    @NonNull
    private Fragment getFragment(int itemId,String url) {
        if(url!=null){
            return ViewerFragment.toUrl(url,AppData.INSTANCE.getLoggedUser().getLogin());
        }
        return null;
//        if (itemId != R.id.nav_news) {
//            if (itemId == R.id.nav_owned) {
//                return RepositoriesFragment.create(RepositoriesType.OWNED, AppData.INSTANCE.getLoggedUser().getLogin());
//            }
//            if (itemId != R.id.nav_public_news) {
//                return null;
//            }
//        }
//        return ActivityFragment.create(ActivityType.PublicNews, AppData.INSTANCE.getLoggedUser().getLogin(),itemId);
    }

    private void showAndHideFragment(@NonNull Fragment showFragment, @Nullable Fragment hideFragment) {
        Log.i("=============>", "006 - web showAndHideFragment");
        if (hideFragment == null) {
            getSupportFragmentManager().beginTransaction().show(showFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().show(showFragment).hide(hideFragment).commit();
        }
    }

    private void addAndHideFragment(@NonNull Fragment showFragment, @Nullable Fragment hideFragment, @NonNull String addTag) {
        Log.i("=============>", "007 - web addAndHideFragment");
        if (hideFragment == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.frame_layout_content, showFragment, addTag).commit();
        } else {
            getSupportFragmentManager().beginTransaction().add(R.id.frame_layout_content, showFragment, addTag).hide(hideFragment).commit();
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == -1) {
            recreate();
        }
    }

    /* access modifiers changed from: protected */
    public void onToolbarDoubleClick() {
        super.onToolbarDoubleClick();
    }

    private void handlerEndDrawerClick(MenuItem item) {
        Fragment fragment = getVisibleFragment();
        if (fragment != null && (fragment instanceof RepositoriesFragment)) {
            int i = this.selectedPage;
//            if (i == R.id.nav_owned || i == R.id.nav_starred) {
//            if (i == R.id.nav_owned) {
//                ((RepositoriesFragment) fragment).onDrawerSelected(this.navViewEnd, item);
//            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    private void toggleAccountLay() {
        isManageAccount = !isManageAccount;
        this.toggleAccountBn.setImageResource(this.isManageAccount ? R.drawable.ic_arrow_drop_up : R.drawable.ic_arrow_drop_down);
//        invalidateMainMenu();
    }

    private void invalidateMainMenu() {
        Log.i(TAG, "invalidateMainMenu: _________");
        if(navViewStart == null){
            return ;
        }
        Menu menu = navViewStart.getMenu();

        if(!isAccountsAdded){
            isAccountsAdded = true;
//            List<AuthUser> users = mPresenter.getLoggedUserList();
//            for(AuthUser user : users){
//                MenuItem menuItem = menu.add(R.id.manage_accounts, Menu.NONE, 1, user.getLoginId())
//                        .setIcon(R.drawable.ic_menu_person)
//                        .setOnMenuItemClickListener(item -> {
//                            mPresenter.toggleAccount(item.getTitle().toString());
//                            return true;
//                        });
//            }
        }
        menu.setGroupVisible(R.id.my_account, isManageAccount);
        menu.setGroupVisible(R.id.manage_accounts, isManageAccount);

//        menu.setGroupVisible(R.id.my, false);
        menu.setGroupVisible(R.id.repositories, !isManageAccount);
//        menu.setGroupVisible(R.id.search, !isManageAccount);
        menu.setGroupVisible(R.id.setting, !isManageAccount);
    }

    public void restartApp() {
        getActivity().finishAffinity();
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        am.killBackgroundProcesses("com.hong");
        android.os.Process.killProcess(android.os.Process.myPid());
        startActivity(new Intent(getActivity(), SplashActivity.class));
    }

    @Override
    public void renderMenu(ArrayList<UMenu> menus) {
        System.out.println("renderMenu_________-"+menus.size());
        updateStartDrawerContent(R.layout.activity_web_drawer,menus);

    }

    private void logout() {
        new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setTitle(R.string.warning_dialog_tile)
                .setMessage(R.string.logout_warning)
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                .setPositiveButton(R.string.logout, (dialog, which) -> {
                    mPresenter.logout();
                })
                .show();
    }

    public /* synthetic */ void lambda$logout$2$WebActivity(DialogInterface dialog, int which) {
        ((WebPresenter) this.mPresenter).logout();
    }
}
