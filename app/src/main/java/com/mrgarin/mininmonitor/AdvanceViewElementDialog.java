package com.mrgarin.mininmonitor;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.mrgarin.mininmonitor.Data.BTCcomElement;
import com.mrgarin.mininmonitor.Data.BasicPoolElement;
import com.mrgarin.mininmonitor.Data.EthermineOrgElement;

import java.util.ArrayList;

public class AdvanceViewElementDialog extends AppCompatDialogFragment {

    private int element_id;
    private ArrayList<BasicPoolElement> pools = new ArrayList<>();
    private Bundle bundle;

    private TextView title, activeMiners, inactiveMiners, balance, currentHashrate, avgHashrate;
    private EditText minersAlert, hashrateAlert;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //this.element_id = element_id;
        //this.pools.addAll(pools);
        //this.pools = pools;
        bundle = this.getArguments();
        if (bundle != null){
            element_id = bundle.getInt("element position");
        }
        View v = inflater.inflate(R.layout.advanced_pool_view, null);

        initView(v);

        return v;
    }

    private void initView(View v){
        title = v.findViewById(R.id.advancedPoolview_poolTitle);
        activeMiners = v.findViewById(R.id.advancedPoolview_activeMiners);
        inactiveMiners = v.findViewById(R.id.advancedPoolview_inActiveMiners);
        balance = v.findViewById(R.id.advancedPoolview_unpaidBalance);
        currentHashrate = v.findViewById(R.id.advancedPoolview_currentHashrate);
        avgHashrate = v.findViewById(R.id.advancedPoolview_avgHashrate);
        minersAlert = v.findViewById(R.id.advancedPoolview_minersAlert);
        hashrateAlert = v.findViewById(R.id.advancedPoolview_hashrateAlert);

        switch (pools.get(element_id).getPoolName()){
            case "BTC.com":
                BTCcomElement element = (BTCcomElement) pools.get(element_id);
                title.setText(element.getPoolName() + " Subaccount: " + element.getSubAccountName() + " Coin: " + element.getCoinName());
                activeMiners.setText(String.valueOf(element.getActiveWorkers()));
                inactiveMiners.setText(String.valueOf(element.getInActiveWorkers()));
                balance.setText(String.valueOf(element.getBalance()));
                currentHashrate.setText(String.valueOf(element.getCurrentHashRate()));
                avgHashrate.setText(String.valueOf(element.getAvgHashRate()));
                hashrateAlert.setText(String.valueOf(element.getAlert_MinCurrentHashrate()));
                minersAlert.setText(String.valueOf(element.getAlert_ActiveWorkers()));
                break;

            case "Ethermine.org":
                EthermineOrgElement ethermineOrgElement = (EthermineOrgElement) pools.get(element_id);
                title.setText(ethermineOrgElement.getPoolName() + " Wallet: " + ethermineOrgElement.getWalletAdress());
                activeMiners.setText(String.valueOf(ethermineOrgElement.getActiveWorkers()));
                inactiveMiners.setText(String.valueOf(ethermineOrgElement.getInActiveWorkers()));
                balance.setText(String.valueOf(ethermineOrgElement.getBalance()));
                currentHashrate.setText(String.valueOf(ethermineOrgElement.getCurrentHashRate()));
                avgHashrate.setText(String.valueOf(ethermineOrgElement.getAvgHashRate()));
                hashrateAlert.setText(String.valueOf(ethermineOrgElement.getAlert_MinCurrentHashrate()));
                minersAlert.setText(String.valueOf(ethermineOrgElement.getAlert_ActiveWorkers()));
                break;
            default:
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        switch (pools.get(element_id).getPoolName()){
            case "BTC.com":
                BTCcomElement btCcomElement = (BTCcomElement) miningDashboard.pools.get(element_id);
                if (!hashrateAlert.getText().toString().isEmpty()){
                    btCcomElement.setAlert_MinCurrentHashrate(Float.valueOf(hashrateAlert.getText().toString()));
                }
                if (!minersAlert.getText().toString().isEmpty()){
                    btCcomElement.setAlert_ActiveWorkers(Integer.valueOf(minersAlert.getText().toString()));
                }
                break;
            case "Ethermine.org":
                EthermineOrgElement ethermineOrgElement = (EthermineOrgElement) miningDashboard.pools.get(element_id);
                if (!hashrateAlert.getText().toString().isEmpty()){
                    ethermineOrgElement.setAlert_MinCurrentHashrate(Float.valueOf(hashrateAlert.getText().toString()));
                }
                if (!minersAlert.getText().toString().isEmpty()){
                    ethermineOrgElement.setAlert_ActiveWorkers(Integer.valueOf(minersAlert.getText().toString()));
                }
                break;
            default:
                break;
        }
    }

    private MiningDashboard miningDashboard;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        miningDashboard = (MiningDashboard) context;
        this.pools.addAll(miningDashboard.pools);
    }
}
