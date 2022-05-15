package edu.sharif.weather.view;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.mig35.carousellayoutmanager.CarouselLayoutManager;
import com.mig35.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.mig35.carousellayoutmanager.CenterScrollListener;

import java.util.ArrayList;

import edu.sharif.weather.R;
import edu.sharif.weather.adapters.WeatherRecyclerAdapter;
import edu.sharif.weather.controller.WeatherController;
import edu.sharif.weather.model.DailyWeather;

public class HomeFragment extends Fragment implements WeatherRecyclerAdapter.OnWeatherListener {


    private RecyclerView recyclerView;
    private ArrayList<DailyWeather> mWeatherForecast;
    private WeatherController wc;
    private WeatherRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());

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

        ProgressDialog dialog = ProgressDialog.show(getActivity(), "Searching Weather Info", "Please wait...", true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mWeatherForecast = wc.getWeatherByLocationName(cityName);
                if (mWeatherForecast == null)
                    onFailure(dialog);
                else
                    onSuccess(cityName, dialog);
            }
        }).start();
    }

    public void getWeeklyForecastByCoordinates(String longitude, String latitude) {
        ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
                "Loading. Please wait...", true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mWeatherForecast = wc.getWeatherByGeoLocation(latitude, longitude);
                String cityName = wc.getCityName(longitude, latitude);
                if (cityName == null)
                    cityName = "Karaj";
                if (mWeatherForecast == null)
                    onFailure(dialog);
                else
                    onSuccess(cityName, dialog);
            }
        }).start();
    }

    public void onSuccess(String cityName, ProgressDialog dialog) {

        for (DailyWeather dw : mWeatherForecast) {
            dw.setCityName(cityName);
        }
        getActivity().runOnUiThread(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                dialog.dismiss();
                adapter.changeDataSet(mWeatherForecast);
                adapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.scrollToPosition(0);
            }
        });
    }


    public void onFailure(ProgressDialog dialog) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                Toast.makeText(getActivity(), "Ridim seyed",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onWeatherClick(int position) {
        Log.d("test", "someone clicked on my pussy.");
    }
}
