package com.mrgarin.mininmonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            Intent startAppIntent = new Intent(context, MiningDashboard.class);
            startAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startAppIntent.putExtra("background", true);
            context.startActivity(startAppIntent);
        }
    }
}
