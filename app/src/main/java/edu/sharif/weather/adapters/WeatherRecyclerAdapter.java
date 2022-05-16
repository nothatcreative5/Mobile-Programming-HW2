package edu.sharif.weather.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.ArrayList;

import edu.sharif.weather.R;
import edu.sharif.weather.model.DailyWeather;

public class WeatherRecyclerAdapter extends RecyclerView.Adapter<WeatherRecyclerAdapter.ViewHolder> {

    private ArrayList<DailyWeather> weatherForecast;
    private final OnWeatherListener mOnWeatherListener;
    private Context context;


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
        context = parent.getContext();
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

        try {
            String iconCode = weatherForecast.get(position).getIcon();
            InputStream inputStream = context.getAssets().open(iconCode + ".png");
            Drawable d = Drawable.createFromStream(inputStream, null);
            holder.weatherIcon.setImageDrawable(d);
        }
        catch(Exception ignored) {
        }
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
            weatherIcon = itemView.findViewById(R.id.weatherIcon);
            this.onWeatherListener = onWeatherListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onWeatherListener.onWeatherClick(getAdapterPosition());
            int position = getAdapterPosition();
            double windSpeed = weatherForecast.get(position).getWindSpeed();
            String description = weatherForecast.get(position).getDescription();
            String windSpeedInfo = "⦿ Wind speed: " + windSpeed + " km/h";
            String descriptionInfo = "⦿ Description: " + description;
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                    .setTitle("More Info")
                    .setMessage(descriptionInfo + "\n" + windSpeedInfo);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }


    public interface OnWeatherListener {
        void onWeatherClick(int position);
    }

}
