package view;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import edu.sharif.weather.R;

public class SettingPage extends AppCompatActivity {

    private static final String sharedPrefFile =
            "edu.sharif.weather";

    private Button mainPageBtn;
    private Button settingButton;
    private Switch themeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);

        mainPageBtn = findViewById(R.id.mainPageBtn);
        settingButton = findViewById(R.id.settingBtn);
        themeSwitch = findViewById(R.id.themeSwitch);

        themeSwitch.setChecked(AppCompatDelegate.getDefaultNightMode() == MODE_NIGHT_YES);

        settingButton.setEnabled(false);

        mainPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
                    themeSwitch.setText("Dark Mode");
                }else{
                    AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO);
                    themeSwitch.setText("Light Mode");
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor preferencesEditor = getSharedPreferences(sharedPrefFile, MODE_PRIVATE).edit();
        preferencesEditor.putBoolean("MODE", themeSwitch.isChecked());
        preferencesEditor.apply();
    }
}