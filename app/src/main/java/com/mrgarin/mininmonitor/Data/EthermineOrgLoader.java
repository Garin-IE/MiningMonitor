package com.mrgarin.mininmonitor.Data;

import com.mrgarin.mininmonitor.Interfaces.EthermineOrgAPI;
import com.mrgarin.mininmonitor.aplication.AppConfig;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class EthermineOrgLoader {

    private Retrofit mRetrofit;
    private EthermineOrgAPI ethermineOrgAPI;

    public EthermineOrgLoader(){
        mRetrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.baseurl_ethermineorg)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        ethermineOrgAPI = mRetrofit.create(EthermineOrgAPI.class);
    }

    public EthermineOrgAPI getEthermineOrgAPI() {
        return ethermineOrgAPI;
    }

    public interface OnEthermineOrgPoolAdd{
        public void onEthermineOrgPoolAdd(EthermineOrgElement element);
    }
}
