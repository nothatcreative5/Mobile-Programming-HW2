package edu.sharif.weather.view;

import static android.content.Context.MODE_PRIVATE;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

        SwitchCompat themeSwitch = (SwitchCompat) view.findViewById(R.id.themeSwitch);
        if (AppCompatDelegate.getDefaultNightMode() == MODE_NIGHT_YES) {
            themeSwitch.setChecked(true);
            themeSwitch.setText("Dark Mode");
        }
        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Toast toast = Toast.makeText(getContext(), "Dark", Toast.LENGTH_SHORT);
                    toast.show();
                    saveDarkModeState(true);
                    AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
                } else {
                    Toast toast = Toast.makeText(getContext(), "Light", Toast.LENGTH_SHORT);
                    toast.show();
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
