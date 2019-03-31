package com.mrgarin.mininmonitor.Data;

import com.mrgarin.mininmonitor.BTCcom.BTCComApiData;

import java.util.List;

import retrofit2.Call;

public class PoolsPackageLoader{

    public void updatePools(List<BasicPoolElement> poolElements){

        int count;


        if (poolElements != null && poolElements.size() > 0) {
            count = poolElements.size();
            for (int i = 0; i < count; i++) {
                switch (poolElements.get(i).getPoolName()){
                    case "BTC.com":
                        BTCcomElement element = (BTCcomElement) poolElements.get(i);
                        BTCcomElement tempElement;
                        Call<BTCComApiData> btcComApiDataCall = new BTCcomLoader().getBtcComApi().getElement(element.getAccess_key(), element.getPuid());
                        //tempElement = btcComApiDataCall.execute();
                }
            }
        }
    }
}
