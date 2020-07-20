package com.example.weatherapp;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RelativeLayout relativeLayout;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Configuration.setContext(getApplicationContext());
        Configuration.refreshPreferences();

        if (Configuration.isFirstStart()) {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
            editor.putBoolean("firstStart", true);
            editor.putInt("colorPrimary", ContextCompat.getColor(this, R.color.colorPrimary));
            editor.putInt("colorPrimaryDark", ContextCompat.getColor(this, R.color.colorPrimaryDark));
            editor.putInt("navigationBackground", ContextCompat.getColor(this, R.color.navigationBackground));
            editor.putInt("navigationIcon", ContextCompat.getColor(this, R.color.navigationIcon));
            editor.putInt("navigationText", ContextCompat.getColor(this, R.color.navigationText));
            editor.apply();

            Configuration.refreshPreferences();
        }

        relativeLayout = findViewById(R.id.navigation_bottom_layout);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        drawerLayout = findViewById(R.id.navigation_drawer_layout);
        NavigationView navigationDrawerView = findViewById(R.id.navigation_drawer_view);
        navigationDrawerView.setNavigationItemSelectedListener(this);

        initializeSettings();

        FrameLayout splashContainer = findViewById(R.id.splash_container);

        if (savedInstanceState == null) {
            SplashScreen splashScreen = new SplashScreen();
            getSupportFragmentManager().beginTransaction().add(R.id.splash_container, splashScreen).commit();

            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                splashContainer.setVisibility(View.GONE);
                MainScreen mainScreen = new MainScreen();

                if (!Configuration.isNavigationTypeBottom())
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_drawer, mainScreen).commit();
                else
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_bottom, mainScreen).commit();
            }, 2500);
        }
    }

    @Override
    public void onResume(){
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.navigation_header_layout);
        if (linearLayout != null)
            linearLayout.setBackgroundColor(Configuration.getNavigationBackground());
        super.onResume();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            menuItem -> {
                Fragment selectedFragment = null;

                switch (menuItem.getItemId()) {
                    case R.id.navigation_bottom_main:
                        selectedFragment = new MainScreen();
                        break;
                    case R.id.navigation_bottom_forecastweek:
                        selectedFragment = new ForecastWeekScreen();
                        break;
                    case R.id.navigation_bottom_forecast48:
                        selectedFragment = new Forecast48Screen();
                        break;
                    case R.id.navigation_bottom_search:
                        selectedFragment = new SearchScreen();
                        break;
                    case R.id.navigation_bottom_settings:
                        selectedFragment = new SettingsScreen();
                        break;
                }

                assert selectedFragment != null;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_bottom,
                        selectedFragment).commit();

                return true;
            };

    private void initializeSettings() {
        Configuration.refreshPreferences();

        if(!Configuration.isDarkModeEnabled())
            applyColors();
        else
            applyDarkColors();

        if (Configuration.isToolbarEnabled())
            getSupportActionBar().show();
        else
            getSupportActionBar().hide();

        if (Configuration.isNavigationTypeDrawer())
            relativeLayout.setVisibility(View.GONE);
        else
            drawerLayout.setVisibility(View.GONE);
    }

    void applyColors() {
        setTheme(R.style.AppTheme);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setBackgroundColor(Configuration.getNavigationBackground());

        NavigationView drawerNavigationView = findViewById(R.id.navigation_drawer_view);
        drawerNavigationView.setBackgroundColor(Color.WHITE);

        int[][] states = new int[][]{
                new int[]{android.R.attr.state_enabled}, // enabled
                new int[]{-android.R.attr.state_enabled}, // disabled
                new int[]{-android.R.attr.state_checked}, // unchecked
                new int[]{android.R.attr.state_pressed}  // pressed
        };

        int[] colors = new int[]{
                Configuration.getNavigationText(),
                Configuration.getNavigationText(),
                Configuration.getNavigationText(),
                Configuration.getNavigationText()
        };
        ColorStateList colorStateList = new ColorStateList(states, colors);
        bottomNavigationView.setItemTextColor(colorStateList);
        drawerNavigationView.setItemTextColor(colorStateList);

        colors = new int[]{
                Configuration.getNavigationIcon(),
                Configuration.getNavigationIcon(),
                Configuration.getNavigationIcon(),
                Configuration.getNavigationIcon()
        };
        colorStateList = new ColorStateList(states, colors);
        bottomNavigationView.setItemIconTintList(colorStateList);
        drawerNavigationView.setItemIconTintList(colorStateList);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Configuration.getColorPrimary()));

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.navigation_header_layout);
        if (linearLayout != null)
            linearLayout.setBackgroundColor(Configuration.getNavigationBackground());
    }

    public void changeNavigation(boolean isNavigationDrawer) {
        Configuration.refreshPreferences();

        if (isNavigationDrawer) {
            relativeLayout.setVisibility(View.GONE);
            drawerLayout.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_drawer, new SettingsScreen()).commit();
        } else {
            drawerLayout.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
            bottomNavigationView.setSelectedItemId(R.id.navigation_bottom_settings);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_bottom, new SettingsScreen()).commit();
        }

        if(!Configuration.isDarkModeEnabled())
            applyColors();
        else
            applyDarkColors();
    }

    public void applyDarkColors(){
        setTheme(R.style.DarkPreferenceTheme);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setBackgroundColor(Color.DKGRAY);

        NavigationView drawerNavigationView = findViewById(R.id.navigation_drawer_view);
        drawerNavigationView.setBackgroundColor(Color.DKGRAY);

        int[][] states = new int[][]{
                new int[]{android.R.attr.state_enabled}, // enabled
                new int[]{-android.R.attr.state_enabled}, // disabled
                new int[]{-android.R.attr.state_checked}, // unchecked
                new int[]{android.R.attr.state_pressed}  // pressed
        };

        int[] colors = new int[]{
                Color.BLACK,
                Color.BLACK,
                Color.BLACK,
                Color.BLACK
        };
        ColorStateList colorStateList = new ColorStateList(states, colors);
        bottomNavigationView.setItemTextColor(colorStateList);
        drawerNavigationView.setItemTextColor(colorStateList);
        bottomNavigationView.setItemIconTintList(colorStateList);
        drawerNavigationView.setItemIconTintList(colorStateList);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Configuration.getColorPrimaryDark()));

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.navigation_header_layout);
        if (linearLayout != null)
            linearLayout.setBackgroundColor(Color.DKGRAY);
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
            case R.id.navigation_drawer_main:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_drawer, new MainScreen()).commit();
                break;
            case R.id.navigation_drawer_forecastweek:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_drawer, new ForecastWeekScreen()).commit();
                break;
            case R.id.navigation_drawer_forecast48:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_drawer, new Forecast48Screen()).commit();
                break;
            case R.id.navigation_drawer_search:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_drawer, new SearchScreen()).commit();
                break;
            case R.id.navigation_drawer_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_drawer, new SettingsScreen()).commit();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}