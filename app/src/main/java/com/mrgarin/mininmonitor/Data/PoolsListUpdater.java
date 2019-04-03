package com.mrgarin.mininmonitor.Data;

import android.os.AsyncTask;
import android.util.Log;

import com.mrgarin.mininmonitor.BTCcom.BTCComApiData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class PoolsListUpdater extends AsyncTask<List<BasicPoolElement>, Void, List<BasicPoolElement>>  {

    DataResult callback;

    public interface DataResult{
        void result(List<BasicPoolElement> pools);
    }

    public PoolsListUpdater(DataResult callback) {
        this.callback = callback;
    }

    @Override
    protected List<BasicPoolElement> doInBackground(List<BasicPoolElement>... lists) {
        int count;
        List<BasicPoolElement> pools = new ArrayList<>();
        pools.addAll(lists[0]);
        if (pools != null && pools.size() > 0) {
            count = pools.size();
            for (int i = 0; i < count; i++) {
                switch (pools.get(i).getPoolName()){
                    case "BTC.com":
                        try {
                        BTCcomElement element = (BTCcomElement) pools.get(i);
                        BTCComApiData tempElement;
                        Call<BTCComApiData> btcComApiDataCall = new BTCcomLoader().getBtcComApi()
                                .getElement(element.getAccess_key(), element.getPuid());
                            tempElement = btcComApiDataCall.execute().body();
                            pools.get(i).setCurrentHashRate(tempElement.getCurrentHashRate());
                            pools.get(i).setAvgHashRate(tempElement.getAvgHashRate());
                            pools.get(i).setActiveWorkers(tempElement.getWorkersActive());
                            pools.get(i).setInActiveWorkers(tempElement.getWorkersInActive());
                            btcComApiDataCall = new BTCcomLoader().getBtcComApi().getEarnStats(element.getAccess_key(), element.getPuid());
                            tempElement = btcComApiDataCall.execute().body();
                            pools.get(i).setBalance(tempElement.getUnpaidBalance()/100000000);
                            //Log.d("myLogs", String.valueOf(tempElement.getUnpaidBalance()/100000000));
                        }catch (Exception e) {
                            e.printStackTrace();
                            Log.d("myLogs", "error in async: " + e.toString());
                        }
                        break;
                    case "Ethermine.org":
                        try {
                            EthermineOrgElement element = (EthermineOrgElement) pools.get(i);
                            EthermineOrgApiData tempElement;
                            Call<EthermineOrgApiData> ethermineOrgApiDataCall = new EthermineOrgLoader()
                                    .getEthermineOrgAPI().getCurrentStats(element.getWalletAdress());
                            tempElement = ethermineOrgApiDataCall.execute().body();
                            pools.get(i).setActiveWorkers(tempElement.getActiveWorkers());
                            pools.get(i).setCurrentHashRate(tempElement.getCurrentHashRate());
                            pools.get(i).setAvgHashRate(tempElement.getAvgHashRate());
                            pools.get(i).setBalance(tempElement.getUnpaid());
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        }

        return pools;
    }

    @Override
    protected void onPostExecute(List<BasicPoolElement> pools){
        super.onPostExecute(pools);
        this.callback.result(pools);
    }
}
