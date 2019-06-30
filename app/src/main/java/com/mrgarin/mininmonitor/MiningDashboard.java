package com.mrgarin.mininmonitor;

import android.Manifest;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mrgarin.mininmonitor.Adapters.NotificationHelper;
import com.mrgarin.mininmonitor.Adapters.PoolTouchHelperCallback;
import com.mrgarin.mininmonitor.Adapters.PoolsAdapter;
import com.mrgarin.mininmonitor.Adapters.PoolsViewOffsetDecoration;
import com.mrgarin.mininmonitor.BTCcom.BTCComApiData;
import com.mrgarin.mininmonitor.Data.BTCcomElement;
import com.mrgarin.mininmonitor.Data.BTCcomLoader;
import com.mrgarin.mininmonitor.Data.BasicPoolElement;
import com.mrgarin.mininmonitor.Data.BinanceApiData;
import com.mrgarin.mininmonitor.Data.BinanceCryptoPairPriceLoader;
import com.mrgarin.mininmonitor.Data.BinanceLoader;
import com.mrgarin.mininmonitor.Data.CryptoPrice;
import com.mrgarin.mininmonitor.Data.EthermineOrgApiData;
import com.mrgarin.mininmonitor.Data.EthermineOrgElement;
import com.mrgarin.mininmonitor.Data.EthermineOrgLoader;
import com.mrgarin.mininmonitor.Data.PoolsInstanceState;
import com.mrgarin.mininmonitor.Data.PoolsListUpdater;

import com.mrgarin.mininmonitor.Interfaces.OnAdvanceElementShow;
import com.mrgarin.mininmonitor.Interfaces.OnPoolAdd;
import com.mrgarin.mininmonitor.Services.MiningMonitorService;
import com.mrgarin.mininmonitor.aplication.AppConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MiningDashboard extends AppCompatActivity implements View.OnClickListener,
        OnPoolAdd, BTCcomLoader.OnBTCComPoolAdd, SwipeRefreshLayout.OnRefreshListener,
        EthermineOrgLoader.OnEthermineOrgPoolAdd, OnAdvanceElementShow {

    final static int AUTO_UPDATE_MSG = 1000;
    final static int BTC_COM_GET_BALANCE = 1001;
    final int RE_NEW_UI = 1002;
    NotificationHelper notificationHelper;

    BTCcomLoader btCcomLoader = new BTCcomLoader();
    FloatingActionButton fab_add;
    DialogFragment fab_add_dialog;
    ArrayList<BasicPoolElement> pools = new ArrayList<>();
    RecyclerView poolsView;
    PoolsAdapter poolsAdapter;
    PoolsInstanceState poolsInstanceState;
    SwipeRefreshLayout refreshLayout;
    Handler handler;
    Intent intent;
    String intentAction;
    static Map<String, Double> cryptoPriceList = new HashMap<>();
    ServiceConnection monitorServiceConnection;
    MiningMonitorService monitorService;
    boolean monitorBound = false;

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
                        setUnpaidBalance(pools);
                        //handler.sendEmptyMessageDelayed(AUTO_UPDATE_MSG, AppConfig.autoUpdateTime);
                        Log.d("myLogs", "Auto updater handler message posted");
                        notificationHelper.checkForAlerts(pools);
                        break;
                    }
                case RE_NEW_UI:
                    if (monitorBound){
                        //pools.clear();
                        //pools.addAll(monitorService.getPools());
                        //poolsAdapter.notifyDataSetChanged();
                        Log.d("myLogs", "MiningdDashBoard: returned pools count from service: " + monitorService.getPools().size());
                        setPools(monitorService.getPools());
                        setUnpaidBalance(pools);
                        //handler.sendEmptyMessageDelayed(RE_NEW_UI, AppConfig.autoUpdateTime);
                        Log.d("myLogs", "MiningDashboard: renew ui message sended");
                    }
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getBooleanExtra("background", false)){
            moveTaskToBack(true);
        }

        SettingManager.loadPreference(this);
        handler = new Handler(handlerCallback);

        Intent serviceIntent = new Intent(this, MiningMonitorService.class);
        monitorServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                if (AppConfig.debug){
                    Log.d("myLogs", "MiningDashboard: onServiceConnected");
                }
                monitorBound = true;
                monitorService = ((MiningMonitorService.MyBinder) service).getService();
                if (AppConfig.debug){
                    Log.d("myLogs", "MiningDashBoard: send first renew ui message");
                }
                //handler.sendEmptyMessageDelayed(RE_NEW_UI, AppConfig.autoUpdateTime);
                monitorService.setHandler(handler);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                if (AppConfig.debug){
                    Log.d("myLogs", "MiningDashboard: onServiceDisconnected");
                }
                monitorBound = false;
            }
        };
        bindService(serviceIntent, monitorServiceConnection, BIND_AUTO_CREATE);
        startService(new Intent(this, MiningMonitorService.class));

        setContentView(R.layout.activity_mining_dashboard);

        notificationHelper = new NotificationHelper(this);

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
        setUnpaidBalance(pools);

        //monitorService.pushToUI(pools);
        Log.d("myLogs", "MiningDashBoard: monitorBound = " + monitorBound);

        Intent sIntent = new Intent();
        sIntent.setAction("ReNewService");
        sendBroadcast(sIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppConfig.reNewAutoUpdate){
            //handler.removeMessages(AUTO_UPDATE_MSG);
            //handler.sendEmptyMessageDelayed(AUTO_UPDATE_MSG, AppConfig.autoUpdateTime);
            if (monitorBound){
                //monitorService.reNewHandler();
            }
            AppConfig.reNewAutoUpdate = false;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int elementPosition = intent.getIntExtra("position", -1);
        Log.d("myLogs", "onNewIntent Action: " + intent.getAction());
        Log.d("myLogs", "onNewIntent position in extra: " + elementPosition);
        if (intent.getAction().equals("com.mrgarin.miningmonitor.ALERT_NOTIFICATION")
                && elementPosition >= 0){
            onAdvanceElementShow(intent.getIntExtra("position", -1));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        poolsInstanceState.saveInstance(pools);
        SettingManager.savePreference(this);
        Log.d("myLogs", "File Saved");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (monitorBound) {
            unbindService(monitorServiceConnection);
        }
        stopService(new Intent(this, MiningMonitorService.class));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            default:
                break;
            case R.id.menu_refresh:
                refreshLayout.setRefreshing(true);
                onRefresh();
                break;
            case R.id.menu_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            case R.id.menu_about:
                Intent aboutIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutIntent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setUnpaidBalance(final ArrayList<BasicPoolElement> pools){

        BinanceCryptoPairPriceLoader loader = new BinanceCryptoPairPriceLoader(new BinanceCryptoPairPriceLoader.IDataResult() {
            @Override
            public void result(Map<String, Double> map) {
                cryptoPriceList.putAll(map);
                setTotalUnpaidBalance(pools);
            }
        });
        loader.execute();
    }

    public void setTotalUnpaidBalance(ArrayList<BasicPoolElement> pools){
        double totalUnpaidBalance = 0;
        int count = pools.size();
        for (int i=0; i < count; i++){
            switch (pools.get(i).getPoolName()){
                default:
                    break;
                case "Ethermine.org":
                    totalUnpaidBalance += pools.get(i).getBalance()*cryptoPriceList.get("ETHUSDT");
                    break;
                case "BTC.com":
                    BTCcomElement element = (BTCcomElement) pools.get(i);
                    if (element.getCoinName().equals("BCC")){
                        totalUnpaidBalance += element.getBalance()*cryptoPriceList.get("BCCUSDT");
                        break;
                    }
                    if (element.getCoinName().equals("BTC")){
                        totalUnpaidBalance += element.getBalance()*cryptoPriceList.get("BTCUSDT");
                        break;
                    }
            }
        }
        TextView tv_unpaidBalance = findViewById(R.id.dashboard_unpaid_balance);
        tv_unpaidBalance.setText(String.format("%.2f", totalUnpaidBalance) + " USD");
    }

    public void setPoolsToService(){
        poolsInstanceState.saveInstance(pools);
        monitorService.setPools(pools);
    }
}
