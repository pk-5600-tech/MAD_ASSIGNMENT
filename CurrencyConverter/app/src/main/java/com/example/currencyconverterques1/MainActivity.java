package com.example.currencyconverterques1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 1. Load theme preference BEFORE super.onCreate
        SharedPreferences prefs = getSharedPreferences("theme_prefs", Context.MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("is_dark_mode", false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText etAmount = findViewById(R.id.etAmount);
        Spinner spFrom = findViewById(R.id.spFrom);
        Spinner spTo = findViewById(R.id.spTo);
        Button btnConvert = findViewById(R.id.btnConvert);
        Button btnSettings = findViewById(R.id.btnSettings);
        TextView tvResult = findViewById(R.id.tvResult);

        String[] cur = {"INR", "USD", "EUR", "JPY"};
        ArrayAdapter<String> ad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cur);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spFrom.setAdapter(ad);
        spTo.setAdapter(ad);

        btnSettings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));

        btnConvert.setOnClickListener(v -> {
            String val = etAmount.getText().toString().trim();
            if (val.isEmpty()) {
                Toast.makeText(this, "Enter amount", Toast.LENGTH_SHORT).show();
                return;
            }

            double amt = Double.parseDouble(val);
            String from = spFrom.getSelectedItem().toString();
            String to = spTo.getSelectedItem().toString();

            // Exchange rates (1 USD = X)
            double inr = 93.30, eur = 0.92, jpy = 151.50;
            double toUsd;

            if (from.equals("INR")) toUsd = amt / inr;
            else if (from.equals("EUR")) toUsd = amt / eur;
            else if (from.equals("JPY")) toUsd = amt / jpy;
            else toUsd = amt;

            double res;
            if (to.equals("INR")) res = toUsd * inr;
            else if (to.equals("EUR")) res = toUsd * eur;
            else if (to.equals("JPY")) res = toUsd * jpy;
            else res = toUsd;

            tvResult.setText(String.format(Locale.getDefault(), "Result: %.2f %s", res, to));
        });
    }
}