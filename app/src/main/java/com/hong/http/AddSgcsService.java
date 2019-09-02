package com.hong.http;

import android.support.annotation.NonNull;

import com.hong.mvp.model.sgcs.RbEntity;
import com.hong.mvp.model.sgcs.SgcsReturn;

import java.util.HashMap;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface AddSgcsService {

    /**
     *  根据did和工序查询第二个下拉框
     */
    @POST("main/getSgcsDesc")
    @Headers({"Accept: application/json"})
    Observable<Response<SgcsReturn>>   getSgcsDesc(
            @Query("did") String did,
            @Query("orderClasses") String orderClasses,
            @Query("reportTime3") String reportTime,
            @Query("spid") String spid,
            @Query("pdid") String pdid,
            @Query("oilfield") String oilfield,
            @Query("userid") String userid
    );

    @POST("main/savePPData")
    @Headers({"Accept: application/json"})
    Observable<Response<HashMap<String,String>>>   savePPData(
            @NonNull @Body RbEntity rbEntity
    );
//    @PUT("main/savePPData")
//    @Headers({"Accept: application/json"})
//    Observable<Response<String>>   savePPData(
//            @NonNull @Body RbEntity rbEntity
//    );

}
