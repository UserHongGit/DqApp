package com.hong.http;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
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
    @POST("sggl/ImgUpload!uploadImg2")
    @Headers({"Accept: application/json"})
    Observable<Response<HashMap<String,String>>>  uploadImg(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file,
            @Query("fileName") String fileName,
            @Query("username") String userName,
            @Query("jdid") String jdid,
            @Query("prefix") String prefix

    );
}
