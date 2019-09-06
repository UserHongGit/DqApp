

package com.hong.mvp.contract;

import android.support.annotation.NonNull;

import com.hong.mvp.contract.base.IBaseContract;

import java.io.File;


/**
 * Created by upc_jxzy on 2017/8/19 15:57:16
 */

public interface IViewerContract {

    interface View extends IBaseContract.View{
        void loadLuUrl(@NonNull String url);
//        void loadImageUrl(@NonNull String url);
//        void loadMdText(@NonNull String text, @Nullable String baseUrl);
//        void loadCode(@NonNull String text, @Nullable String extension);
//        void loadDiffFile(@NonNull String text);
//        void loadHtmlSource(@NonNull String htmlSource);
    }

    interface Presenter extends IBaseContract.Presenter<IViewerContract.View>{
        void load(boolean isReload);
        void uploadImg(File file, String fileName, String userName, String jdid, String prefix);
    }

}
