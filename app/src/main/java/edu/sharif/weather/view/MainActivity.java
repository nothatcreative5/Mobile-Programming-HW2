package edu.sharif.weather.view;


import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import edu.sharif.weather.R;
import edu.sharif.weather.controller.WeatherController;
import edu.sharif.weather.model.CitySearchModel;
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class MainActivity extends AppCompatActivity {

    private static final String Shared_KEY = "edu.sharif.weather";
    private SharedPreferences sharedPreferences;

    WeatherController wc = new WeatherController();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("Why","why");
        sharedPreferences = getSharedPreferences(Shared_KEY, MODE_PRIVATE);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

//        new Thread(() -> {
//            JSONObject test = wc.getWeatherByGeoLocation("22","32");
//            Log.d("Bug",test.toString());
//        }).start();

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
            popMenu.getMenu().add(1, 1, 1, "Search by City Name");
            popMenu.getMenu().add(1, 2, 2, "Search by City Coordinates");
            popMenu.show();
            popMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == 1)
                        showSearchByCityNameDialog();
                    if (item.getItemId() == 2)
                        Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<CitySearchModel> createSampleData(){
        ArrayList<CitySearchModel> items = new ArrayList<>();
        items.add(new CitySearchModel("Tehran"));
        items.add(new CitySearchModel("Karaj"));
        items.add(new CitySearchModel("Shiraz"));
        items.add(new CitySearchModel("Yazd"));
        items.add(new CitySearchModel("Tabriz"));
        return items;
    }

    private void showSearchByCityNameDialog() {
        new SimpleSearchDialogCompat(MainActivity.this, "Choose City Name",
                "Enter City Name...", null, createSampleData(),
                (SearchResultListener<CitySearchModel>) (dialog, item, position) -> {
                    // If filtering is enabled, [position] is the index of the item in the filtered result, not in the unfiltered source
                    Toast.makeText(MainActivity.this, item.getTitle(),
                            Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }).show();

//        new AlertDialog.Builder(MainActivity.this)
//                .setIcon(android.R.drawable.ic_menu_search)
//                .setTitle("Search Weather Info")
//                .setMessage("Enter city name")
//                .setPositiveButton("Search", (dialogInterface, i) -> {
//
//                })
//                .setNegativeButton("Cancel", (dialogInterface, i) -> {
//                    Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
//                })
//                .show();
    }
}