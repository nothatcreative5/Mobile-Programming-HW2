package edu.sharif.weather.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Random;


import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import edu.sharif.weather.R;
import edu.sharif.weather.controller.DailyWeather;


import java.util.ArrayList;

public class WeatherRecyclerAdapter extends RecyclerView.Adapter<WeatherRecyclerAdapter.ViewHolder> {

    private ArrayList<DailyWeather> weatherForcast;
    private OnWeatherListener mOnWeatherListener;


    public WeatherRecyclerAdapter(ArrayList<DailyWeather> weatherForcast, OnWeatherListener onWeatherListener) {
        this.weatherForcast = weatherForcast;
        this.mOnWeatherListener = onWeatherListener;
    }

    public void changeDataSet(ArrayList<DailyWeather> weatherForcast) {
        this.weatherForcast = weatherForcast;
    }

    @NonNull
    @Override
    public WeatherRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_list, parent, false);
        return new ViewHolder(view, mOnWeatherListener);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherRecyclerAdapter.ViewHolder holder, int position) {

        Random rand = new Random();
        int n = rand.nextInt(50);

        holder.text1.setText(Integer.toString(n));
        holder.text2.setText(Integer.toString(n + 2));
    }

    @Override
    public int getItemCount() {
        return weatherForcast.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView text1;
        TextView text2;
        ConstraintLayout parentLayout;
        OnWeatherListener onWeatherListener;

        public ViewHolder(@NonNull View itemView, OnWeatherListener onWeatherListener) {
            super(itemView);
            text1 = itemView.findViewById(R.id.text1);
            text2 = itemView.findViewById(R.id.text2);
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
