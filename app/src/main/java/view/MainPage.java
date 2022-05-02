package view;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;

import edu.sharif.weather.R;
import edu.sharif.weather.controller.Sadegh;

public class MainPage extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Sadegh>>, Loader.OnLoadCanceledListener<List<Sadegh>> {

    private static final String sharedPrefFile =
            "edu.sharif.weather";
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);


        sharedPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        boolean mode = sharedPreferences.getBoolean("MODE", false);
        if(mode) {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO);
        }

        Button mainPageBtn = findViewById(R.id.mainPageBtn);
        Button settingButton = findViewById(R.id.settingBtn);

        mainPageBtn.setEnabled(false);

        settingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, SettingPage.class);
                startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public Loader<List<Sadegh>> onCreateLoader(int id, @Nullable Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Sadegh>> loader, List<Sadegh> data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Sadegh>> loader) {

    }

    @Override
    public void onLoadCanceled(@NonNull Loader<List<Sadegh>> loader) {

    }
}