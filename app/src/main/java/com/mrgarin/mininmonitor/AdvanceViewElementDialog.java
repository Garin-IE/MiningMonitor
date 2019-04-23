package com.mrgarin.mininmonitor;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mrgarin.mininmonitor.Adapters.PoolsViewOffsetDecoration;
import com.mrgarin.mininmonitor.Adapters.WorkersAdapter;
import com.mrgarin.mininmonitor.BTCcom.BTCComApiDataObjectList;
import com.mrgarin.mininmonitor.Data.BTCcomElement;
import com.mrgarin.mininmonitor.Data.BTCcomLoader;
import com.mrgarin.mininmonitor.Data.BasicPoolElement;
import com.mrgarin.mininmonitor.Data.EthermineOrgApiDataList;
import com.mrgarin.mininmonitor.Data.EthermineOrgElement;
import com.mrgarin.mininmonitor.Data.EthermineOrgLoader;
import com.mrgarin.mininmonitor.Data.Worker;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdvanceViewElementDialog extends AppCompatDialogFragment {

    private int element_id;
    private ArrayList<BasicPoolElement> pools = new ArrayList<>();
    private ArrayList<Worker> workers;
    private Bundle bundle;
    private WorkersAdapter adapter;
    private RecyclerView rvWorkers;

    private TextView title, activeMiners, inactiveMiners, balance, currentHashrate, avgHashrate, reportTitle;
    private EditText minersAlert, hashrateAlert;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bundle = this.getArguments();
        if (bundle != null){
            element_id = bundle.getInt("element position");
        }
        View v = inflater.inflate(R.layout.advanced_pool_view, null);

        initView(v);
        workers = new ArrayList<>();
        adapter = new WorkersAdapter(getContext(), workers);
        rvWorkers = v.findViewById(R.id.advancedPoolview_rvMiners);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvWorkers.setLayoutManager(layoutManager);
        rvWorkers.setAdapter(adapter);
        rvWorkers.addItemDecoration(new PoolsViewOffsetDecoration(20));

        NotificationManager manager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        manager.cancel(element_id);

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
        reportTitle = v.findViewById(R.id.advancedPoolview_reporthashratetitle);

        switch (pools.get(element_id).getPoolName()){
            case "BTC.com":
                BTCcomElement element = (BTCcomElement) pools.get(element_id);
                title.setText(element.getPoolName() + " Subaccount: " + element.getSubAccountName() + " Coin: " + element.getCoinName());
                activeMiners.setText(String.valueOf(element.getActiveWorkers()));
                inactiveMiners.setText(String.valueOf(element.getInActiveWorkers()));
                balance.setText(String.format("%.8f", element.getBalance()));
                //balance.setText(String.valueOf(element.getBalance()));
                currentHashrate.setText(String.valueOf(element.getCurrentHashRate()));
                avgHashrate.setText(String.valueOf(element.getAvgHashRate()));
                hashrateAlert.setText(String.valueOf(element.getAlert_MinCurrentHashrate()));
                minersAlert.setText(String.valueOf(element.getAlert_ActiveWorkers()));
                Call<BTCComApiDataObjectList> objectListCall = new BTCcomLoader().getBtcComApi().getWorkers(element.getAccess_key(), element.getPuid());
                objectListCall.enqueue(new Callback<BTCComApiDataObjectList>() {
                    @Override
                    public void onResponse(Call<BTCComApiDataObjectList> call, Response<BTCComApiDataObjectList> response) {
                        if (response.body().getErr().equals("0")){
                            int count = response.body().getObjectList().getDataLists().size();
                            for (int i = 0; i < count; i++){
                                Worker worker = new Worker();
                                worker.setWorkerName(response.body().getObjectList().getDataLists().get(i).getWorkerName());
                                worker.setCurrentHashrate(response.body().getObjectList().getDataLists().get(i).getCurrentHashrate());
                                worker.setAvgHashrate(response.body().getObjectList().getDataLists().get(i).getAvgHashrate());
                                workers.add(worker);
                            }
                            adapter.notifyDataSetChanged();
                            reportTitle.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<BTCComApiDataObjectList> call, Throwable t) {
                        Log.d("myLogs", "Ошибка загрузки воркеров: " + t.toString());
                    }
                });
                break;

            case "Ethermine.org":
                EthermineOrgElement ethermineOrgElement = (EthermineOrgElement) pools.get(element_id);
                title.setText(ethermineOrgElement.getPoolName() + " Wallet: " + ethermineOrgElement.getWalletAdress());
                activeMiners.setText(String.valueOf(ethermineOrgElement.getActiveWorkers()));
                inactiveMiners.setText(String.valueOf(ethermineOrgElement.getInActiveWorkers()));
                balance.setText(String.format("%.8f", ethermineOrgElement.getBalance()));
                //balance.setText(String.valueOf(ethermineOrgElement.getBalance()));
                currentHashrate.setText(String.valueOf(ethermineOrgElement.getCurrentHashRate()));
                avgHashrate.setText(String.valueOf(ethermineOrgElement.getAvgHashRate()));
                hashrateAlert.setText(String.valueOf(ethermineOrgElement.getAlert_MinCurrentHashrate()));
                minersAlert.setText(String.valueOf(ethermineOrgElement.getAlert_ActiveWorkers()));
                Call<EthermineOrgApiDataList> ethermineOrgApiDataListCall = new EthermineOrgLoader()
                        .getEthermineOrgAPI().getWorkers(ethermineOrgElement.getWalletAdress());
                ethermineOrgApiDataListCall.enqueue(new Callback<EthermineOrgApiDataList>() {
                    @Override
                    public void onResponse(Call<EthermineOrgApiDataList> call, Response<EthermineOrgApiDataList> response) {
                        if (response.body().getStatus().equals("OK")){
                            int count = response.body().getDataResponseList().size();
                            for (int i = 0; i < count; i++){
                                Worker worker = new Worker();
                                worker.setWorkerName(response.body().dataResponseList.get(i).worker);
                                worker.setReportedHashrate(response.body().dataResponseList.get(i).reportedHashrate/1000000);
                                worker.setCurrentHashrate(response.body().dataResponseList.get(i).currentHashrate/1000000);
                                worker.setAvgHashrate(response.body().dataResponseList.get(i).avgHashrate/1000000);
                                Log.d("myLogs", worker.getWorkerName());
                                workers.add(worker);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<EthermineOrgApiDataList> call, Throwable t) {
                        Log.d("myLogs", "Ошибка загрузки воркеров " + t.toString());
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Boolean needNotify = false;
        switch (pools.get(element_id).getPoolName()){
            case "BTC.com":
                BTCcomElement btCcomElement = (BTCcomElement) miningDashboard.pools.get(element_id);
                if (!hashrateAlert.getText().toString().isEmpty()){
                    btCcomElement.setAlert_MinCurrentHashrate(Float.valueOf(hashrateAlert.getText().toString()));
                    needNotify = true;
                }
                if (!minersAlert.getText().toString().isEmpty()){
                    btCcomElement.setAlert_ActiveWorkers(Integer.valueOf(minersAlert.getText().toString()));
                    needNotify = true;
                }
                break;
            case "Ethermine.org":
                EthermineOrgElement ethermineOrgElement = (EthermineOrgElement) miningDashboard.pools.get(element_id);
                if (!hashrateAlert.getText().toString().isEmpty()){
                    ethermineOrgElement.setAlert_MinCurrentHashrate(Float.valueOf(hashrateAlert.getText().toString()));
                    needNotify = true;
                }
                if (!minersAlert.getText().toString().isEmpty()){
                    ethermineOrgElement.setAlert_ActiveWorkers(Integer.valueOf(minersAlert.getText().toString()));
                    needNotify = true;
                }
                break;
            default:
                break;
        }
        if (needNotify){
            Toast.makeText(getContext(), "Alerts saved", Toast.LENGTH_SHORT).show();
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
