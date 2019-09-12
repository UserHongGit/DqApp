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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;

import com.hong.AppConfig;
import com.hong.http.model.UMenu;
import com.hong.ui.fragment.ViewerFragment;
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

        Beta.checkUpgrade();
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
        String str = "=============>";
        Log.i(str, "11 - web initView"+AppData.INSTANCE.getAccessToken());
        super.initView(savedInstanceState);
        mPresenter.getMenu(AppData.INSTANCE.getAccessToken());
        setToolbarScrollAble(true);

        //左侧滑动菜单通过这个方法渲染,activity_web_drawer存储的菜单的列表名称
        updateStartDrawerContent(R.layout.activity_web_drawer);

        removeEndDrawer();
        if (((WebPresenter) this.mPresenter).isFirstUseAndNoNewsUser()) {
            Log.i(TAG, "initView: 干哈呢?");
            this.selectedPage = R.id.nav_public_news;
            updateFragmentByNavId(this.selectedPage,null);
        } else {
            Log.i(TAG, "initView: 搁这呢!");
            int i = this.selectedPage;
            if (i != 0) {
                updateFragmentByNavId(i,null);
            } else {
                int startPageIndex = Arrays.asList(getResources().getStringArray(R.array.start_pages_id)).indexOf(PrefUtils.getStartPage());
                TypedArray typedArray = getResources().obtainTypedArray(R.array.start_pages_nav_id);
                int startPageNavId = typedArray.getResourceId(startPageIndex, 0);
                typedArray.recycle();
                if (this.FRAGMENT_NAV_ID_LIST.contains(Integer.valueOf(startPageNavId))) {
                    Log.i(str, "11 - web FRAGMENT_NAV_ID_LIST.contains(startPageNavId)");
                    this.selectedPage = startPageNavId;
                    updateFragmentByNavId(this.selectedPage,null);
                } else {
                    Log.i(str, "11 - web !FRAGMENT_NAV_ID_LIST.contains(startPageNavId)");
//                    this.selectedPage = R.id.nav_news;
                    updateFragmentByNavId(this.selectedPage,null);
                    updateFragmentByNavId(startPageNavId,null);
                }
            }
        }
        ImageView avatar = (ImageView) this.navViewStart.getHeaderView(0).findViewById(R.id.avatar);
        TextView name = (TextView) this.navViewStart.getHeaderView(0).findViewById(R.id.name);
        TextView mail = (TextView) this.navViewStart.getHeaderView(0).findViewById(R.id.mail);
        this.toggleAccountBn = (AppCompatImageView) this.navViewStart.getHeaderView(0).findViewById(R.id.toggle_account_bn);
        toggleAccountBn.setOnClickListener(v -> {
            toggleAccountLay();
        });
        User loginUser = AppData.INSTANCE.getLoggedUser();
//        GlideApp.with(getActivity())
//                .load(loginUser.getAvatarUrl())
//                .onlyRetrieveFromCache(!PrefUtils.isLoadImageEnable())
//                .into(avatar);
        name.setText(StringUtils.isBlank(loginUser.getName()) ? loginUser.getLogin() : loginUser.getName());
        Calendar c = Calendar.getInstance();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(new Date());
        stringBuilder.append(f.format(c.getTime()));
        mail.setText(StringUtils.isBlank(loginUser.getBio()) ? "登录时间".concat(stringBuilder.toString()) : loginUser.getBio());
        this.tabLayout.setVisibility(android.view.View.GONE);
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
        Log.i(this.TAG, "updateFragmentByNavId: "+id);
        if (this.FRAGMENT_NAV_ID_LIST.contains(Integer.valueOf(id))) {
            Log.i(this.TAG, "updateFragmentByNavId: 三连杀"+id);
            updateTitle(id);
            loadFragment(id,null);
            updateFilter(id);
            return;
        }
        switch (id) {
            case R.id.nav_add_account /*2131296471*/:
                showLoginPage();
                break;
//            case R.id.nav_issues /*2131296478*/:
//                Log.i(this.TAG, "updateFragmentByNavId: 我尼玛,这个才是问题____第四个___");
//                IssuesActivity.showForUser(getActivity());
//                break;
            case R.id.nav_logout /*2131296480*/:
                logout();
                break;
//            case R.id.nav_notifications /*2131296484*/:
//                Log.i(this.TAG, "updateFragmentByNavId: case R.id.nav_notifications:");
//                NotificationsActivity.show(getActivity());
//                break;
//            case R.id.nav_profile /*2131296487*/:
//                StringBuilder stringBuilder = new StringBuilder();
//                stringBuilder.append("updateFragmentByNavId: 呵呵呵呵_____");
//                stringBuilder.append(AppData.INSTANCE.getLoggedUser().getAvatarUrl());
//                stringBuilder.append("/////");
//                stringBuilder.append(AppData.INSTANCE.getLoggedUser().getLogin());
//                Log.i("================", stringBuilder.toString());
//                break;
            case R.id.nav_settings /*2131296492*/:
                SettingsActivity.show(getActivity(), 100);
                break;

            default:
                Log.i(this.TAG, "updateFragmentByNavId: default");
                loadFragment(R.id.nav_search,url);
                break;
        }
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
        Log.i(this.TAG,  "loadFragment: "+itemId);
        this.selectedPage = itemId;
        String fragmentTag = (String) this.TAG_MAP.get(Integer.valueOf(itemId));
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
        Log.i("=============>", "005 - web getFragment"+itemId);
        //这里写switch区分跳转
        if(itemId!=0){
            switch(itemId){
                case R.id.nav_search:
                    return ViewerFragment.toUrl(AppConfig.UPC_API_BASE_URL+url,AppData.INSTANCE.getLoggedUser().getLogin());
                case 2131230100:
                    return ViewerFragment.toUrl(AppConfig.UPC_API_BASE_URL+"m/sggl/zyfp1.jsp",AppData.INSTANCE.getLoggedUser().getLogin());
                case 2131230101:
                    return ViewerFragment.toUrl(AppConfig.UPC_API_BASE_URL+"m/sggl/zyfp2.jsp",AppData.INSTANCE.getLoggedUser().getLogin());
                case 2131230102:
                    return ViewerFragment.toUrl(AppConfig.UPC_API_BASE_URL+"m/sggl/zyfp2.jsp",AppData.INSTANCE.getLoggedUser().getLogin());
                case 2131230103:
                    return ViewerFragment.toUrl(AppConfig.UPC_API_BASE_URL+"m/sggl/zyfp2.jsp",AppData.INSTANCE.getLoggedUser().getLogin());
                case 2131230104:
                    return ViewerFragment.toUrl(AppConfig.UPC_API_BASE_URL+"m/sggl/zyfp2.jsp",AppData.INSTANCE.getLoggedUser().getLogin());
               default:
                    return ViewerFragment.toUrl(AppConfig.UPC_API_BASE_URL+url,AppData.INSTANCE.getLoggedUser().getLogin());


            }
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
        invalidateMainMenu();
    }

    private void invalidateMainMenu() {
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
