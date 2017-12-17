package com.example.hadasp.weather03.utilities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.example.hadasp.weather03.data.AlarmPreferences;
import com.example.hadasp.weather03.sync.WeatherSyncTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;

/**
 * Created by hadasp on 14/12/2017.
 */

public class AlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Set<String> preferredDays = AlarmPreferences.getPreferredDays(context);
        Log.e("AlarmReciever", "preferred days: " + preferredDays);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dfDay = new SimpleDateFormat("EEEE");
        String currentDay = dfDay.format(c.getTime());
        Log.e("AlarmReciever", "current day: " + currentDay);


        if (preferredDays.contains(currentDay)){
            Log.e("AlarmReciever", "preferredDays contains current day ");
            WeatherSyncTask.syncWeather(context);
            Log.e("AlarmReciever", "start  WeatherSyncTask.syncWeather");
           // WeatherSyncUtils.initialize(context);
        }
    }
}
