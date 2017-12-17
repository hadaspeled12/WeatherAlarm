package com.example.hadasp.weather03.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hadasp.weather03.R;
import com.example.hadasp.weather03.data.WeatherItem;

import java.util.ArrayList;


public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private ArrayList<WeatherItem> weathers;
    private Context context;

    public WeatherAdapter(Context context, ArrayList<WeatherItem> weathersList) {
        this.weathers = weathersList;
        this.context = context;
    }

    @Override
    public WeatherViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hit_layout, parent, false);
        TextView timeTextView = view.findViewById(R.id.time);
        WeatherViewHolder weatherViewHolder = new WeatherViewHolder(view, timeTextView);
        return weatherViewHolder;
    }

    @Override
    public void onBindViewHolder (WeatherViewHolder holder, int position) {
        WeatherItem weather = weathers.get(position);

        String dateString = weather.getShortDay();

        String timeString = weather.getTime();

        String descriptionString = weather.getDescription();

        String humidityString = weather.getHumidity();

        String tempString = weather.getTemp();

        String weatherSummary = dateString + "   " + timeString + "   " + tempString + "   " +
                descriptionString + "   " + humidityString;

        holder.weatherTextView.setText(weatherSummary);
        Resources resources = context.getResources();
        holder.weatherTextView.setBackgroundColor(resources.getColor(getColorResurce(descriptionString)));

    }
    private int getColorResurce(String description){
        int colorResurce = 0;
        switch (description){
            case "overcast clouds":
                colorResurce = R.color.clouds04;
                break;
            case "broken clouds":
                colorResurce = R.color.clouds03;
                break;
            case "scattered clouds":
                colorResurce = R.color.clouds02;
                break;
            case "few clouds":
                colorResurce = R.color.clouds01;
                break;
            case "clear sky":
                colorResurce = R.color.clear;
                break;
            case "hail":
                colorResurce = R.color.hail;
                break;
            case "heavy shower snow":
                colorResurce = R.color.snow10;
                break;
            case "shower snow":
                colorResurce = R.color.snow09;
                break;
            case "light shower snow":
                colorResurce = R.color.snow08;
                break;
            case "rain and snow":
                colorResurce = R.color.snow07;
                break;
            case "light rain and snow":
                colorResurce = R.color.snow06;
                break;
            case "shower sleet":
                colorResurce = R.color.snow05;
                break;
            case "sleet":
                colorResurce = R.color.snow04;
                break;
            case "heavy snow":
                colorResurce = R.color.snow03;
                break;
            case "snow":
                colorResurce = R.color.snow02;
                break;
            case "light snow":
                colorResurce = R.color.snow01;
                break;
            case "ragged shower rain":
                colorResurce = R.color.rain10;
                break;
            case "heavy intensity shower rain":
                colorResurce = R.color.rain09;
                break;
            case "shower rain":
                colorResurce = R.color.rain08;
                break;
            case "light intensity shower rain":
                colorResurce = R.color.rain07;
                break;
            case "freezing rain":
                colorResurce = R.color.rain06;
                break;
            case "extreme rain":
                colorResurce = R.color.rain05;
                break;
            case "very heavy rain":
                colorResurce = R.color.rain04;
                break;
            case "heavy intensity rain":
                colorResurce = R.color.rain03;
                break;
            case "moderate rain":
                colorResurce = R.color.rain02;
                break;
            case "light rain":
                colorResurce = R.color.rain01;
                break;
            default:
                colorResurce = R.color.white;
        }
        return colorResurce;
    }

    @Override
    public int getItemCount () {
        return weathers.size();
    }

    class WeatherViewHolder extends RecyclerView.ViewHolder {

        View weatherLayout;
        TextView weatherTextView;

        public WeatherViewHolder (View itemView, TextView textView) {
            super(itemView);
            weatherLayout = itemView;
            weatherTextView = textView;
        }
    }

}
