package com.example.hadasp.weather03.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by hadasp on 03/12/2017.
 */

public interface WeatherService {

    String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    @GET
    Call<WeatherResult> listRepos(@Url String url);
}
