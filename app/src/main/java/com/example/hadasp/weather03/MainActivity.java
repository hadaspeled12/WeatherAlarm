package com.example.hadasp.weather03;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.hadasp.weather03.data.WeatherItem;
import com.example.hadasp.weather03.network.WeatherResult;
import com.example.hadasp.weather03.network.WeatherService;
import com.example.hadasp.weather03.utilities.WeatherAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    // retrofit
    private Retrofit retrofit;
    private WeatherService service;
    public static AlarmManager alarmManager;
    public static PendingIntent pendingIntent;

    /** Tag for log messages */
    private static final String LOG_TAG = MainActivity.class.getName();

    public static String urlString = "http://api.openweathermap.org/data/2.5/forecast?id=293396&APPID=fb2b6a8af170b708d8cc36292838bd6c";

    // views
    private WeatherAdapter weatherAdapter;
    private RecyclerView weatherRecyclerView;
    private TextView weatherDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // init views
        weatherRecyclerView = findViewById(R.id.hits_recycler_view);
        weatherDescription = findViewById(R.id.hits_description);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        weatherRecyclerView.setLayoutManager(mLayoutManager);

        //weatherAdapter = new WeatherAdapter(this, null);
        //weatherRecyclerView.setAdapter(weatherAdapter);
        // init retrofit.
        retrofit = new Retrofit.Builder().
                baseUrl(WeatherService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(WeatherService.class);
        service.listRepos(urlString);
        listReposs();
        //WeatherSyncUtils.initialize(this);
    }

    public void listReposs() {

        Log.e(LOG_TAG, "start listReposs");
        weatherDescription.setText("searching...");

        // create the call
        Call<WeatherResult> weatherSearchResultCall = service.listRepos(urlString);
        Log.e(LOG_TAG, "create the call");

        // make the call
        weatherSearchResultCall.enqueue(new Callback<WeatherResult>() {
            @Override
            public void onResponse (Call<WeatherResult> call, Response<WeatherResult> response) {
                Log.e(LOG_TAG, "make the call - onResponse");
                // check if call went through and update the UI accordingly.
                if (response.code() == 200) {
                    Log.e(LOG_TAG, "make the call - onResponse " + response.code());
                    ArrayList<WeatherItem> weathers = (ArrayList<WeatherItem>) response.body().getWeatherItems();
                    Log.e(LOG_TAG, "make the call - onResponse - weathers is ready ");
                    Log.e(LOG_TAG, "date" + weathers.get(0).getDate());
                    weatherAdapter = new WeatherAdapter(getBaseContext(), weathers);
                    Log.e(LOG_TAG, "make the call - onResponse - Adapter is ready ");
                    weatherRecyclerView.setAdapter(weatherAdapter);
                    Log.e(LOG_TAG, "make the call - onResponse - RecyclerView is set ");
                    weatherDescription.setVisibility(View.GONE);
                } else {
                    weatherDescription.setText(String.format("error with code", response.code()));
                }

            }

            @Override
            public void onFailure (Call<WeatherResult> call, Throwable t) {
                // HTTP call failed, show something to the user.
                weatherDescription.setText(R.string.something_went_wrong);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "set alarm" menu option
            //case R.id.action_set_alarm:
                //setAlarm();
            //    return true;
            // Respond to a click on the "Edit alarm" menu option
            case R.id.action_edit_alarm:
                startActivity(new Intent(this, EditAlarmActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
