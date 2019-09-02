package com.hong.http;

import android.support.annotation.NonNull;

import com.hong.mvp.model.Branch;
import com.hong.mvp.model.Event;
import com.hong.mvp.model.FileModel;
import com.hong.mvp.model.Repository;
import com.hong.mvp.model.User;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

public interface RepoService {
    @GET("user/starred/{owner}/{repo}")
    @NonNull
    Observable<Response<ResponseBody>> checkRepoStarred(@Path("owner") String str, @Path("repo") String str2);

    @GET("user/subscriptions/{owner}/{repo}")
    @NonNull
    Observable<Response<ResponseBody>> checkRepoWatched(@Path("owner") String str, @Path("repo") String str2);

    @POST("repos/{owner}/{repo}/forks")
    @NonNull
    Observable<Response<Repository>> createFork(@Path("owner") String str, @Path("repo") String str2);

    @GET("repos/{owner}/{repo}/branches")
    @NonNull
    Observable<Response<ArrayList<Branch>>> getBranches(@Path("owner") String str, @Path("repo") String str2);

    @NonNull
    @GET
    @Headers({"Accept: application/vnd.github.html"})
    Observable<Response<ResponseBody>> getFileAsHtmlStream(@Header("forceNetWork") boolean z, @Url String str);

    @NonNull
    @GET
    @Headers({"Accept: application/vnd.github.VERSION.raw"})
    Observable<Response<ResponseBody>> getFileAsStream(@Header("forceNetWork") boolean z, @Url String str);

    @GET("repos/{owner}/{repo}/forks")
    @NonNull
    Observable<Response<ArrayList<Repository>>> getForks(@Header("forceNetWork") boolean z, @Path("owner") String str, @Path("repo") String str2, @Query("page") int i);

    @GET("daily/searchByDailyid/{dailyid}")
    @NonNull
    Observable<Response<ArrayList<Event>>> getRepoEvent(@Header("forceNetWork") boolean z, @Path("dailyid") String str);

    @GET("repos/{owner}/{repo}/contents/{path}")
    @NonNull
    Observable<Response<ArrayList<FileModel>>> getRepoFiles(@Path("owner") String str, @Path("repo") String str2, @Path(encoded = true, value = "path") String str3, @Query("ref") String str4);

    @GET("daily/{dailyid}")
    @NonNull
    Observable<Response<Repository>> getRepoInfo(@Header("forceNetWork") boolean z, @Path("dailyid") String str);

    @GET("repos/{owner}/{repo}/stargazers")
    @NonNull
    Observable<Response<ArrayList<User>>> getStargazers(@Header("forceNetWork") boolean z, @Path("owner") String str, @Path("repo") String str2, @Query("page") int i);

    @GET("users/{user}/starred")
    @NonNull
    Observable<Response<ArrayList<Repository>>> getStarredRepos(@Header("forceNetWork") boolean z, @NonNull @Path("user") String str, @Query("page") int i, @Query("sort") String str2, @Query("direction") String str3);

    @GET("repos/{owner}/{repo}/tags")
    @NonNull
    Observable<Response<ArrayList<Branch>>> getTags(@Path("owner") String str, @Path("repo") String str2);

    @GET("users/{user}/repos")
    @NonNull
    Observable<Response<ArrayList<Repository>>> getUserPublicRepos(@Header("forceNetWork") boolean z, @NonNull @Path("user") String str, @Query("page") int i, @Query("type") String str2, @Query("sort") String str3, @Query("direction") String str4);


    @GET("main/searchDataByAccount")
    @NonNull
    Observable<Response<ArrayList<Repository>>> getUserRepos(
            @Header("forceNetWork") boolean z,
            @Query("page") int i,
            @Query("oilfield") String oilfield);



    @GET("repos/{owner}/{repo}/subscribers")
    @NonNull
    Observable<Response<ArrayList<User>>> getWatchers(@Header("forceNetWork") boolean z, @Path("owner") String str, @Path("repo") String str2, @Query("page") int i);

    @PUT("user/starred/{owner}/{repo}")
    @NonNull
    Observable<Response<ResponseBody>> starRepo(@Path("owner") String str, @Path("repo") String str2);

    @DELETE("user/starred/{owner}/{repo}")
    @NonNull
    Observable<Response<ResponseBody>> unstarRepo(@Path("owner") String str, @Path("repo") String str2);

    @DELETE("user/subscriptions/{owner}/{repo}")
    @NonNull
    Observable<Response<ResponseBody>> unwatchRepo(@Path("owner") String str, @Path("repo") String str2);

    @PUT("user/subscriptions/{owner}/{repo}")
    @NonNull
    Observable<Response<ResponseBody>> watchRepo(@Path("owner") String str, @Path("repo") String str2);
}
