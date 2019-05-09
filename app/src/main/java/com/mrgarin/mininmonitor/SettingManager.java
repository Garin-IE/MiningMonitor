package com.mrgarin.mininmonitor;

import android.content.Context;
import android.content.SharedPreferences;

import com.mrgarin.mininmonitor.aplication.AppConfig;

public class SettingManager {

    private final static String SAVED_PREFERENCE = "saved_preference";

    protected static SharedPreferences sharedPreferences;

    public static void savePreference(Context context){
        sharedPreferences = context.getSharedPreferences(SAVED_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("updater_timer", AppConfig.userAutoUpdateTime);
        editor.commit();
    }

    public static void loadPreference(Context context){
        sharedPreferences = context.getSharedPreferences(SAVED_PREFERENCE, Context.MODE_PRIVATE);
        AppConfig.userAutoUpdateTime = sharedPreferences.getInt("updater_timer", 10);
    }
}
