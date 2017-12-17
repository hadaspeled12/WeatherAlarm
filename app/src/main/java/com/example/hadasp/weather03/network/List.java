package com.example.hadasp.weather03.network;

import com.example.hadasp.weather03.data.WeatherItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hadasp on 30/11/2017.
 */

public class List {

    @SerializedName("dt")
    @Expose
    private Integer dt;
    @SerializedName("main")
    @Expose
    private Main main;
    @SerializedName("weather")
    @Expose
    private java.util.List<Weather> weather = null;

    public Integer getDt() {
        return dt;
    }

    public void setDt(Integer dt) {
        this.dt = dt;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public java.util.List<Weather> getWeather() {
        return weather;
    }

    public List getList(){return this;}

    public WeatherItem getWeatherItem() {
        return new WeatherItem( getWeather().get(0).getDescription(),
                getMain().getTemp(),
                getDt(),
                getMain().getHumidity().toString());

    }

    public void setWeather(java.util.List<Weather> weather) {
        this.weather = weather;
    }


}
