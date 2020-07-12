package com.example.weatherapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    //NAVIGATION VIEW private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        NAVIGATION VIEW

        Toolbar toolbar = findViewById(R.id.navigation_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.navigation_drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();*/

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

    /*
    NAVIGATION VIEW

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_main:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainScreen()).commit();
                break;
            case R.id.navigation_forecastweek:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ForecastWeekScreen()).commit();
                break;
            case R.id.navigation_forecast48:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Forecast48Screen()).commit();
                break;
            case R.id.navigation_search:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchScreen()).commit();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }*/

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