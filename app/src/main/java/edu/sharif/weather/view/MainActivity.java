package edu.sharif.weather.view;


import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.sharif.weather.R;
import edu.sharif.weather.controller.WeatherController;

public class MainActivity extends AppCompatActivity {

    private static final String Shared_KEY = "edu.sharif.weather";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(Shared_KEY, MODE_PRIVATE);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        WeatherController wc = new WeatherController();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = new HomeFragment();
            if (item.getItemId() == R.id.nav_settings)
                selectedFragment = new SettingsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        });

        if (sharedPreferences.getBoolean("DarkMode", false))
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);

        if (sharedPreferences.getBoolean("DarkModeJustChanged", false)) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
            bottomNavigationView.setSelectedItemId(R.id.nav_settings);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
        sharedPreferences.edit().putBoolean("DarkModeJustChanged", false).apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.location_search) {
            View view = findViewById(R.id.location_search);
            PopupMenu popMenu = new PopupMenu(this, view);
            popMenu.getMenu().add(1, 1, 1, "Search by city name");
            popMenu.getMenu().add(1, 2, 2, "Search by city coordinates");
            popMenu.show();
            popMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == 1) //TODO
                        Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT).show();
                    if (item.getItemId() == 2)
                        Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}