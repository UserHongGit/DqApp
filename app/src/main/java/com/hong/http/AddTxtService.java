package com.hong.http;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Response;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface AddTxtService {

    /**
     *  根据did和工序查询第二个下拉框
     */
    @POST("main/getCs1")
    @Headers({"Accept: application/json"})
    Observable<Response<ArrayList<HashMap<String,String>>>>  getCs1(
            @Query("did") String did,
            @Query("gxType") String gxType,
            @Query("oilfield") String oilfield
    );
}
