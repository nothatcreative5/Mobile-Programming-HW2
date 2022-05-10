package edu.sharif.weather.adapters;


import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.transition.Hold;

import edu.sharif.weather.R;
import edu.sharif.weather.controller.DailyWeather;


import java.util.ArrayList;

public class WeatherRecyclerAdapter extends RecyclerView.Adapter<WeatherRecyclerAdapter.ViewHolder> {

    private ArrayList<DailyWeather> weatherForecast;
    private OnWeatherListener mOnWeatherListener;


    public WeatherRecyclerAdapter(ArrayList<DailyWeather> weatherForcast, OnWeatherListener onWeatherListener) {
        this.weatherForecast = weatherForcast;
        this.mOnWeatherListener = onWeatherListener;
    }

    public void changeDataSet(ArrayList<DailyWeather> weatherForcast) {
        this.weatherForecast = weatherForcast;
    }

    @NonNull
    @Override
    public WeatherRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_list, parent, false);
        return new ViewHolder(view, mOnWeatherListener);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherRecyclerAdapter.ViewHolder holder, int position) {

        holder.parentLayout.setBackgroundColor(0xFF42ecf5);

        holder.cityName.setText("Tehran");
        holder.cityName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);

        holder.temperature.setText("20" + "\u00B0");
        holder.temperature.setTextSize(TypedValue.COMPLEX_UNIT_SP, 23);

        holder.feelsLike.setText("23" + " \u00B0");
        holder.feelsLike.setTextSize(TypedValue.COMPLEX_UNIT_SP, 23);
    }

    @Override
    public int getItemCount() {
        return weatherForecast.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView temperature;
        TextView feelsLike;
        TextView cityName;
        ImageView weatherIcon;
        ConstraintLayout parentLayout;
        OnWeatherListener onWeatherListener;

        public ViewHolder(@NonNull View itemView, OnWeatherListener onWeatherListener) {
            super(itemView);
            temperature = itemView.findViewById(R.id.temperature);
            feelsLike = itemView.findViewById(R.id.feelsLike);
            cityName = itemView.findViewById(R.id.cityName);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            this.onWeatherListener = onWeatherListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onWeatherListener.onWeatherClick(getAdapterPosition());
        }
    }


    public interface OnWeatherListener {
        void onWeatherClick(int position);
    }


}
