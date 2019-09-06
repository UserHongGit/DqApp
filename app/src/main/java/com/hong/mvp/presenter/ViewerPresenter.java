

package com.hong.mvp.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.hong.AppData;
import com.hong.dao.DaoSession;
import com.hong.http.core.HttpObserver;
import com.hong.http.core.HttpResponse;
import com.hong.mvp.contract.IViewerContract;
import com.hong.mvp.model.FileModel;
import com.hong.mvp.model.sgcs.GxEntity;
import com.hong.mvp.model.sgcs.SgcsReturn;
import com.hong.mvp.presenter.base.BasePresenter;
import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import rx.Observable;

/**
 * Created by upc_jxzy on 2017/8/19 15:58:43
 */

public class ViewerPresenter extends BasePresenter<IViewerContract.View>
        implements IViewerContract.Presenter{

//    @AutoAccess
//    ViewerActivity.ViewerType viewerType;
//
    @AutoAccess
FileModel fileModel;

//    private String downloadSource;

    @AutoAccess String title;
    @AutoAccess String source;
    @AutoAccess String imageUrl;
   //lu
    @AutoAccess String url;

//    @AutoAccess
//    CommitFile commitFile;

    @Inject
    public ViewerPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        mView.loadLuUrl(url);
//        Log.i("=============->", "show: ====================-loadMdText"+loadMdText);
//        if(ViewerActivity.ViewerType.RepoFile.equals(loadMdText)){
//            load(false);
//        } else if(ViewerActivity.ViewerType.DiffFile.equals(viewerType)) {
//            mView.loadDiffFile(commitFile.getPatch());
//        } else if(ViewerActivity.ViewerType.Image.equals(viewerType)) {
//            mView.loadImageUrl(imageUrl);
//        } else if(ViewerActivity.ViewerType.HtmlSource.equals(viewerType)) {
//            mView.loadMdText(source, null);
//        } else {
//            mView.loadMdText(source, null);
//        }
    }


    @Override
    public void uploadImg(File file,String fileName,String userName,String jdid,String prefix) {
        mView.showLoading();
        Log.i("--------", "uploadImg: shangcuhan____________--");
        HttpObserver<HashMap<String,String>> httpObserver =
                new HttpObserver<HashMap<String,String>>() {
                    @Override
                    public void onError(@NonNull Throwable error) {
                        Toast.makeText(getContext(),"图片上传失败!",Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                        mView.hideLoading();
                    }

                    @Override
                    public void onSuccess(@NonNull HttpResponse<HashMap<String,String>> response) {
                        Toast.makeText(getContext(),response.body().get("msg"),Toast.LENGTH_SHORT).show();
                        mView.hideLoading();
                    }
                };
        generalRxHttpExecute(new IObservableCreator<HashMap<String,String>>() {
            @Nullable
            @Override
            public Observable<Response<HashMap<String,String>>> createObservable(boolean forceNetWork) {
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("application/otcet-stream"), file);
                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("file2", file.getName(), requestFile);
                String descriptionString = fileName;
                RequestBody description =
                        RequestBody.create(
                                MediaType.parse("multipart/form-data"), descriptionString);

                return getViewerService("").uploadImg(description,body,fileName,userName,jdid,prefix);
            }
        }, httpObserver);

    }


    @Override
    public void load(boolean isReload) {
        Log.i("=============->", "show:ViewerPresenter ====================-load");
//
//        final String url = fileModel.getUrl();
//        final String htmlUrl = fileModel.getHtmlUrl();
//        if(StringUtils.isBlank(url) || StringUtils.isBlank(htmlUrl)){
//            mView.showWarningToast(getString(R.string.url_invalid));
//            mView.hideLoading();
//            return;
//        }
//
//        if(GitHubHelper.isArchive(url)){
//            mView.showWarningToast(getString(R.string.view_archive_file_error));
//            mView.hideLoading();
//            return;
//        }
//
//        if(GitHubHelper.isImage(url)){
//            mView.loadImageUrl(fileModel.getDownloadUrl());
//            mView.hideLoading();
//            return;
//        }
//
//        HttpObserver<ResponseBody> httpObserver =
//                new HttpObserver<ResponseBody>() {
//                    @Override
//                    public void onError(Throwable error) {
//                        mView.hideLoading();
//                        mView.showErrorToast(getErrorTip(error));
//                    }
//
//                    @Override
//                    public void onSuccess(HttpResponse<ResponseBody> response) {
//                        mView.hideLoading();
//                        try {
//                            downloadSource = response.body().string();
//                            if(GitHubHelper.isMarkdown(url)){
//                                mView.loadMdText(downloadSource, htmlUrl);
//                            }else{
//                                mView.loadCode(downloadSource, getExtension());
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                };
//        generalRxHttpExecute(new IObservableCreator<ResponseBody>() {
//            @Override
//            public Observable<Response<ResponseBody>> createObservable(boolean forceNetWork) {
//                return GitHubHelper.isMarkdown(url) ?
//                        getRepoService().getFileAsHtmlStream(forceNetWork, url) :
//                        getRepoService().getFileAsStream(forceNetWork, url);
//            }
//        }, httpObserver, false);
//        mView.showLoading();
    }

//    public boolean isCode(){
//        String url = fileModel != null ? fileModel.getUrl() : commitFile.getBlobUrl();
//        return !GitHubHelper.isArchive(url) &&
//                !GitHubHelper.isImage(url) &&
//                !GitHubHelper.isMarkdown(url);
//    }

//    public String getDownloadSource() {
//        return downloadSource;
//    }

//    public String getExtension(){
//        return GitHubHelper.getExtension(fileModel.getUrl());
//    }
//
//    public FileModel getFileModel() {
//        return fileModel;
//    }
//
//    public CommitFile getCommitFile() {
//        return commitFile;
//    }
//
//    public ViewerActivity.ViewerType getViewerType() {
//        return viewerType;
//    }
}
