package edu.sharif.weather.adapters;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import edu.sharif.weather.R;
import edu.sharif.weather.model.DailyWeather;


import java.util.ArrayList;

public class WeatherRecyclerAdapter extends RecyclerView.Adapter<WeatherRecyclerAdapter.ViewHolder> {

    private ArrayList<DailyWeather> weatherForecast;
    private final OnWeatherListener mOnWeatherListener;


    public WeatherRecyclerAdapter(ArrayList<DailyWeather> weatherForecast, OnWeatherListener onWeatherListener) {
        this.weatherForecast = weatherForecast;
        this.mOnWeatherListener = onWeatherListener;
    }

    public void changeDataSet(ArrayList<DailyWeather> weatherForecast) {
        this.weatherForecast = weatherForecast;
    }

    @NonNull
    @Override
    public WeatherRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_list, parent, false);
        return new ViewHolder(view, mOnWeatherListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WeatherRecyclerAdapter.ViewHolder holder, int position) {

        holder.cityName.setText(weatherForecast.get(position).getCityName());

        String dateString = position + " days later";
        if (position == 0)
            dateString = "Today";
        else if (position == 1)
            dateString = "Tomorrow";
        holder.date.setText(dateString);
        holder.temperature.setText(weatherForecast.get(position).getTemp() + "\u00B0");

        holder.feelsLike.setText(weatherForecast.get(position).getFeelsLike() + "\u00B0");

        holder.humidity.setText(weatherForecast.get(position).getHumidity() + "%");

    }

    @Override
    public int getItemCount() {
        return weatherForecast.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView temperature;
        TextView feelsLike;
        TextView cityName;
        TextView date;
        TextView humidity;
        ImageView weatherIcon;
        ConstraintLayout parentLayout;
        OnWeatherListener onWeatherListener;

        public ViewHolder(@NonNull View itemView, OnWeatherListener onWeatherListener) {
            super(itemView);
            temperature = itemView.findViewById(R.id.temperature);
            feelsLike = itemView.findViewById(R.id.feelsLike);
            humidity = itemView.findViewById(R.id.humidity);
            cityName = itemView.findViewById(R.id.cityName);
            date = itemView.findViewById(R.id.dateTextView);
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
