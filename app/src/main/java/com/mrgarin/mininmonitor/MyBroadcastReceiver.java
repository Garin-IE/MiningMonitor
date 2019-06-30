package com.mrgarin.mininmonitor;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import com.mrgarin.mininmonitor.Services.MiningMonitorService;
import com.mrgarin.mininmonitor.aplication.AppConfig;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        prepareToStartReceiver(context);
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            if (AppConfig.debug){
                Log.d("myLogs", "Broadcast Receiver: get action: " + intent.getAction());
            }
            startMonitorService(context);
        }
        if (intent.getAction().equals("ReNewService")){
            Log.d("myLogs", "Broadcast Receiver: get action: " + intent.getAction());
            startMonitorService(context);
        }
    }

    private void startActivity(Context context){
        Intent startAppIntent = new Intent(context, MiningDashboard.class);
        startAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startAppIntent.putExtra("background", true);
        context.startActivity(startAppIntent);
    }

    private void startMonitorService(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MiningMonitorService.class);
        intent.setAction("Foreground");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            PendingIntent pIntent = PendingIntent.getForegroundService(context, 1, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pIntent);
            if (AppConfig.autoUpdatePools) {
                alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                        SystemClock.elapsedRealtime() + 3000, AppConfig.autoUpdateTime, pIntent);
            }
            else {
                alarmManager.set(AlarmManager.ELAPSED_REALTIME,
                        SystemClock.elapsedRealtime() + 3000,pIntent);
            }
        }
        else {
            PendingIntent pIntent = PendingIntent.getService(context, 1, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pIntent);
            if (AppConfig.autoUpdatePools) {
                alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                        SystemClock.elapsedRealtime() + 3000, AppConfig.autoUpdateTime, pIntent);
            }
            else {
                context.startService(intent);
            }
        }
    }

    private void prepareToStartReceiver(Context context){
        SettingManager.loadPreference(context);
        if (AppConfig.debug){
            Log.d("myLogs", "MyBroadcastReceiver: auto update: " +AppConfig.autoUpdatePools);
            Log.d("myLogs", "MyBroadcastReceiver: auto update time: " +AppConfig.autoUpdateTime);
        }
    }
}
