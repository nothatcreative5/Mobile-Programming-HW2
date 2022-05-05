package edu.sharif.weather.view;

import static android.content.Context.MODE_PRIVATE;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import edu.sharif.weather.R;
import ma.apps.widgets.daynightswitch.DayNightSwitch;
import ma.apps.widgets.daynightswitch.OnSwitchListener;

public class SettingsFragment extends Fragment {

    private static final String Shared_KEY = "edu.sharif.weather";
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = this.getActivity().getSharedPreferences(Shared_KEY, MODE_PRIVATE);
        DayNightSwitch dayNightSwitch = (DayNightSwitch) view.findViewById(R.id.themeSwitch);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

        boolean isNight = AppCompatDelegate.getDefaultNightMode() == MODE_NIGHT_YES;
        imageView.setBackgroundResource(isNight ? R.drawable.ic_moon : R.drawable.ic_sun);
        dayNightSwitch.setDayChecked(!isNight, false);
        dayNightSwitch.setOnSwitchListener(new OnSwitchListener() {
            @Override
            public void onSwitch(@NonNull DayNightSwitch dayNightSwitch, boolean isDay) {
                if (isDay) {
                    saveDarkModeState(false);
                    AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO);
                } else {
                    saveDarkModeState(true);
                    AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
                }
            }
        });
    }

    private void saveDarkModeState(boolean state) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("DarkMode", state);
        editor.putBoolean("DarkModeJustChanged", true);
        editor.apply();
    }
}
