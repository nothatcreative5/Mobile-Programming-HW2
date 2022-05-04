package edu.sharif.weather.view;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.sharif.weather.R;

public class MainActivity extends AppCompatActivity {

    private static String Shared_KEY = "edu.sharif.weather";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor myEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         sharedPreferences = getSharedPreferences(Shared_KEY, MODE_PRIVATE);
         myEdit = sharedPreferences.edit();

         Log.d("Iman", String.valueOf(sharedPreferences.getAll()));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (sharedPreferences.getBoolean("Setting", false)) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
            bottomNavigationView.setSelectedItemId(R.id.nav_settings);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = new HomeFragment();
            myEdit.putBoolean("Setting", false);
            if (item.getItemId() == R.id.nav_settings) {
                selectedFragment = new SettingsFragment();
                myEdit.putBoolean("Setting", true);
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Iman","onStop");
        Log.d("Iman",String.valueOf(sharedPreferences.getAll()));
        if (sharedPreferences.getBoolean("Change", false)) {
            myEdit.putBoolean("Change", false);
        } else {
            myEdit.putBoolean("Setting", false);
        }
        Log.d("Iman",String.valueOf(sharedPreferences.getAll()));
        myEdit.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}