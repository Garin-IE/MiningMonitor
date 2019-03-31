package com.mrgarin.mininmonitor.Data;

import com.mrgarin.mininmonitor.Interfaces.BTCComApi;
import com.mrgarin.mininmonitor.aplication.AppConfig;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;


public class BTCcomLoader {

    private Retrofit mRetrofit;
    private BTCComApi btcComApi;

    public BTCcomLoader() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.baseurl_btcCom)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        btcComApi = mRetrofit.create(BTCComApi.class);
    }

    public BTCComApi getBtcComApi(){
        return btcComApi;
    }

    public interface OnBTCComPoolAdd{
        public void onBTCComPoolAdd(BTCcomElement element);
    }

    public interface OnBTCcomPoolUpdate{
        public void onBTCcomPoolUpdate(BTCcomElement element, int i);
    }
}
