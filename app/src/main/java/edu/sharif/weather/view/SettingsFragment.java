package edu.sharif.weather.view;

import static android.content.Context.MODE_PRIVATE;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.vimalcvs.switchdn.DayNightSwitch;
import com.vimalcvs.switchdn.DayNightSwitchAnimListener;
import com.vimalcvs.switchdn.DayNightSwitchListener;

import edu.sharif.weather.R;

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
        dayNightSwitch.setDuration(400);
        if (AppCompatDelegate.getDefaultNightMode() == MODE_NIGHT_YES)
            dayNightSwitch.setIsNight(true);
        dayNightSwitch.setAnimListener(new DayNightSwitchAnimListener() {
            @Override
            public void onAnimStart() {

            }

            @Override
            public void onAnimEnd() {

            }

            @Override
            public void onAnimValueChanged(float value) {
                if (value == 1) {
                    saveDarkModeState(true);
                    AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
                } else if (value == 0) {
                    saveDarkModeState(false);
                    AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO);
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
