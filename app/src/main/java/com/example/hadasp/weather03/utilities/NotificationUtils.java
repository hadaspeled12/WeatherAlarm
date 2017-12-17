package com.example.hadasp.weather03.utilities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;

import com.example.hadasp.weather03.MainActivity;
import com.example.hadasp.weather03.R;

import java.util.List;

import static com.example.hadasp.weather03.sync.WeatherSyncTask.endHail;
import static com.example.hadasp.weather03.sync.WeatherSyncTask.endRain;
import static com.example.hadasp.weather03.sync.WeatherSyncTask.endSnow;
import static com.example.hadasp.weather03.sync.WeatherSyncTask.startHail;
import static com.example.hadasp.weather03.sync.WeatherSyncTask.startRain;
import static com.example.hadasp.weather03.sync.WeatherSyncTask.startSnow;

public class NotificationUtils {

    /*
     * This notification ID can be used to access our notification after we've displayed it. This
     * can be handy when we need to cancel the notification, or perhaps update it. This number is
     * arbitrary and can be set to whatever you like. 3004 is in no way significant.
     */
    private static final int WEATHER_NOTIFICATION_ID = 3004;

    /**
     * Constructs and displays a notification for the newly updated weather for today.
     *
     * @param context Context used to query our ContentProvider and use various Utility methods
     */
    public static void notifyUserOfNewWeather(Context context) {

            String notificationTitle = "WEATHER";
            String notificationRainText = "";
            String notificationSnowText = "";
            String notificationHailText = "";

            if (startRain != null){
                notificationRainText = getNotificationText(context, startRain, endRain);
            }
            if (startHail != null){
                notificationHailText = getNotificationText(context, startHail, endHail);
            }
            if (startSnow != null){
                notificationSnowText = getNotificationText(context, startSnow, endSnow);
            }

            String notificationText = getNotificationText(
                    context, notificationRainText, notificationHailText, notificationSnowText);

            /*
             * NotificationCompat Builder is a very convenient way to build backward-compatible
             * notifications. In order to use it, we provide a context and specify a color for the
             * notification, a couple of different icons, the title for the notification, and
             * finally the text of the notification, which in our case in a summary of today's
             * forecast.
             */
            NotificationCompat.Builder notificationBuilder = new NotificationCompat
                    .Builder(context, String.valueOf(WEATHER_NOTIFICATION_ID))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationText)
                    .setAutoCancel(true);

            /*
             * This Intent will be triggered when the user clicks the notification. In our case,
             * we want to open Sunshine to the DetailActivity to display the newly updated weather.
             */
            Intent detailIntentForToday = new Intent(context, MainActivity.class);

            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
            taskStackBuilder.addNextIntentWithParentStack(detailIntentForToday);
            PendingIntent resultPendingIntent = taskStackBuilder
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

//          COMPLETED (5) Set the content Intent of the NotificationBuilder
            notificationBuilder.setContentIntent(resultPendingIntent);

//          COMPLETED (6) Get a reference to the NotificationManager
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);

//          COMPLETED (7) Notify the user with the ID WEATHER_NOTIFICATION_ID
            /* WEATHER_NOTIFICATION_ID allows you to update or cancel the notification later on */
            notificationManager.notify(WEATHER_NOTIFICATION_ID, notificationBuilder.build());

        }

    private static String getNotificationText(Context context,
                                              String notificationRainText,
                                              String notificationHailText,
                                              String notificationSnowText) {

        String notificationFormat = context.getString(R.string.format_notification);

        /* Using String's format method, we create the forecast summary */
        String notificationText = String.format(notificationFormat,
                notificationRainText,
                notificationSnowText,
                notificationHailText);

        return notificationText;

    }

    private static String getNotificationText(Context context, List<String> startRain, List<String> endRain) {
        String shortDescription = "";
        for (int i=0; i<startRain.size();i++) {
            if (startRain.get(i).equals(endRain.get(i))) {
                if (i==0) {
                    shortDescription = startRain.get(i);
                } else {
                    shortDescription = shortDescription + " , " + startRain.get(i);
                }
            } else {
                if (i==0) {
                    shortDescription = startRain.get(i) + "-" + endRain.get(i);
                } else {
                    shortDescription = shortDescription + " , " + startRain.get(i) + "-" + endRain.get(i);
                }
            }
        }

        return shortDescription;
    }
}
