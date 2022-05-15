package edu.sharif.weather.view;


import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.ContentFrameLayout;
import androidx.appcompat.widget.FitWindowsLinearLayout;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textview.MaterialTextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import edu.sharif.weather.R;
import edu.sharif.weather.controller.WeatherController;
import edu.sharif.weather.model.CitySearchModel;
import edu.sharif.weather.model.DailyWeather;
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

public class MainActivity extends AppCompatActivity {

    private static final String Shared_KEY = "edu.sharif.weather";
    private SharedPreferences sharedPreferences;

    WeatherController wc = new WeatherController(getApplication());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Why", "why");
        sharedPreferences = getSharedPreferences(Shared_KEY, MODE_PRIVATE);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        new Thread(() -> {
            ArrayList<DailyWeather> test = wc.getWeatherByGeoLocation("22", "32");
            Log.d("Bug", test.toString());
        }).start();

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
                    if (item.getItemId() == 1) // TODO: Search by City Name
                        showSearchByCityNameDialog();
                    if (item.getItemId() == 2) // TODO: Search by City Coordinates
                        showSearchByCityCoordinatesDialog();
                    return false;
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<CitySearchModel> createSampleData() {
        ArrayList<CitySearchModel> items = new ArrayList<>();
        AssetManager assets = getAssets();
        try {
            InputStream in = assets.open("city_names.txt");
            LineNumberReader lin = new LineNumberReader(new InputStreamReader(in));
            String line;
            while ((line = lin.readLine()) != null) {
                items.add(new CitySearchModel(line));
            }
        } catch (IOException ignored) {
        }
        return items;
    }

    private List<View> getAllChildrenBFS(View v) {
        List<View> visited = new ArrayList<View>();
        List<View> unvisited = new ArrayList<View>();
        unvisited.add(v);
        while (!unvisited.isEmpty()) {
            View child = unvisited.remove(0);
            visited.add(child);
            if (!(child instanceof ViewGroup)) continue;
            ViewGroup group = (ViewGroup) child;
            final int childCount = group.getChildCount();
            for (int i = 0; i < childCount; i++) unvisited.add(group.getChildAt(i));
        }
        return visited;
    }

    private void showSearchByCityNameDialog() {
        SimpleSearchDialogCompat simpleSearchDialogCompat = new SimpleSearchDialogCompat(MainActivity.this,
                "Choose City Name", "Enter City Name...", null, createSampleData(),
                (SearchResultListener<CitySearchModel>) (dialog, item, position) -> {
                    String cityName = item.getTitle();
                    // TODO: API handling
                    HomeFragment hf = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                    hf.getWeeklyForecastByCityName(cityName);
                    Toast.makeText(MainActivity.this, cityName,
                            Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });
        simpleSearchDialogCompat.show();
        // Handling dark mode for SimpleSearchDialogCompat library
        if (AppCompatDelegate.getDefaultNightMode() == MODE_NIGHT_YES) {
            View view = simpleSearchDialogCompat.getWindow().getDecorView();
            List<View> children = getAllChildrenBFS(view);
            for (View child : children) {
                child.setBackgroundColor(Color.parseColor("#121212"));
                if (child instanceof AppCompatTextView) {
                    AppCompatTextView d = (AppCompatTextView) child;
                    d.setTextColor(Color.parseColor("#ffffff"));
                }
                if (child instanceof RecyclerView) {
                    RecyclerView recyclerView = (RecyclerView) child;
                    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                            for (int i = 0; i < recyclerView.getAdapter().getItemCount(); i++) {
                                View item = recyclerView.getLayoutManager().findViewByPosition(i);
                                if (item != null)
                                    ((MaterialTextView) item).setTextColor(Color.parseColor("#ffffff"));
                            }
                        }
                    });
                    recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                        for (int i = 0; i < recyclerView.getAdapter().getItemCount(); i++) {
                            View item = recyclerView.getLayoutManager().findViewByPosition(i);
                            if (item != null)
                                ((MaterialTextView) item).setTextColor(Color.parseColor("#ffffff"));
                        }
                    });
                }
            }
        }
    }

    private void showSearchByCityCoordinatesDialog() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout layout = new LinearLayout(this);
        layout.setLayoutParams(lp);
        int paddingDp = 20;
        float density = this.getResources().getDisplayMetrics().density;
        int paddingPixel = (int) (paddingDp * density);
        layout.setPadding(paddingPixel, 0, paddingPixel, paddingPixel);
        EditText longitudeEditText = new EditText(this);
        EditText latitudeEditText = new EditText(this);
        longitudeEditText.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        latitudeEditText.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        longitudeEditText.setHint("Longitude");
        latitudeEditText.setHint("Latitude");
        longitudeEditText.setGravity(Gravity.CENTER_HORIZONTAL);
        latitudeEditText.setGravity(Gravity.CENTER_HORIZONTAL);
        longitudeEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        latitudeEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        layout.addView(longitudeEditText);
        layout.addView(latitudeEditText);
        boolean[] searchButtonConditions = {false, false};
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this)
                .setView(layout)
                .setTitle("Search Weather Info")
                .setMessage("Enter city coordinates")
                .setPositiveButton("Search", (dialogInterface, i) -> {
                    double longitude = Double.parseDouble(longitudeEditText.getText().toString());
                    double latitude = Double.parseDouble(latitudeEditText.getText().toString());
                    Toast.makeText(getApplicationContext(), longitude + " " + latitude, Toast.LENGTH_LONG).show();

                    // TODO: API handling
                    HomeFragment hf = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                    hf.getWeeklyForecastByCoordinates(Double.toString(longitude), Double.toString(latitude));
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        longitudeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchButtonConditions[0] = !charSequence.toString().trim().equals("");
                boolean enable = searchButtonConditions[0] && searchButtonConditions[1];
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(enable);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        latitudeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchButtonConditions[1] = !charSequence.toString().trim().equals("");
                boolean enable = searchButtonConditions[0] && searchButtonConditions[1];
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(enable);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}