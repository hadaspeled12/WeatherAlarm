package com.example.hadasp.weather03;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v14.preference.MultiSelectListPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.util.Log;

import com.example.hadasp.weather03.data.AlarmPreferences;
import com.example.hadasp.weather03.utilities.AlarmReceiver;
import com.example.hadasp.weather03.utilities.TimePreference;
import com.example.hadasp.weather03.utilities.TimePreferenceDialogFragmentCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import static com.example.hadasp.weather03.MainActivity.pendingIntent;

/**
 * Created by hadasp on 29/11/2017.
 */

public class EditAlarmFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {


    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        // Try if the preference is one of our custom Preferences
        TimePreferenceDialogFragmentCompat dialogFragment = null;
        if (preference instanceof TimePreference) {
            // Create a new instance of TimePreferenceDialogFragment with the key of the related
            // Preference
            dialogFragment = TimePreferenceDialogFragmentCompat
                    .newInstance(preference.getKey());
        }

        // If it was one of our cutom Preferences, show its dialog
        if (dialogFragment != null) {
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(this.getFragmentManager(),
                    "android.support.v7.preference" +
                            ".PreferenceFragment.DIALOG");
        }
        // Could not be handled here. Try with the super method.
        else {
            super.onDisplayPreferenceDialog(preference);
        }
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_alarm);


        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen prefScreen = getPreferenceScreen();
        setSummary(sharedPreferences, prefScreen);

    }
    @Override
    public void onStop() {
        super.onStop();
        // unregister the preference change listener
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // register the preference change listener
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }


    private void setSummary(SharedPreferences sharedPreferences, PreferenceScreen prefScreen) {
        int count = prefScreen.getPreferenceCount();
        for (int i = 0; i < count; i++) {
            Preference p = prefScreen.getPreference(i);
            if (p instanceof ListPreference){
                String value = sharedPreferences.getString(p.getKey(), "");
                ListPreference listPreference = (ListPreference) p;
                int prefIndex = listPreference.findIndexOfValue(value);
                if (prefIndex >= 0) {
                    p.setSummary(listPreference.getEntries()[prefIndex]);
                }
            } else if (p instanceof MultiSelectListPreference){
                Set<String> value = sharedPreferences.getStringSet(p.getKey(),
                        new HashSet<String>());
                String summary = "";
                Object[] valueArray = value.toArray();
                MultiSelectListPreference multiSelectListPreference = (MultiSelectListPreference) p;
                for (int j = 0; j < valueArray.length; j++){
                    int prefIndex = multiSelectListPreference.findIndexOfValue(
                            valueArray[j].toString());
                    if (prefIndex >= 0 && j != valueArray.length-1) {
                        summary = summary + multiSelectListPreference.getEntries()[prefIndex]  + " , ";
                    } else if (prefIndex >= 0 && j == valueArray.length-1){
                        summary = summary + multiSelectListPreference.getEntries()[prefIndex];
                    }
                }
                p.setSummary(summary);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Activity activity = getActivity();
        MainActivity.alarmManager.cancel(pendingIntent);

        if (key.equals(getString(R.string.pref_days_key))) {
            AlarmPreferences.resetDays(activity);
        } else if (key.equals(getString(R.string.pref_on_switch_key))) {
            AlarmPreferences.resetOnSwitch(activity);
        } else if (key.equals("key4")){
            Log.e("SharedPreferenceChanged", "INTERESTING");

        }
        //MainActivity.alarmManager.cancel(pendingIntent);
        Log.e("SharedPreferenceChanged", "start");
        PreferenceScreen prefScreen = getPreferenceScreen();

        Log.e("SharedPreferenceChanged", "got preference screen");
        setSummary(sharedPreferences, prefScreen);
        Log.e("SharedPreferenceChanged", "set summary");

        boolean notificationsEnabled = AlarmPreferences.areNotificationsEnabled(activity);

        Log.e("SharedPreferenceChanged", "got if alarm is on " + notificationsEnabled);
        Set<String> preferredDays = AlarmPreferences.getPreferredDays(activity);

        Log.e("SharedPreferenceChanged", "got preferred days" + preferredDays);
        Calendar c = Calendar.getInstance();

        Log.e("SharedPreferenceChanged", "got calander instance");
        SimpleDateFormat dfDay = new SimpleDateFormat("EEEE");
        Log.e("SharedPreferenceChanged", "got simple date format");
        String currentDay = dfDay.format(c.getTime());
        Log.e("SharedPreferenceChanged", "got current day " + currentDay);
        for (int i = 0; i< prefScreen.getPreferenceCount(); i++){
            Preference p = prefScreen.getPreference(i);
            if (p instanceof TimePreference){
                TimePreference timePreference = (TimePreference) p;
                int hours = timePreference.getTime() / 60;
                int minutes = timePreference.getTime() % 60;

                Log.e("SharedPreferenceChanged", "got hours "
                        + hours + "\n got minutes " + minutes);
                c.set(Calendar.HOUR_OF_DAY, hours);
                c.set(Calendar.MINUTE, minutes);

            }
        }

        Intent myIntent = new Intent(activity, AlarmReceiver.class);

        Log.e("SharedPreferenceChanged", "got my intent");
        pendingIntent = PendingIntent.getBroadcast(getContext(), 0, myIntent, 0);

        Log.e("SharedPreferenceChanged", "got pending intent");
        if (notificationsEnabled){

            Log.e("SharedPreferenceChanged", "notification is enabled");
            MainActivity.alarmManager.set(AlarmManager.RTC, c.getTimeInMillis(), pendingIntent);
            Log.e("SharedPreferenceChanged", "started the alarm manager");
        }
    }
}
