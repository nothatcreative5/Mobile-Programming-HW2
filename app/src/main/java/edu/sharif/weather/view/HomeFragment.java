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

        wc = WeatherController.getControllerInstance();

        recyclerView = view.findViewById(R.id.weatherRecycler);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new CenterScrollListener());
        cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        mWeatherForecast = new ArrayList<>();


        adapter = new WeatherRecyclerAdapter(mWeatherForecast, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    public void getWeeklyForecastByCityName(String cityName) {

        final ProgressDialog[] dialog = new ProgressDialog[1];

        getActivity().runOnUiThread(() -> dialog[0] =  ProgressDialog.show(getActivity(), "Searching Weather Info", "Please wait...", true));
        new Thread(() -> {
            mWeatherForecast = wc.getWeatherByLocationName(cityName);
            if (mWeatherForecast == null)
                onFailure(dialog[0]);
            else
                onSuccess(cityName, dialog[0]);
        }).start();
    }


    public void getWeeklyForecastByCityName(String cityName, boolean incomplete) {
        final ProgressDialog[] dialog = new ProgressDialog[1];
//        getActivity().runOnUiThread(() ->  dialog[0] =  ProgressDialog.show(getActivity(), "Searching Weather Info", "Please wait...", true));
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog[0] = ProgressDialog.show(getActivity(), "Searching Weather Info", "Please wait...", true);
                Log.d("test",dialog[0].toString());
            }
        });

        new Thread(() -> {
            mWeatherForecast = wc.getWeatherByLocationName(cityName);
            String completeName = wc.getGeoLocation(cityName).get("loc");
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog[0].dismiss();
                }
            });
            if (mWeatherForecast == null)
                onFailure();
            else {
                onSuccess(completeName);
            }
        }).start();
    }

    public void getWeeklyForecastByCoordinates(String longitude, String latitude) {
        final ProgressDialog[] dialog = new ProgressDialog[1];
        getActivity().runOnUiThread(() -> dialog[0] = ProgressDialog.show(getActivity(), "Searching Weather Info", "Please wait...", true));
        new Thread(() -> {
            String cityName = wc.getCityName(longitude, latitude);
            mWeatherForecast = wc.getWeatherByGeoLocation(latitude, longitude);
            if (cityName == null) {
                onFailure(dialog[0], "We couldn't find a city with these coordinates. Please try another location.");
            } else if (mWeatherForecast == null) {
                onFailure(dialog[0]);
            } else {
                onSuccess(cityName, dialog[0]);
            }
        }).start();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void onSuccess(String cityName,@NonNull ProgressDialog dialog) {
        for (DailyWeather dw : mWeatherForecast) {
            dw.setCityName(cityName);
        }
        getActivity().runOnUiThread(() -> {
            Log.d("test","we do a little trolling");
            dialog.dismiss();
            adapter.changeDataSet(mWeatherForecast);
            adapter.notifyDataSetChanged();
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.scrollToPosition(0);
        });
    }


    @SuppressLint("NotifyDataSetChanged")
    public void onSuccess(String cityName) {
        for (DailyWeather dw : mWeatherForecast) {
            dw.setCityName(cityName);
        }
        getActivity().runOnUiThread(() -> {
            Log.d("test","we do a little trolling");
            adapter.changeDataSet(mWeatherForecast);
            adapter.notifyDataSetChanged();
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.scrollToPosition(0);
        });
    }



    public void onFailure(ProgressDialog dialog) {
        getActivity().runOnUiThread(() -> {
            dialog.dismiss();
            if (cm.getActiveNetworkInfo() != null) {
                Toast.makeText(getActivity(), "A problem occurred. Please try again."
                        , Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "You don't have an active internet Connection." +
                        "Please try again later.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onFailure() {
        getActivity().runOnUiThread(() -> {
            if (cm.getActiveNetworkInfo() != null) {
                Toast.makeText(getActivity(), "A problem occurred. Please try again."
                        , Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "You don't have an active internet Connection." +
                        "Please try again later.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onFailure(ProgressDialog dialog, String message) {
        getActivity().runOnUiThread(() -> {
            if(dialog != null)
                dialog.dismiss();
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public void onWeatherClick(int position) {
    }
}
