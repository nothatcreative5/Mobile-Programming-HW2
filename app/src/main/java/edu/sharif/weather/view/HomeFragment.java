package edu.sharif.weather.view;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.mig35.carousellayoutmanager.CarouselLayoutManager;
import com.mig35.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.mig35.carousellayoutmanager.CenterScrollListener;

import java.util.ArrayList;
import java.util.HashMap;

import edu.sharif.weather.R;
import edu.sharif.weather.adapters.WeatherRecyclerAdapter;
import edu.sharif.weather.controller.WeatherController;
import edu.sharif.weather.model.DailyWeather;

public class HomeFragment extends Fragment implements WeatherRecyclerAdapter.OnWeatherListener {


    private RecyclerView recyclerView;
    private ArrayList<DailyWeather> mWeatherForecast;
    private WeatherController wc;
    private WeatherRecyclerAdapter adapter;
    private ConnectivityManager cm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());

        cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        wc = WeatherController.getControllerInstance();

        recyclerView = view.findViewById(R.id.weatherRecycler);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new CenterScrollListener());

        mWeatherForecast = new ArrayList<>();
        adapter = new WeatherRecyclerAdapter(mWeatherForecast, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    public void getWeeklyForecastByCityName(String cityName) {
        final ProgressDialog[] dialog = new ProgressDialog[1];
        getActivity().runOnUiThread(() -> {
            dialog[0] = ProgressDialog.show(getActivity(), "Searching Weather Info", "Please wait...", true);
        });

        new Thread(() -> {
            mWeatherForecast = wc.getWeatherByLocationName(cityName);
            getActivity().runOnUiThread(() -> dialog[0].dismiss());
            if (mWeatherForecast == null) {
                onFailure();
            } else {
                String originalCityName = wc.getGeoLocation(cityName).get("loc");
                onSuccess(originalCityName);
            }
        }).start();
    }

    public void getWeeklyForecastByCoordinates(String longitude, String latitude) {
        final ProgressDialog[] dialog = new ProgressDialog[1];
        getActivity().runOnUiThread(() -> dialog[0] = ProgressDialog.show(getActivity(), "Searching Weather Info", "Please wait...", true));
        new Thread(() -> {
            String cityName = wc.getCityName(longitude, latitude);
            mWeatherForecast = wc.getWeatherByGeoLocation(latitude, longitude);
            getActivity().runOnUiThread(() -> dialog[0].dismiss());
            if (cityName == null) {
                onFailure("We couldn't find a city with these coordinates. Please try another location.");
            } else if (mWeatherForecast == null) {
                onFailure();
            } else {
                onSuccess(cityName);
            }
        }).start();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void onSuccess(String cityName) {
        for (DailyWeather dw : mWeatherForecast) {
            dw.setCityName(cityName);
        }
        getActivity().runOnUiThread(() -> {
            adapter.changeDataSet(mWeatherForecast);
            adapter.notifyDataSetChanged();
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.scrollToPosition(0);
        });
    }

    public void onFailure(String message) {
        getActivity().runOnUiThread(() -> {
            if (cm.getActiveNetworkInfo() != null) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "You don't have an active internet Connection. Please try again later.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onFailure() {
        onFailure("A problem occurred. Please try again.");
    }

    @Override
    public void onWeatherClick(int position) {
    }
}
