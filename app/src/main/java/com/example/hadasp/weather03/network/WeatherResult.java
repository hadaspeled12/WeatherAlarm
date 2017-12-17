package com.example.hadasp.weather03.network;

import com.example.hadasp.weather03.data.WeatherItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by hadasp on 04/12/2017.
 */

public class WeatherResult {
    @SerializedName("list")
    @Expose
    private java.util.List<List> weathers;

    public java.util.List<WeatherItem> getWeatherItems() {
        java.util.List<WeatherItem> weatherItems = new ArrayList<>();
        for (int i = 0; i < weathers.size(); i++){
            weatherItems.add(weathers.get(i).getWeatherItem());
        }
        return weatherItems;
    }
    public void setWeathers(java.util.List<List> weathers) {
        this.weathers = weathers;
    }

}

