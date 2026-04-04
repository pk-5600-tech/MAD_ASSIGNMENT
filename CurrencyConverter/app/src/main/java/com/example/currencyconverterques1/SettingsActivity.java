package com.example.currencyconverterques1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SwitchCompat themeSwitch = findViewById(R.id.themeSwitch);
        SharedPreferences prefs = getSharedPreferences("theme_prefs", Context.MODE_PRIVATE);

        // Set switch to current saved state
        boolean isDarkMode = prefs.getBoolean("is_dark_mode", false);
        themeSwitch.setChecked(isDarkMode);

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save choice
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("is_dark_mode", isChecked);
            editor.apply();

            // Apply immediately
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
    }
}