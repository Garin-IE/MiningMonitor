package com.mrgarin.mininmonitor.Interfaces;

import com.mrgarin.mininmonitor.Data.EthermineOrgApiData;
import com.mrgarin.mininmonitor.Data.EthermineOrgApiDataList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface EthermineOrgAPI {

    @GET("/miner/{address}/currentStats")
    Call<EthermineOrgApiData> getCurrentStats(@Path("address") String address);

    @GET("/miner/{address}/workers")
    Call<EthermineOrgApiDataList> getWorkers(@Path("address") String address);
}
