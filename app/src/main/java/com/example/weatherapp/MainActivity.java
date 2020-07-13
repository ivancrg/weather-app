package com.example.weatherapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        bottomNavigationView.setVisibility(View.GONE);

        if (savedInstanceState == null) {
            SplashScreen splashScreen = new SplashScreen();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, splashScreen).commit();

            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                bottomNavigationView.setVisibility(View.VISIBLE);
                MainScreen mainScreen = new MainScreen();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mainScreen).commit();
            }, 2500);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            menuItem -> {
                Fragment selectedFragment = null;

                switch (menuItem.getItemId()) {
                    case R.id.navigation_main:
                        selectedFragment = new MainScreen();
                        break;
                    case R.id.navigation_forecastweek:
                        selectedFragment = new ForecastWeekScreen();
                        break;
                    case R.id.navigation_forecast48:
                        selectedFragment = new Forecast48Screen();
                        break;
                    case R.id.navigation_search:
                        selectedFragment = new SearchScreen();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();

                return true;
            };
}