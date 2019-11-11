package com.hong.http;

import android.support.annotation.NonNull;

import com.hong.http.model.UMenu;
import com.hong.http.model.ZyjdPicEntity;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

public interface ViewerService {

    /**
     * uploadImg(File file,String fileName,String userName,String jdid,String prefix) {
     */
    @Multipart
    @POST("ImgUpload/uploadImg2")
    @Headers({"Accept: application/json"})
    Observable<Response<HashMap<String,String>>>  uploadImg(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file,
            @Query("fileName") String fileName,
            @Query("username") String userName,
            @Query("jdid") String jdid,
            @Query("prefix") String prefix

    );
    @Multipart
    @POST("ImgUpload/cbs_upload")
    @Headers({"Accept: application/json"})
    Observable<Response<HashMap<String,String>>>  cbs_upload(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file,
            @Query("fileName") String fileName,
            @Query("username") String userName,
            @Query("jcid") String jdid,
            @Query("jcxm1") String jcxm1,
            @Query("jcxm2") String jcxm2,
            @Query("jcxm3") String jcxm3,
            @Query("tab") String tab,
            @Query("prefix") String prefix,
            @Query("oilfield") String oilfield

    );

    @GET("ImgUpload/selectImgByJdid")
    @NonNull
    Observable<Response<HashMap<String,ArrayList<ZyjdPicEntity>>>>  searcImages(
            @Query("jdid") String jdid
    );




}
