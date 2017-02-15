package com.mixi.rxjavademo.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by mixi on 2017/2/14.
 */

public interface ApiService {
    @GET("LoginUser")
    Call<GetIpInfoResponse> getUserInfo();

    @GET("LoginUser")
    Observable<GetIpInfoResponse> getUserInfo2();
}
