/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.hadasp.weather03.sync;

import android.content.Context;

import com.example.hadasp.weather03.data.WeatherItem;
import com.example.hadasp.weather03.network.WeatherResult;
import com.example.hadasp.weather03.network.WeatherService;
import com.example.hadasp.weather03.utilities.NotificationUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.hadasp.weather03.MainActivity.urlString;

public class WeatherSyncTask {

    public static List<String> startRain = null;
    public static List<String> endRain = null;


    public static List<String> startSnow = null;
    public static List<String> endSnow = null;


    public static List<String> startHail = null;
    public static List<String> endHail = null;



    /**
     * Performs the network request for updated weather, parses the JSON from that request, and
     * inserts the new weather information into our ContentProvider. Will notify the user that new
     * weather has been loaded if the user hasn't been notified of the weather within the last day
     * AND they haven't disabled notifications in the preferences screen.
     *
     * @param context Used to access utility methods and the ContentResolver
     */
    synchronized public static void syncWeather(final Context context) {


        // retrofit
        Retrofit retrofit;
        WeatherService service;

        try {
            // init retrofit.
            retrofit = new Retrofit.Builder().
                    baseUrl(WeatherService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            service = retrofit.create(WeatherService.class);
            service.listRepos(urlString);

            // create the call
            Call<WeatherResult> weatherSearchResultCall = service.listRepos(urlString);

            // make the call
            weatherSearchResultCall.enqueue(new Callback<WeatherResult>() {
                @Override
                public void onResponse (Call<WeatherResult> call, Response<WeatherResult> response) {
                    // check if call went through and set a notification.
                    if (response.code() == 200) {
                        ArrayList<WeatherItem> weathers = (ArrayList<WeatherItem>) response.body().getWeatherItems();
                        if (weathers != null && weathers.size() != 0) {
                            getExtremeWeatherTime(weathers);

                            if (startRain != null || startSnow != null || startHail != null){
                                NotificationUtils.notifyUserOfNewWeather(context);
                            }

            /* If the code reaches this point, we have successfully performed our sync */

                        }

                    }
                }

                @Override
                public void onFailure (Call<WeatherResult> call, Throwable t) {
                    // HTTP call failed.
                }
            });

        } catch (Exception e) {
            /* Server probably invalid */
            e.printStackTrace();
        }
    }

    private static void getExtremeWeatherTime(ArrayList<WeatherItem> weathers){
        List<Integer> startRainIndex = null;
        List<Integer> endRainIndex = null;
        List<Integer> startSnowIndex = null;
        List<Integer> endSnowIndex = null;
        List<Integer> startHailIndex = null;
        List<Integer> endHailIndex = null;

        for (int i = 0; i< weathers.size(); i++){
            Calendar c = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
            String currentDate = dateFormat.format(c.getTime());
            WeatherItem currentWeather = weathers.get(i);
            if (currentWeather.getDate().equals(currentDate)){
                if (currentWeather.getDescription().contains("snow") ||
                        currentWeather.getDescription().contains("sleet")) {
                    if (startSnowIndex == null) {
                        startSnowIndex.add(i);
                        startSnow.add(currentWeather.getTime());
                        endSnowIndex.add(i);
                        endSnow.add(currentWeather.getTime());
                    } else if (endSnowIndex.get(endSnowIndex.size() - 1) + 1 == i) {
                        endSnowIndex.add(endSnowIndex.size() - 1, i);
                        endSnow.add(endSnowIndex.size() - 1, currentWeather.getTime());
                    } else {
                        startSnowIndex.add(i);
                        startSnow.add(currentWeather.getTime());
                        endSnowIndex.add(i);
                        endSnow.add(currentWeather.getTime());
                    }
                }else if (currentWeather.getDescription().contains("rain")) {
                    if (startRainIndex == null) {
                        startRainIndex.add(i);
                        startRain.add(currentWeather.getTime());
                        endRainIndex.add(i);
                        endRain.add(currentWeather.getTime());
                    } else if (endRainIndex.get(endRainIndex.size() - 1) + 1 == i) {
                        endRainIndex.add(endRainIndex.size() - 1, i);
                        endRain.add(endRainIndex.size() - 1, currentWeather.getTime());
                    } else {
                        startRainIndex.add(i);
                        startRain.add(currentWeather.getTime());
                        endRainIndex.add(i);
                        endRain.add(currentWeather.getTime());
                    }
                } else if (currentWeather.getDescription().contains("hail")){
                    if (startHailIndex == null) {
                        startHailIndex.add(i);
                        startHail.add(currentWeather.getTime());
                        endHailIndex.add(i);
                        endHail.add(currentWeather.getTime());
                    } else if (endHailIndex.get(endHailIndex.size() - 1) + 1 == i) {
                        endHailIndex.add(endHailIndex.size() - 1, i);
                        endHail.add(endHailIndex.size() - 1, currentWeather.getTime());
                    } else {
                        startHailIndex.add(i);
                        startHail.add(currentWeather.getTime());
                        endHailIndex.add(i);
                        endHail.add(currentWeather.getTime());
                    }
                }
            }

        }
    }
}