package com.mrgarin.mininmonitor.Adapters;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.mrgarin.mininmonitor.Data.BTCcomElement;
import com.mrgarin.mininmonitor.Data.BasicPoolElement;
import com.mrgarin.mininmonitor.Data.EthermineOrgElement;
import com.mrgarin.mininmonitor.MiningDashboard;
import com.mrgarin.mininmonitor.R;
import com.mrgarin.mininmonitor.aplication.AppConfig;

import java.util.ArrayList;

public class NotificationHelper {
    private Context context;
    private NotificationManager manager;
    private NotificationChannel channel;
    private NotificationCompat.Builder builder;
    private Intent intent;
    private PendingIntent pendingIntent;
    final String alertAction = "com.mrgarin.miningmonitor.ALERT_NOTIFICATION";


    public NotificationHelper(Context context) {
        this.context = context;
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(context, AppConfig.ALERT_NOTIFICATION_CHANEL);
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        builder.setBadgeIconType(R.mipmap.ic_launcher_round);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            channel = new NotificationChannel(AppConfig.ALERT_NOTIFICATION_CHANEL, "Alerts",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Alert notification by pool data");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            manager.createNotificationChannel(channel);
        }
        intent = new Intent(context, MiningDashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }

    public void sendNotification(String title, String text){
        builder.setContentTitle(title);
        builder.setContentText(text);
        manager.notify(1, builder.build());
    }

    public Notification getForegroundServiceNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel serviceChannel = new NotificationChannel(
                    AppConfig.SERVICE_NOTIFICATION_CHANEL, "Services",
                    NotificationManager.IMPORTANCE_HIGH);
            serviceChannel.setDescription("Service notifications");
            serviceChannel.enableLights(false);
            serviceChannel.enableVibration(false);
            manager.createNotificationChannel(serviceChannel);
        }
        NotificationCompat.Builder serviceBuilder;
        serviceBuilder = new NotificationCompat.Builder(context, AppConfig.SERVICE_NOTIFICATION_CHANEL);
        serviceBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        serviceBuilder.setBadgeIconType(R.mipmap.ic_launcher_round);
        serviceBuilder.setContentTitle("Mining Monitor");
        serviceBuilder.setContentText("Mining Monitor service updating pools data");
        serviceBuilder.setOngoing(true);
        serviceBuilder.setCategory(Notification.CATEGORY_SERVICE);
        return serviceBuilder.build();
    }

    public void checkForAlerts(ArrayList<BasicPoolElement> pools){
        int count = pools.size();
        for (int i = 0; i < count; i++){
            switch (pools.get(i).getPoolName()){
                case "BTC.com":
                    checkForAlertsBTCcom((BTCcomElement) pools.get(i), i);
                    break;
                case "Ethermine.org":
                    chechForAlertsEthermineOrg((EthermineOrgElement) pools.get(i), i);
                    break;
                default:
                    break;
            }
        }
    }

    private void checkForAlertsBTCcom(BTCcomElement element, int i){
        boolean isNotified = false;
        if (element.getAlert_ActiveWorkers() > element.getActiveWorkers() && element.getAlert_MinCurrentHashrate() > element.getCurrentHashRate()){
            builder.setContentTitle("Warning BTC.com " + element.getSubAccountName());
            builder.setContentText("Low hashrate " + element.getCurrentHashRate() + " and workers " + element.getActiveWorkers());
            isNotified = true;
        } else {
            if (element.getAlert_ActiveWorkers() > element.getActiveWorkers()) {
                builder.setContentTitle("Warning BTC.com " + element.getSubAccountName());
                builder.setContentText("Low count of workers " + element.getActiveWorkers());
                Log.d("myLogs", "alert: " + element.getAlert_ActiveWorkers() + " current: " + element.getActiveWorkers());
                isNotified = true;
            }else {
                if (element.getAlert_MinCurrentHashrate() > element.getCurrentHashRate()){
                    builder.setContentTitle("Warning BTC.com " + element.getSubAccountName());
                    builder.setContentText("Low hashrate " + element.getCurrentHashRate());
                    isNotified = true;
                }
            }
        }
        if (isNotified) {
            intent.setAction(alertAction);
            intent.putExtra("position", i);
            pendingIntent = PendingIntent.getActivity(context, i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            manager.notify(i, builder.build());
        }
        else {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(i);
        }
    }

    private void chechForAlertsEthermineOrg(EthermineOrgElement element, int i){
        boolean isNotified = false;
        if (element.getAlert_ActiveWorkers() > element.getActiveWorkers() && element.getAlert_MinCurrentHashrate() > element.getCurrentHashRate()){
            builder.setContentTitle("Warning Ethermine.org");
            builder.setContentText("Low hashrate " + element.getCurrentHashRate() + " and workers " + element.getActiveWorkers());
            isNotified = true;
        }else {
            if (element.getAlert_ActiveWorkers() > element.getActiveWorkers()) {
                builder.setContentTitle("Warning Ethermine.org");
                builder.setContentText("Low workers " + element.getActiveWorkers());
                Log.d("myLogs", "alert: " + element.getAlert_ActiveWorkers() + " current: " + element.getActiveWorkers());
                isNotified = true;
            } else{
                if (element.getAlert_MinCurrentHashrate() > element.getCurrentHashRate()) {
                    builder.setContentTitle("Warning Ethermine.org");
                    builder.setContentText("Low hashrate " + element.getCurrentHashRate());
                    isNotified = true;
                }
            }
        }
        if (isNotified) {
            intent.setAction(alertAction);
            intent.putExtra("position", i);
            pendingIntent = PendingIntent.getActivity(context, i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            //builder.setDefaults(Notification.DEFAULT_VIBRATE);
            manager.notify(i, builder.build());
        }
        else {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(i);
        }
    }
}
