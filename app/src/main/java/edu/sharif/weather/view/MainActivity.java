package edu.sharif.weather.view;


import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
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

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import edu.sharif.weather.R;
import edu.sharif.weather.controller.WeatherController;
import edu.sharif.weather.model.CitySearchModel;
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
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

        new Thread(() -> {
            JSONObject test = wc.getWeatherByGeoLocation("22","32");
            Log.d("Bug",test.toString());
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

    private ArrayList<CitySearchModel> createSampleData(){
        ArrayList<CitySearchModel> items = new ArrayList<>();
        for (int i = 0; i <= 10000; i++)
            items.add(new CitySearchModel(String.valueOf(i)));
        items.add(new CitySearchModel("Tehran"));
        items.add(new CitySearchModel("Karaj"));
        items.add(new CitySearchModel("Shiraz"));
        items.add(new CitySearchModel("Yazd"));
        items.add(new CitySearchModel("Kerman"));
        items.add(new CitySearchModel("Tabriz"));
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
            for (int i=0; i<childCount; i++) unvisited.add(group.getChildAt(i));
        }
        return visited;
    }

    private void showSearchByCityNameDialog() {
        SimpleSearchDialogCompat simpleSearchDialogCompat = new SimpleSearchDialogCompat(MainActivity.this,
                "Choose City Name", "Enter City Name...", null, createSampleData(),
                (SearchResultListener<CitySearchModel>) (dialog, item, position) -> {
                    String cityName = item.getTitle();
                    // TODO: API handling
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
                child.setBackgroundColor(Color.parseColor("#000000"));
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
        new AlertDialog.Builder(MainActivity.this)
                .setIcon(android.R.drawable.ic_menu_search)
                .setTitle("Search Weather Info")
                .setMessage("Enter city coordinates")
                .setPositiveButton("Search", (dialogInterface, i) -> {
                    // TODO: API handling
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                })
                .show();
    }
}