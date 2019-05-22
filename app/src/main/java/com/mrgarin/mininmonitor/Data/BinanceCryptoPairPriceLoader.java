package com.mrgarin.mininmonitor.Data;

import android.os.AsyncTask;
import android.util.Log;

import com.mrgarin.mininmonitor.aplication.AppConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public class BinanceCryptoPairPriceLoader extends AsyncTask<Void, Void, Map<String, Double>> {

    IDataResult callback;

    public interface  IDataResult{
        void result(Map<String, Double> map);
    }

    public BinanceCryptoPairPriceLoader(IDataResult callback){
        this.callback = callback;
    }

    @Override
    protected Map<String, Double> doInBackground(Void... voids) {
        Call<ArrayList<CryptoPrice>> priceListCall = new BinanceLoader().getBinanceAPI().getPriceList();
        ArrayList<CryptoPrice> priceList = new ArrayList<>();
        try {
            priceList = priceListCall.execute().body();
        } catch (Exception e) {
            e.printStackTrace();
            if (AppConfig.debug) {
                Log.d("myLogs", "Error in binance price loader: " + e);
                return null;
            }
        }
        Map<String, Double> priceListMap = new HashMap<>();
        int count = priceList.size();
        for (int i = 0; i < count; i++){
            CryptoPrice price = priceList.get(i);
            priceListMap.put(price.getSymbol(),price.getPrice());
        }
        return priceListMap;
    }

    @Override
    protected void onPostExecute(Map<String, Double> stringDoubleMap) {
        super.onPostExecute(stringDoubleMap);
        this.callback.result(stringDoubleMap);
    }
}
