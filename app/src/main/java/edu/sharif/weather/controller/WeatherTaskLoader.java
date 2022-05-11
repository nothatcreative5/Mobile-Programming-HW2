package edu.sharif.weather.controller;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

import edu.sharif.weather.model.DailyWeather;

public class WeatherTaskLoader extends AsyncTaskLoader<List<DailyWeather>> {

    public WeatherTaskLoader(@NonNull Context context) {
        super(context);
    }

    @Nullable
    @Override
    public List<DailyWeather> loadInBackground() {
        return null;
    }
}
