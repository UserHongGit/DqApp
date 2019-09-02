package com.hong.http;

import android.support.annotation.NonNull;

import com.hong.mvp.model.GxrbModel;
import com.hong.mvp.model.ReportWork;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface GxlrService {
    @POST("main/getBaseData")
    @Headers({"Accept: application/json"})
    Observable<Response<ReportWork>>  getBaseData(
            @Query("did") String did,
            @Query("banbaoType") String banbaoType,
            @Query("reportTime3") String reportTime3,
            @Query("orderClasses") String orderClasses
    );

    @POST("main/bz_select")
    @Headers({"Accept: application/json"})
    Observable<Response<ArrayList<GxrbModel>>> getBzSel(@Query("construct_team") String str);

    @POST("main/xzcs_select")
    @Headers({"Accept: application/json"})
    Observable<Response<ArrayList<GxrbModel>>> getCs1Service();

    @POST("main/gxmc_select")
    @Headers({"Accept: application/json"})
    Observable<Response<ArrayList<GxrbModel>>> getCs2Service(@Query("two_class_measure") String str);

    @POST("main/getDataByDid")
    @Headers({"Accept: application/json"})
    Observable<Response<List<HashMap<String, String>>>> getDataByDid(@Query("did") String str, @Query("banbaoType") String str2, @Query("oilfield") String str3);

    @POST("main/ganbu_select")
    @Headers({"Accept: application/json"})
    Observable<Response<ArrayList<GxrbModel>>> getDbgbSel(@Query("construct_team") String str);

    @POST("main/gxnr_select")
    @Headers({"Accept: application/json"})
    Observable<Response<GxrbModel>> getGxNrService(@Query("two_class_measure") String str);

    @POST("main/jlr_select")
    @Headers({"Accept: application/json"})
    Observable<Response<ArrayList<GxrbModel>>> getJlrSel(@Query("construct_team") String str);

    @POST("main/getWellService")
    @Headers({"Accept: application/json"})
    Observable<Response<ArrayList<GxrbModel>>> getWellService(@Query("oilfield") String str);

    @POST("main/isUpload")
    @Headers({"Accept: application/json"})
    Observable<Response<String>> getWellService(@Query("did") String str, @Query("orderClasses") String str2, @Query("reportTime3") String str3);

    @POST("main/rbSave")
    @Headers({"Accept: application/json"})
    Observable<Response<GxrbModel>> rbSave(@Query("cs1") String str, @Query("cs2") String str2, @Query("sj1") String str3, @Query("sj2") String str4, @Query("nr") String str5);


    @POST("main/sgbbSave")
    @Headers({"Accept: application/json"})
    Observable<Response<HashMap<String,String>>>  sgbbSave(
            @NonNull @Body ReportWork re
    );
}
