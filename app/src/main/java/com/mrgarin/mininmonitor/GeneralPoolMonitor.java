package com.mrgarin.mininmonitor;

import android.util.Log;

public abstract class GeneralPoolMonitor {
    private String poolName;
    private int rt_HashRate, avg_HashRate;
    private double unpaidBalance;

    protected void setPoolName(String name) {
        if (name != null && !name.equals("")) {
            poolName = name;
        }
        else {
            Log.d("myLogs", "setPollName error " + name);
        }
    }
    protected String getPoolName() {
        return poolName;
    }

    protected void setRt_HashRate(int hashRate) {
        if ( !(hashRate < 0)) {
            rt_HashRate = hashRate;
        }
        else {
            Log.d("myLogs", "RealTime HashRate error " + hashRate);
        }
    }
    protected int getRt_HashRate() {
        return rt_HashRate;
    }

    protected void setAvg_HashRate(int hashRate) {
        if (!(hashRate < 0)) {
            avg_HashRate = hashRate;
        }
        else {
            Log.d("myLogs", "Average HashRate error " + hashRate);
        }
    }
    protected int getAvg_HashRate() {
        return avg_HashRate;
    }

    protected void setUnpaidBalance(double balance) {
        if (!(balance < 0)) {
            unpaidBalance = balance;
        }
        else {
            Log.d("myLogs", "Unpaid Balance error " + balance);
        }
    }
    protected double getUnpaidBalance(){
        return unpaidBalance;
    }
}
