package edu.sharif.weather.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import edu.sharif.weather.controller.DailyWeather;

public class HomeFragment extends Fragment implements WeatherRecyclerAdapter.OnWeatherListener {


    private RecyclerView recyclerView;
    private ArrayList<DailyWeather> mWeatherForecast;
    private WeatherRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL,true);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());


        recyclerView = view.findViewById(R.id.weatherRecycler);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new CenterScrollListener());

        mWeatherForecast = new ArrayList<>();

        mWeatherForecast.add(new DailyWeather(1,1,1,1));
        mWeatherForecast.add(new DailyWeather(2,2,2,2));
        mWeatherForecast.add(new DailyWeather(1,1,1,1));
        mWeatherForecast.add(new DailyWeather(2,2,2,2));


        adapter = new WeatherRecyclerAdapter(mWeatherForecast,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onWeatherClick(int position) {
        Log.d("test","someone clicked on my pussy.");
    }
}
