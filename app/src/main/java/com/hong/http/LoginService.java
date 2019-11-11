

package com.hong.http;

import android.support.annotation.NonNull;


import com.hong.http.model.AuthRequestModel;
import com.hong.http.model.UMenu;
import com.hong.mvp.model.BasicToken;
import com.hong.mvp.model.OauthToken;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created on 2017/8/1.
 *
 * @author upc_jxzy
 */

public interface LoginService {

    /*原始大庆登录*/
    /*
    @POST("sggl/LoginActionTemp!loginNew")
//    @POST("authorizations")
    @Headers("Accept: application/json")
    Observable<Response<BasicToken>> authorizations(
            @NonNull @Body AuthRequestModel authRequestModel
    );
    */

    /*通用登录*/
    @POST("common/login")
//    @POST("authorizations")
    @Headers("Accept: application/json")
    Observable<Response<BasicToken>> authorizations(
            @NonNull @Body AuthRequestModel authRequestModel
    );


    @POST("sggl/LoginActionTemp!loginNew")
//    @POST("authorizations")
    @Headers("Accept: application/json")
    Observable<Response<HashMap<String, String>>> authorizations2(
            @Query("token") String token
    );


    @POST("login/oauth/access_token")
    @Headers("Accept: application/json")
    Observable<Response<OauthToken>> getAccessToken(
            @Query("client_id") String clientId,
            @Query("client_secret") String clientSecret,
            @Query("code") String code,
            @Query("state") String state
    );

    @POST("sggl/LoginActionTemp!getMenu")
    @NonNull
    Observable<Response<HashMap<String,ArrayList<UMenu>>>>  getMenu(
            @Query("username") String username
    );

}
