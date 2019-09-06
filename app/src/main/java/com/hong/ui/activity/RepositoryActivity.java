package com.hong.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog.Builder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import com.hong.AppData;
import com.hong.R;
import com.hong.inject.component.AppComponent;
import com.hong.inject.component.DaggerActivityComponent;
import com.hong.inject.module.ActivityModule;
import com.hong.mvp.contract.IRepositoryContract.View;
import com.hong.mvp.model.Branch;
import com.hong.mvp.model.Repository;
import com.hong.mvp.presenter.RepositoryPresenter;
import com.hong.ui.activity.base.PagerActivity;
import com.hong.ui.adapter.base.FragmentPagerModel;
import com.hong.ui.fragment.ActivityFragment;
import com.hong.util.AppOpener;
import com.hong.util.AppUtils;
import com.hong.util.BundleHelper;
import com.hong.util.PrefUtils;
import com.hong.util.StringUtils;

public class RepositoryActivity extends PagerActivity<RepositoryPresenter> implements View {
    private static String TAG;
    @BindView(R.id.desc)
    TextView desc;
    @BindView(R.id.info)
    TextView info;
    @BindView(R.id.loader)
    ProgressBar loader;
    @BindView(R.id.user_avatar_bg)
    ImageView userImageViewBg;

    public interface RepositoryListener {
        void onBranchChanged(Branch branch);

        void onRepositoryInfoUpdated(Repository repository);
    }

    static {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(RepositoryActivity.class.getSimpleName());
        stringBuilder.append("===============");
        TAG = stringBuilder.toString();
    }

    public static void show(@NonNull Context activity, @NonNull String owner, @NonNull String repoName) {
        Log.i(TAG, "show: 3参数");
        activity.startActivity(createIntent(activity, owner, repoName));
    }

    public static void show(@NonNull Context activity, @NonNull Repository repository) {
        Log.i(TAG, "show: __2参数"+repository.getTitle()+"///"+repository.getWellCommonName());
//        Intent intent = new Intent(activity, BuildBanbaoSearch.class);
//        intent.putExtra("repository", repository);
//        activity.startActivity(intent);
    }

    public static Intent createIntent(@NonNull Context activity, @NonNull String owner, @NonNull String repoName) {
        Log.i(TAG, "createIntent: ");
        return new Intent(activity, RepositoryActivity.class).putExtras(BundleHelper.builder().put("owner", owner).put("repoName", repoName).build());
    }

    /* access modifiers changed from: protected */
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder().appComponent(appComponent).activityModule(new ActivityModule(getActivity())).build().inject(this);
    }

    /* access modifiers changed from: protected */
    @Nullable
    public int getContentView() {
        return R.layout.activity_repository;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu: ");
        return true;
    }

    /* access modifiers changed from: protected */
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        Log.i(TAG, "initView: ");
        setTransparentStatusBar();
        toolbar.setTitleTextAppearance(getActivity(), R.style.Toolbar_TitleText);
        toolbar.setSubtitleTextAppearance(getActivity(), R.style.Toolbar_Subtitle);
        setToolbarBackEnable();
        setToolbarTitle(((RepositoryPresenter) this.mPresenter).getRepoName());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected: ");
        int i;
        switch (item.getItemId()) {
            case R.id.action_bookmark /*2131296266*/:
                mPresenter.bookmark(!mPresenter.isBookmarked());
                invalidateOptionsMenu();
                showSuccessToast(mPresenter.isBookmarked() ?
                        getString(R.string.bookmark_saved) : getString(R.string.bookmark_removed));
                return true;
            case R.id.action_branch /*2131296267*/:
                ((RepositoryPresenter) this.mPresenter).loadBranchesAndTags();
                return true;
            case R.id.action_copy_clone_url /*2131296270*/:
                AppUtils.copyToClipboard(getActivity(), ((RepositoryPresenter) this.mPresenter).getRepository().getCloneUrl());
                return true;
            case R.id.action_copy_url /*2131296271*/:
                AppUtils.copyToClipboard(getActivity(), ((RepositoryPresenter) this.mPresenter).getRepository().getHtmlUrl());
                return true;
            case R.id.action_download_source_tar /*2131296273*/:
                AppOpener.startDownload(getActivity(), ((RepositoryPresenter) this.mPresenter).getTarSourceUrl(), ((RepositoryPresenter) this.mPresenter).getTarSourceName());
                return true;
            case R.id.action_download_source_zip /*2131296274*/:
                AppOpener.startDownload(getActivity(), ((RepositoryPresenter) this.mPresenter).getZipSourceUrl(), ((RepositoryPresenter) this.mPresenter).getZipSourceName());
                return true;
            case R.id.action_fork /*2131296279*/:
                if (!((RepositoryPresenter) this.mPresenter).getRepository().isFork()) {
                    forkRepo();
                }
                return true;
            case R.id.action_open_in_browser /*2131296295*/:
                AppOpener.openInCustomTabsOrBrowser(getActivity(), ((RepositoryPresenter) this.mPresenter).getRepository().getHtmlUrl());
                return true;
            case R.id.action_releases /*2131296297*/:
                showReleases();
                return true;
            case R.id.action_share /*2131296299*/:
                AppOpener.shareText(getActivity(), ((RepositoryPresenter) this.mPresenter).getRepository().getHtmlUrl());
                return true;
            case R.id.action_star /*2131296301*/:
                starRepo(!mPresenter.isStarred());
                return true;
            case R.id.action_watch /*2131296303*/:
                mPresenter.watchRepo(!mPresenter.isWatched());
                invalidateOptionsMenu();
                showSuccessToast(mPresenter.isWatched() ?
                        getString(R.string.watched) : getString(R.string.unwatched));
                return true;
            case R.id.action_wiki /*2131296304*/:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void starRepo(boolean star) {
        int i;
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("starRepo: ");
        stringBuilder.append(star);
        Log.i(str, stringBuilder.toString());
        ((RepositoryPresenter) this.mPresenter).starRepo(star);
        invalidateOptionsMenu();
        if (((RepositoryPresenter) this.mPresenter).isStarred()) {
            i = R.string.starred;
        } else {
            i = R.string.unstarred;
        }
        showSuccessToast(getString(i));
    }

    public void showRepo(Repository repo) {
        Log.i(TAG, "showRepo: 测试repository是否注入进来_____"+repo.getDailyid()+"/////"+repo.getTitle()+"///"+repo.getDescription()+"///"+repo.getCreateTime());
//        setToolbarTitle(repo.getFullName(), repo.getDefaultBranch());
        desc.setText(repo.getTitle());
        //增加的提示,用的什么语言,一共多大    暂不需要
//        String language = StringUtils.isBlank(repo.getLanguage()) ?
//                getString(R.string.unknown) : repo.getLanguage();
//        info.setText(String.format(Locale.getDefault(), "Language %s, size %s",
//                language, StringUtils.getSizeString(repo.getSize() * 1024)));

        if (this.pagerAdapter.getCount() == 0) {
            this.pagerAdapter.setPagerList(FragmentPagerModel.createRepoPagerList(getActivity(), repo, getFragments()));
            this.tabLayout.setVisibility(android.view.View.VISIBLE);
            this.tabLayout.setupWithViewPager(this.viewPager);
            this.viewPager.setAdapter(this.pagerAdapter);
            showFirstPager();
//            GlideApp.with(getActivity())
//                    .load(repo.getAvatarurl())
//                    .onlyRetrieveFromCache(!PrefUtils.isLoadImageEnable())
//                    .into(userImageViewBg);
        } else {
            noticeRepositoryUpdated(repo);
        }
        invalidateOptionsMenu();
    }

    public void showStarWishes() {
        Log.i(TAG, "showStarWishes: ");
        String message = getString(R.string.star_wishes);
        String user = AppData.INSTANCE.getLoggedUser().getName();
        user = StringUtils.isBlank(user) ? AppData.INSTANCE.getLoggedUser().getLogin() : user;
        new Builder(getActivity()).setTitle((int) R.string.openhub_wishes).setMessage((CharSequence) "牛皮啊,很6啊!").setNegativeButton((int) R.string.star_next_time, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton((int) R.string.star_me, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                RepositoryActivity.this.starRepo(true);
                RepositoryActivity repositoryActivity = RepositoryActivity.this;
                repositoryActivity.showSuccessToast(repositoryActivity.getString(R.string.star_thanks));
            }
        }).setCancelable(false).show();
    }

    private void forkRepo() {
        Log.i(TAG, "forkRepo: ");
        new Builder(getActivity()).setCancelable(true).setTitle((int) R.string.warning_dialog_tile).setMessage((int) R.string.fork_warning_msg).setNegativeButton((int) R.string.cancel, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setPositiveButton((int) R.string.fork, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ((RepositoryPresenter) RepositoryActivity.this.mPresenter).createFork();
            }
        }).show();
    }

    private void showReleases() {
        Log.i(TAG, "showReleases: ");
    }

    public void showLoading() {
        super.showLoading();
        Log.i(TAG, "showLoading: ");
        this.loader.setVisibility(android.view.View.VISIBLE);
    }

    public void hideLoading() {
        super.hideLoading();
        Log.i(TAG, "hideLoading: ");
        this.loader.setVisibility(android.view.View.GONE);
    }

    private void noticeBranchChanged(Branch branch) {
        Log.i(TAG, "noticeBranchChanged: ");
        for (FragmentPagerModel pagerModel : this.pagerAdapter.getPagerList()) {
            if (pagerModel.getFragment() instanceof RepositoryListener) {
                ((RepositoryListener) pagerModel.getFragment()).onBranchChanged(branch);
            }
        }
    }

    private void noticeRepositoryUpdated(Repository repository) {
        Log.i(TAG, "noticeRepositoryUpdated: ");
        for (FragmentPagerModel pagerModel : this.pagerAdapter.getPagerList()) {
            if (pagerModel.getFragment() instanceof RepositoryListener) {
                ((RepositoryListener) pagerModel.getFragment()).onRepositoryInfoUpdated(repository);
            }
        }
    }

    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        Log.i(TAG, "onAttachFragment: ");
    }

    public int getPagerSize() {
        Log.i(TAG, "getPagerSize: ");
        return 1;
    }

    /* access modifiers changed from: protected */
    public int getFragmentPosition(Fragment fragment) {
        Log.i(TAG, "getFragmentPosition: ");
        if (fragment instanceof ActivityFragment) {
            return 0;
        }
        return -1;
    }
}
