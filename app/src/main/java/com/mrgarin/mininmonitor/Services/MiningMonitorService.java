package com.mrgarin.mininmonitor.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.mrgarin.mininmonitor.Adapters.NotificationHelper;
import com.mrgarin.mininmonitor.Data.BasicPoolElement;
import com.mrgarin.mininmonitor.Data.PoolsInstanceState;
import com.mrgarin.mininmonitor.Data.PoolsListUpdater;
import com.mrgarin.mininmonitor.SettingManager;
import com.mrgarin.mininmonitor.aplication.AppConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MiningMonitorService extends Service {

    final static int AUTO_UPDATE_MSG = 1000;

    NotificationHelper notificationHelper;
    PoolsInstanceState poolsInstanceState;
    ArrayList<BasicPoolElement> pools = new ArrayList<>();
    static Map<String, Double> cryptoPriceList = new HashMap<>();
    MyBinder binder = new MyBinder();
    boolean bindToUI = false;
    Handler handler;
    Message message;
    final int RE_NEW_UI = 1002;

    public MiningMonitorService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (AppConfig.isDebug_mining_monitor_service){
            Log.d("myLogs", "MiningMonitorService: Service started");
        }

        SettingManager.loadPreference(getApplicationContext());
        notificationHelper = new NotificationHelper(getApplicationContext());
        poolsInstanceState = new PoolsInstanceState(getApplicationContext());
        pools.addAll(poolsInstanceState.loadInstance());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (AppConfig.isDebug_mining_monitor_service){
            Log.d("myLogs", "MiningMonitorService: Service stopped");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if (intent.getAction() != null && intent.getAction().equals("Foreground")){
            startForeground(50001, notificationHelper.getForegroundServiceNotification());
            Log.d("myLogs", "MiningMonitorService: startForeground");
            //List<BasicPoolElement> tempPools = new ArrayList<>();
            //tempPools.addAll(poolsInstanceState.loadInstance());
            //pools.addAll(tempPools);
            PoolsListUpdater updater = new PoolsListUpdater(new PoolsListUpdater.DataResult() {
                @Override
                public void result(List<BasicPoolElement> pools) {
                    setPools(pools);
                    if (AppConfig.isDebug_mining_monitor_service){
                        int count = pools.size();
                        for (int i = 0; i < count; i++){
                            Log.d("myLogs", pools.get(i).getPoolName() + " avg HashRate: "
                                    + pools.get(i).getAvgHashRate());
                        }
                    }
                    Log.d("myLogs", "MiningMonitorService: bintToUI: " + bindToUI);
                    if (bindToUI){
                        Log.d("myLogs", "MiningMonitorService: pool size onStartCommand: " + pools.size());
                        handler.sendEmptyMessage(RE_NEW_UI);
                    }
                    stopForeground(true);
                }
            });
            updater.execute(pools);
        }

        return START_NOT_STICKY;
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        if (AppConfig.isDebug_mining_monitor_service){
            Log.d("myLogs", "MiningMonitorService: onBind");
        }
        if (binder == null){
            binder = new MyBinder();
        }
        bindToUI = true;
        //throw new UnsupportedOperationException("Not yet implemented");
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        bindToUI = false;
        return super.onUnbind(intent);
    }

    public class MyBinder extends Binder{
        public MiningMonitorService getService(){
            return MiningMonitorService.this;
        }
    }

    public void setPools(List<BasicPoolElement> pools){
        this.pools.clear();
        this.pools.addAll(pools);
        notificationHelper.checkForAlerts((ArrayList<BasicPoolElement>) pools);
    }

    public void setHandler(Handler handler){
        this.handler = handler;
    }

    public List<BasicPoolElement> getPools(){
        Log.d("myLogs", "MiningMonitorService: pools = " + pools.size());
        return pools;
    }

}
