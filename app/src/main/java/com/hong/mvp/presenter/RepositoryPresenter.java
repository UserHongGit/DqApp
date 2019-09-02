

package com.hong.mvp.presenter;

import android.util.Log;

import com.hong.AppData;
import com.hong.R;
import com.hong.dao.DaoSession;
import com.hong.http.core.HttpObserver;
import com.hong.http.core.HttpProgressSubscriber;
import com.hong.http.core.HttpResponse;
import com.hong.mvp.contract.IRepositoryContract;
import com.hong.mvp.model.Branch;
import com.hong.mvp.model.Repository;
import com.hong.mvp.presenter.base.BasePresenter;
import com.hong.ui.activity.RepositoryActivity;
import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;

import java.util.ArrayList;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by ThirtyDegreesRay on 2017/8/9 21:42:47
 */

public class RepositoryPresenter extends BasePresenter<IRepositoryContract.View>
        implements IRepositoryContract.Presenter {

    private static String TAG = RepositoriesPresenter.class.getSimpleName()+"======================";

    @AutoAccess(dataName = "repository")
    Repository repository;




    @AutoAccess String owner;
    @AutoAccess String repoName;

    private ArrayList<Branch> branches;
    @AutoAccess
    Branch curBranch;
    private boolean starred;
    private boolean watched;

    private boolean isStatusChecked = false;
    @AutoAccess boolean isTraceSaved = false;

    private boolean isBookmarkQueried = false;
    private boolean bookmarked = false;

    @Inject
    public RepositoryPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        if (repository != null) {
            owner ="主人";
            repoName = repository.getName();
            initCurBranch();
            mView.showRepo(repository);
            //去查询活动ViewPager页面数据
            getRepoInfo(false);
            //checkStatus    github查询收藏什么的状态用的
//            checkStatus();
        } else {
            getRepoInfo(true);
        }
    }

    @Override
    public void loadBranchesAndTags() {
        Log.i(TAG, "loadBranchesAndTags: ");
        if (branches != null) {
//            mView.showBranchesAndTags(branches, curBranch);
            return;
        }
        HttpProgressSubscriber<ArrayList<Branch>> httpProgressSubscriber =
                new HttpProgressSubscriber<>(
                        mView.getProgressDialog(getLoadTip()),
                        new HttpObserver<ArrayList<Branch>>() {
                            @Override
                            public void onError(Throwable error) {
                                mView.showErrorToast(getErrorTip(error));
                            }

                            @Override
                            public void onSuccess(HttpResponse<ArrayList<Branch>> response) {
                                setTags(response.body());
                                branches.addAll(response.body());
//                                mView.showBranchesAndTags(branches, curBranch);
                            }
                        }
                );
        Observable<Response<ArrayList<Branch>>> observable = getRepoService().getBranches(owner, repoName)
                .flatMap(new Func1<Response<ArrayList<Branch>>, Observable<Response<ArrayList<Branch>>>>() {
                    @Override
                    public Observable<Response<ArrayList<Branch>>> call(
                            Response<ArrayList<Branch>> arrayListResponse) {
                        branches = arrayListResponse.body();
                        return getRepoService().getTags(owner, repoName);
                    }
                });
        generalRxHttpExecute(observable, httpProgressSubscriber);
    }

    @Override
    public void starRepo(boolean star) {
        Log.i(TAG, "starRepo: ");
        starred = star;
        Observable<Response<ResponseBody>> observable = starred ?
                getRepoService().starRepo(owner, repoName) :
                getRepoService().unstarRepo(owner, repoName);
        executeSimpleRequest(observable);
    }

    @Override
    public void watchRepo(boolean watch) {
        Log.i(TAG, "watchRepo: ");
        watched = watch;
        Observable<Response<ResponseBody>> observable = watched ?
                getRepoService().watchRepo(owner, repoName) :
                getRepoService().unwatchRepo(owner, repoName);
        executeSimpleRequest(observable);
    }

    @Override
    public void createFork() {
        Log.i(TAG, "createFork: ");
        mView.getProgressDialog(getLoadTip()).show();
        HttpObserver<Repository> httpObserver = new HttpObserver<Repository>() {
            @Override
            public void onError(Throwable error) {
                mView.showErrorToast(getErrorTip(error));
                mView.getProgressDialog(getLoadTip()).dismiss();
            }

            @Override
            public void onSuccess(HttpResponse<Repository> response) {
                if(response.body() != null) {
                    mView.showSuccessToast(getString(R.string.forked));
                    RepositoryActivity.show(getContext(), response.body());
                } else {
                    mView.showErrorToast(getString(R.string.fork_failed));
                }
                mView.getProgressDialog(getLoadTip()).dismiss();
            }
        };
        generalRxHttpExecute(new IObservableCreator<Repository>() {
            @Override
            public Observable<Response<Repository>> createObservable(boolean forceNetWork) {
                return getRepoService().createFork(owner, repoName);
            }
        }, httpObserver);
    }

    @Override
    public boolean isForkEnable() {
        Log.i(TAG, "isForkEnable: ");
        if(repository != null && !repository.isFork() &&
                !repository.getOwner().getLogin().equals(AppData.INSTANCE.getLoggedUser().getLogin())){
            return true;
        }
        return false;
    }

    private void setTags(ArrayList<Branch> list) {
        Log.i(TAG, "setTags: ");
        for (Branch branch : list) {
            branch.setBranch(false);
        }
    }

    private void getRepoInfo(final boolean isShowLoading) {
//        Log.i(TAG, "getRepoInfo: ");
//        if (isShowLoading) mView.showLoading();
//        HttpObserver<Repository> httpObserver =
//                new HttpObserver<Repository>() {
//                    @Override
//                    public void onError(Throwable error) {
//                        Log.i(TAG, "onError:错他吗的了");
//                        if (isShowLoading) mView.hideLoading();
//                        mView.showErrorToast(getErrorTip(error));
//                    }
//
//                    @Override
//                    public void onSuccess(HttpResponse<Repository> response) {
//                        Log.i(TAG, "onSuccess: 你说说这是人干的事吗"+response.body());
//                        if (isShowLoading) mView.hideLoading();
//                        repository = response.body();
//                        initCurBranch();
//                        mView.showRepo(repository);
//                        checkStatus();
//                        saveTrace();
//                    }
//                };
//
//        Log.i(TAG, "getRepoInfo: 测试dailyId是否能作为参数传递给url,,,,dailyID = "+repository.getDailyid());
//        generalRxHttpExecute(new IObservableCreator<Repository>() {
//            @Override
//            public Observable<Response<Repository>> createObservable(boolean forceNetWork) {
//                return getRepoService().getRepoInfo(forceNetWork, repository.getDailyid());
//            }
//        }, httpObserver, true);
    }

    private void checkStatus(){
        Log.i(TAG, "checkStatus: ");
        if(isStatusChecked) return;
        isStatusChecked = true;
        checkStarred();
        checkWatched();
    }


    private void checkStarred() {
        Log.i(TAG, "checkStarred: ");
        checkStatus(
                getRepoService().checkRepoStarred(owner, repoName),
                new CheckStatusCallback() {
                    @Override
                    public void onChecked(boolean status) {
                        starred = status;
                        mView.invalidateOptionsMenu();
                        starWishes();
                    }
                }
        );
    }

    private void checkWatched() {
        Log.i(TAG, "checkWatched: ");
        checkStatus(
                getRepoService().checkRepoWatched(owner, repoName),
                new CheckStatusCallback() {
                    @Override
                    public void onChecked(boolean status) {
                        watched = status;
                        mView.invalidateOptionsMenu();
                    }
                }
        );
    }

    private void initCurBranch(){
        Log.i(TAG, "initCurBranch: ");
        curBranch = new Branch(repository.getDefaultBranch());
        curBranch.setZipballUrl("https://github.com/".concat(owner).concat("/"));
        curBranch.setTarballUrl("https://github.com/".concat(owner).concat("/"));
    }

    public Repository getRepository() {
        Log.i(TAG, "getRepository: ");
        return repository;
    }

    public boolean isFork() {
        Log.i(TAG, "isFork: ");
        return repository != null && repository.isFork();
    }

    public boolean isStarred() {
        Log.i(TAG, "isStarred: ");
        return starred;
    }

    public boolean isWatched() {
        Log.i(TAG, "isWatched: ");
        return watched;
    }

    public String getZipSourceUrl(){
        Log.i(TAG, "getZipSourceUrl: ");
        return curBranch.getZipballUrl();
    }

    public String getZipSourceName(){
        Log.i(TAG, "getZipSourceName: ");
        return repoName.concat("-").concat(curBranch.getName()).concat(".zip");
    }

    public String getTarSourceUrl(){
        Log.i(TAG, "getTarSourceUrl: ");
        return curBranch.getTarballUrl();
    }

    public String getTarSourceName(){
        Log.i(TAG, "getTarSourceName: ");
        return repoName.concat("-").concat(curBranch.getName()).concat(".tar.gz");
    }

    public void setCurBranch(Branch curBranch) {
        Log.i(TAG, "setCurBranch: ");
        this.curBranch = curBranch;
    }

    public String getRepoName() {
        Log.i(TAG, "getRepoName: ");
        return repository == null ? repoName : repository.getName();
    }

    private void starWishes(){
        Log.i(TAG, "starWishes: ");
        if(!starred && getString(R.string.author_login_id).equals(owner)
                && getString(R.string.app_name).equals(repoName)
                ){
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(!starred && mView != null){
                        mView.showStarWishes();
                    }
                }
            }, 3000);
        }
    }

    @Override
    public boolean isBookmarked() {
        Log.i(TAG, "isBookmarked: ");
//        if(!isBookmarkQueried && repository != null){
//            bookmarked = daoSession.getBookmarkDao().queryBuilder()
//                    .where(BookmarkDao.Properties.RepoId.eq(repository.getId()))
//                    .unique() != null;
//            isBookmarkQueried = true;
//        }
        return bookmarked;
    }

    @Override
    public void bookmark(boolean bookmark) {
        Log.i(TAG, "bookmark: ");
//        if(repository == null) return;
//        bookmarked = bookmark;
//        Bookmark bookmarkModel = daoSession.getBookmarkDao().queryBuilder()
//                .where(BookmarkDao.Properties.RepoId.eq(repository.getId()))
//                .unique();
//        if(bookmark && bookmarkModel == null){
//            bookmarkModel = new Bookmark(UUID.randomUUID().toString());
//            bookmarkModel.setType("repo");
//            bookmarkModel.setRepoId((long) repository.getId());
//            bookmarkModel.setMarkTime(new Date());
//            daoSession.getBookmarkDao().insert(bookmarkModel);
//        } else if(!bookmark && bookmarkModel != null){
//            daoSession.getBookmarkDao().delete(bookmarkModel);
//        }
    }

    private void saveTrace(){
        Log.i(TAG, "saveTrace: ");
//        daoSession.runInTx(() ->{
//            if(!isTraceSaved){
//                Trace trace = daoSession.getTraceDao().queryBuilder()
//                        .where(TraceDao.Properties.RepoId.eq(repository.getId()))
//                        .unique();
//
//                if(trace == null){
//                    trace = new Trace(UUID.randomUUID().toString());
//                    trace.setType("repo");
//                    trace.setRepoId((long) repository.getId());
//                    Date curDate = new Date();
//                    trace.setStartTime(curDate);
//                    trace.setLatestTime(curDate);
//                    trace.setTraceNum(1);
//                    daoSession.getTraceDao().insert(trace);
//                } else {
//                    trace.setTraceNum(trace.getTraceNum() + 1);
//                    trace.setLatestTime(new Date());
//                    daoSession.getTraceDao().update(trace);
//                }
//            }
//
//            LocalRepo localRepo = daoSession.getLocalRepoDao().load((long) repository.getId());
//            LocalRepo updateRepo = repository.toLocalRepo();
//            if(localRepo == null){
//                daoSession.getLocalRepoDao().insert(updateRepo);
//            } else {
//                daoSession.getLocalRepoDao().update(updateRepo);
//            }
//        });
//        isTraceSaved = true;
    }

}
