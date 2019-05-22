package com.mrgarin.mininmonitor.Data;

import com.mrgarin.mininmonitor.Interfaces.BinanceAPI;
import com.mrgarin.mininmonitor.aplication.AppConfig;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class BinanceLoader {

    private Retrofit mRetrofit;
    private BinanceAPI binanceAPI;

    public BinanceLoader(){
        mRetrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.baseurl_binance)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        binanceAPI = mRetrofit.create(BinanceAPI.class);
    }

    public BinanceAPI getBinanceAPI(){
        return binanceAPI;
    }

    public interface getPrice{
        public void getPrice(String symbol);
    }
}
