package com.example.weatherapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.navigation_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.navigation_drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            SplashScreen splashScreen = new SplashScreen();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, splashScreen).commit();

            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                MainScreen mainScreen = new MainScreen();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mainScreen).commit();
            }, 2500);
        }
    }

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
    }
}