package com.mrgarin.mininmonitor;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.mrgarin.mininmonitor.Adapters.PoolTouchHelperCallback;
import com.mrgarin.mininmonitor.Adapters.PoolsAdapter;
import com.mrgarin.mininmonitor.Adapters.PoolsViewOffsetDecoration;
import com.mrgarin.mininmonitor.BTCcom.BTCComApiData;
import com.mrgarin.mininmonitor.Data.BTCcomElement;
import com.mrgarin.mininmonitor.Data.BTCcomLoader;
import com.mrgarin.mininmonitor.Data.BasicPoolElement;
import com.mrgarin.mininmonitor.Data.EthermineOrgApiData;
import com.mrgarin.mininmonitor.Data.EthermineOrgElement;
import com.mrgarin.mininmonitor.Data.EthermineOrgLoader;
import com.mrgarin.mininmonitor.Data.PoolsInstanceState;
import com.mrgarin.mininmonitor.Data.PoolsListUpdater;

import com.mrgarin.mininmonitor.Interfaces.OnAdvanceElementShow;
import com.mrgarin.mininmonitor.Interfaces.OnPoolAdd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MiningDashboard extends AppCompatActivity implements View.OnClickListener,
        OnPoolAdd, BTCcomLoader.OnBTCComPoolAdd, SwipeRefreshLayout.OnRefreshListener,
        EthermineOrgLoader.OnEthermineOrgPoolAdd, OnAdvanceElementShow {

    final static int AUTO_UPDATE_MSG = 1000;
    final static int BTC_COM_GET_BALANCE = 1001;

    BTCcomLoader btCcomLoader = new BTCcomLoader();
    FloatingActionButton fab_add;
    DialogFragment fab_add_dialog;
    ArrayList<BasicPoolElement> pools = new ArrayList<>();
    RecyclerView poolsView;
    PoolsAdapter poolsAdapter;
    PoolsInstanceState poolsInstanceState;
    SwipeRefreshLayout refreshLayout;
    Handler handler;

    Handler.Callback handlerCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case AUTO_UPDATE_MSG:
                    if (pools != null && pools.size() > 0){
                        PoolsListUpdater updater = new PoolsListUpdater(new PoolsListUpdater.DataResult() {
                            @Override
                            public void result(List<BasicPoolElement> pools) {
                                setPools(pools);
                            }
                        });
                        updater.execute(pools);
                        handler.sendEmptyMessageDelayed(AUTO_UPDATE_MSG, 300000);
                        Log.d("myLogs", "Auto updater handler message posted");
                        break;
                    }
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mining_dashboard);

        fab_add = findViewById(R.id.fab_add);
        fab_add.setOnClickListener(this);
        fab_add_dialog = new FabAddDialog();

        poolsView = findViewById(R.id.rv_Dashboard);
        poolsAdapter = new PoolsAdapter(this, pools, this);
        poolsView.setAdapter(poolsAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        poolsView.setLayoutManager(layoutManager);
        poolsView.addItemDecoration(new PoolsViewOffsetDecoration(20));
        ItemTouchHelper.Callback poolTouchCallback = new PoolTouchHelperCallback(poolsAdapter);
        ItemTouchHelper poolTouchHelper = new ItemTouchHelper(poolTouchCallback);
        poolTouchHelper.attachToRecyclerView(poolsView);

        refreshLayout = findViewById(R.id.dashboard_swipe_refresher);
        refreshLayout.setOnRefreshListener(this);

        poolsInstanceState = new PoolsInstanceState(this);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        List<BasicPoolElement> tempPools = new ArrayList<>();
        tempPools.addAll(poolsInstanceState.loadInstance());
        pools.addAll(tempPools);
        PoolsListUpdater updater = new PoolsListUpdater(new PoolsListUpdater.DataResult() {
            @Override
            public void result(List<BasicPoolElement> pools) {
                setPools(pools);
            }
        });
        updater.execute(pools);

        handler = new Handler(handlerCallback);
        handler.sendEmptyMessageDelayed(AUTO_UPDATE_MSG, 300000);
        Log.d("myLogs", "Auto updater handler message posted");
    }

    @Override
    protected void onPause() {
        super.onPause();
        poolsInstanceState.saveInstance(pools);
        Log.d("myLogs", "File Saved");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add:
                fab_add_dialog.show(getSupportFragmentManager(), "fab_add_dialog");
                break;
            default:
                break;
        }
    }

    @Override
    public void onPoolAdd(BasicPoolElement element) {

    }

    @Override
    public void onBTCComPoolAdd(final BTCcomElement element) {
        Call<BTCComApiData> btcComApiDataCall = btCcomLoader.getBtcComApi().getElement(element.getAccess_key(), element.getPuid());
        final Call<BTCComApiData> btcComApiDataCall2 = btCcomLoader.getBtcComApi().getEarnStats(element.getAccess_key(), element.getPuid());
        btcComApiDataCall.enqueue(new Callback<BTCComApiData>() {
            @Override
            public void onResponse(Call<BTCComApiData> call, Response<BTCComApiData> response) {
                if (response.body().getErr().contentEquals("0")) {
                    element.initData(response.body().getWorkersActive(), response.body().getWorkersInActive(), response.body().getAvgHashRate(), response.body().getCurrentHashRate());
                    btcComApiDataCall2.enqueue(new Callback<BTCComApiData>() {
                        @Override
                        public void onResponse(Call<BTCComApiData> call, Response<BTCComApiData> response) {
                            if (response.body().getErr().contentEquals("0")){
                                element.setBalance(response.body().getUnpaidBalance()/100000000);
                                pools.add(element);
                                poolsAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(Call<BTCComApiData> call, Throwable t) {

                        }
                    });
                    Log.d("myLogs", "pool name: " + element.getPoolName() + " Active workers: " + element.getActiveWorkers() + " InActive Workers: " + element.getInActiveWorkers() + " Current HashRate: " + element.getCurrentHashRate() + " Avg HashRate: " + element.getAvgHashRate());
                    Log.d("myLogs", "pools size: " + pools.size());
                }
                else {
                    Toast.makeText(getBaseContext(), "Ошибка загрузки данных: " + response.body().getErr(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BTCComApiData> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Ошибка: " + t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onEthermineOrgPoolAdd(final EthermineOrgElement element) {
        Call<EthermineOrgApiData> ethermineOrgApiDataCall = new EthermineOrgLoader()
                .getEthermineOrgAPI().getCurrentStats(element.getWalletAdress());
        ethermineOrgApiDataCall.enqueue(new Callback<EthermineOrgApiData>() {
            @Override
            public void onResponse(Call<EthermineOrgApiData> call, Response<EthermineOrgApiData> response) {
                if (response.body().getStatus().equals("OK")){
                    element.init(response.body().getAvgHashRate(), response.body().getCurrentHashRate(), response.body().getActiveWorkers());
                    element.setBalance(response.body().getUnpaid());
                    pools.add(element);
                    poolsAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getBaseContext(), "Ошибка загрузки данных: " + response.body().getStatus(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<EthermineOrgApiData> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Ошибка загрузки данных: " + t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setPools(List<BasicPoolElement> pools){
        this.pools.clear();
        this.pools.addAll(pools);
        poolsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        PoolsListUpdater updater = new PoolsListUpdater(new PoolsListUpdater.DataResult() {
            @Override
            public void result(List<BasicPoolElement> pools) {
                setPools(pools);
                refreshLayout.setRefreshing(false);
            }
        });
        updater.execute(pools);
    }

    @Override
    public void onAdvanceElementShow(int i) {
        DialogFragment advanceElementView = new AdvanceViewElementDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("element position", i);
        advanceElementView.setArguments(bundle);
        advanceElementView.show(getSupportFragmentManager(),"Advance");
        //Toast.makeText(this, "Выбран: " + pools.get(i).getPoolName(), Toast.LENGTH_LONG).show();
    }
}
