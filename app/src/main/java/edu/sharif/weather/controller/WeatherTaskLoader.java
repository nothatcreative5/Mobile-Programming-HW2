package edu.sharif.weather.controller;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class WeatherTaskLoader extends AsyncTaskLoader<List<Sadegh>> {

    public WeatherTaskLoader(@NonNull Context context) {
        super(context);
    }

    @Nullable
    @Override
    public List<Sadegh> loadInBackground() {
        return null;
    }
}