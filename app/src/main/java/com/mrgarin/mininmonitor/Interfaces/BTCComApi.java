package com.mrgarin.mininmonitor.Interfaces;

import com.mrgarin.mininmonitor.BTCcom.BTCComApiData;
import com.mrgarin.mininmonitor.BTCcom.BTCComApiDataList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BTCComApi {

    @GET("worker/full-stats")
    Call<BTCComApiData> getElement(@Query("access_key") String access_key, @Query("puid") String puid);

    @GET("account/sub-account/list")
    Call<BTCComApiDataList> getSubAccountList(@Query("access_key") String access_key);

    @GET("account/earn-stats")
    Call<BTCComApiData> getEarnStats(@Query("access_key") String access_key, @Query("puid") String puid);
}
