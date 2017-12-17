package com.example.hadasp.weather03.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.example.hadasp.weather03.R;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by hadasp on 29/11/2017.
 */

public final class AlarmPreferences {

    public static final String PREF_HOURS = "hours";
    public static final String PREF_MINUTES = "minutes";

    public static final String PREF_DAYS = "days";

    public static final String PREF_ON_SWITCH = "onswitch";

    public static void setDays(Context context, Set<String> days) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putStringSet(PREF_DAYS, days);
        editor.apply();
    }

    public static void resetDays(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.remove(PREF_DAYS);
        editor.apply();
    }

    public static Set<String> getPreferredDays(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String keyForDays = context.getString(R.string.pref_days_key);
        Set<String> defaultDays = new HashSet<String>(Arrays.asList(context.getResources()
                .getStringArray(R.array.pref_days_options)));
        //String allDays = "";
        //if (defaultDays.length > 0){
        //   allDays = defaultDays[0];
        //   for (int i = 1; i< defaultDays.length; i++){
        //       allDays = allDays + ", " + defaultDays[i];
        //   }
        //}
        return sp.getStringSet(keyForDays, defaultDays);
    }

    public static boolean areNotificationsEnabled(Context context) {
        /* Key for accessing the preference for showing notifications */
        String displayNotificationsKey = context.getString(R.string.pref_on_switch_key);

        boolean shouldDisplayNotificationsByDefault = context
                .getResources()
                .getBoolean(R.bool.show_notifications_by_default);

        /* As usual, we use the default SharedPreferences to access the user's preferences */
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        /* If a value is stored with the key, we extract it here. If not, use a default. */
        boolean shouldDisplayNotifications = sp
                .getBoolean(displayNotificationsKey, shouldDisplayNotificationsByDefault);

        return shouldDisplayNotifications;
    }

    public static void resetOnSwitch(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.remove(PREF_ON_SWITCH);
        editor.apply();
    }
}
