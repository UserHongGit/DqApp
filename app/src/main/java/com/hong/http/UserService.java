package com.hong.http;

import android.support.annotation.NonNull;

import com.hong.http.model.UMenu;
import com.hong.mvp.model.Event;
import com.hong.mvp.model.User;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface UserService {
    @GET("user/following/{user}")
    @NonNull
    Observable<Response<ResponseBody>> checkFollowing(@Path("user") String str);

    @GET("users/{user}/following/{targetUser}")
    @NonNull
    Observable<Response<ResponseBody>> checkFollowing(@Path("user") String str, @Path("targetUser") String str2);

    @PUT("user/following/{user}")
    @NonNull
    Observable<Response<ResponseBody>> followUser(@Path("user") String str);

    @GET("users/{user}/followers")
    @NonNull
    Observable<Response<ArrayList<User>>> getFollowers(@Header("forceNetWork") boolean z, @Path("user") String str, @Query("page") int i);

    @GET("users/{user}/following")
    @NonNull
    Observable<Response<ArrayList<User>>> getFollowing(@Header("forceNetWork") boolean z, @Path("user") String str, @Query("page") int i);

    @GET("TestAction!users")
    @NonNull
    Observable<Response<ArrayList<Event>>> getNewsEvent(@Header("forceNetWork") boolean z, @Query("user") String str, @Query("page") int i);

    @GET("orgs/{org}/members")
    @NonNull
    Observable<Response<ArrayList<User>>> getOrgMembers(@Header("forceNetWork") boolean z, @Path("org") String str, @Query("page") int i);

    @GET("sggl/LoginActionTemp!user")
    @NonNull
    Observable<Response<User>> getPersonInfo(@Header("forceNetWork") boolean z);

    @GET("sggl/LoginActionTemp!getMenu")
    @NonNull
    Observable<Response<HashMap<String,ArrayList<UMenu>>>>  getMenu(@Header("forceNetWork") boolean z, @Query("page") int i);


    @GET("sggl/LoginActionTemp!getMenu2")
    @NonNull
    Observable<Response<HashMap<String,ArrayList<String>>>> getPublicEvent(@Header("forceNetWork") boolean z, @Query("page") int i);

    @GET("users/{user}")
    @NonNull
    Observable<Response<User>> getUser(@Header("forceNetWork") boolean z, @Path("user") String str);

    @GET("users/{user}/events")
    @NonNull
    Observable<Response<ArrayList<Event>>> getUserEvents(@Header("forceNetWork") boolean z, @Path("user") String str, @Query("page") int i);

    @GET("users/{user}/orgs")
    @NonNull
    Observable<Response<ArrayList<User>>> getUserOrgs(@Header("forceNetWork") boolean z, @Path("user") String str);

    @DELETE("user/following/{user}")
    @NonNull
    Observable<Response<ResponseBody>> unfollowUser(@Path("user") String str);
}
