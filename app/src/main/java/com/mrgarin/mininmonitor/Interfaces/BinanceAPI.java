package com.mrgarin.mininmonitor.Interfaces;

import com.mrgarin.mininmonitor.Data.BinanceApiData;
import com.mrgarin.mininmonitor.Data.CryptoPrice;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BinanceAPI {

    @GET("/api/v3/ticker/price")
    Call<ArrayList<CryptoPrice>> getPriceList();

    @GET("/api/v3/ticker/price")
    Call<BinanceApiData> getPrice(@Query("symbol") String symbol);

    @GET("/api/v3/ticker/price")
    Call<Map<String, Double>> getPriceHashMap();
}
