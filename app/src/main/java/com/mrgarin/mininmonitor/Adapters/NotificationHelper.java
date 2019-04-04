package com.mrgarin.mininmonitor.Adapters;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.mrgarin.mininmonitor.Data.BTCcomElement;
import com.mrgarin.mininmonitor.Data.BasicPoolElement;
import com.mrgarin.mininmonitor.Data.EthermineOrgElement;
import com.mrgarin.mininmonitor.R;
import com.mrgarin.mininmonitor.aplication.AppConfig;

import java.util.ArrayList;

public class NotificationHelper {
    private Context context;
    private NotificationManager manager;
    private NotificationChannel channel;
    private NotificationCompat.Builder builder;


    public NotificationHelper(Context context) {
        this.context = context;
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(context, AppConfig.ALERT_NOTIFICATION_CHANEL);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            channel = new NotificationChannel(AppConfig.ALERT_NOTIFICATION_CHANEL, "Alerts", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Alert notification by pool data");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            manager.createNotificationChannel(channel);
        }
    }

    public void sendNotification(String title, String text){
        builder.setContentTitle(title);
        builder.setContentText(text);
        manager.notify(1, builder.build());
    }

    public void checkForAlerts(ArrayList<BasicPoolElement> pools){
        int count = pools.size();
        for (int i = 0; i < count; i++){
            switch (pools.get(i).getPoolName()){
                case "BTC.com":
                    checkForAlertsBTCcom((BTCcomElement) pools.get(i));
                    break;
                case "Ethermine.org":
                    chechForAlertsEthermineOrg((EthermineOrgElement) pools.get(i));
                    break;
                default:
                    break;
            }
        }
    }

    private void checkForAlertsBTCcom(BTCcomElement element){
        if (element.getAlert_ActiveWorkers() > element.getActiveWorkers() && element.getAlert_MinCurrentHashrate() > element.getCurrentHashRate()){
            builder.setContentTitle("Warning BTC.com " + element.getSubAccountName());
            builder.setContentText("Low hashrate and workers");
            manager.notify(1, builder.build());
            return;
        }
        if (element.getAlert_ActiveWorkers() > element.getActiveWorkers()){
            builder.setContentTitle("Warning BTC.com " + element.getSubAccountName());
            builder.setContentText("Low count of workers");
            manager.notify(1, builder.build());
            return;
        }
        if (element.getAlert_MinCurrentHashrate() > element.getCurrentHashRate()){
            builder.setContentTitle("Warning BTC.com " + element.getSubAccountName());
            builder.setContentText("Low hashrate");
            manager.notify(1, builder.build());
            return;
        }
    }

    private void chechForAlertsEthermineOrg(EthermineOrgElement element){
        if (element.getAlert_ActiveWorkers() > element.getActiveWorkers() && element.getAlert_MinCurrentHashrate() > element.getCurrentHashRate()){
            builder.setContentTitle("Warning Ethermine.org");
            builder.setContentText("Low hashrate and workers on wallet: " + element.getWalletAdress());
            manager.notify(2, builder.build());
            return;
        }
        if (element.getAlert_ActiveWorkers() > element.getActiveWorkers()){
            builder.setContentTitle("Warning Ethermine.org");
            builder.setContentText("Low workers on wallet: " + element.getWalletAdress());
            manager.notify(2, builder.build());
            return;
        }
        if (element.getAlert_MinCurrentHashrate() > element.getCurrentHashRate()){
            builder.setContentTitle("Warning Ethermine.org");
            builder.setContentText("Low hashrate on wallet: " + element.getWalletAdress());
            manager.notify(2, builder.build());
            return;
        }
    }
}
