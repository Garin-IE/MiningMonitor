package com.mrgarin.mininmonitor.Interfaces;

import com.mrgarin.mininmonitor.Data.EthermineOrgApiData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface EthermineOrgAPI {

    @GET("/miner/{address}/currentStats")
    Call<EthermineOrgApiData> getCurrentStats(@Path("address") String address);
}
