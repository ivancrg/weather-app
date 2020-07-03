package com.example.weatherapp;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SplashScreen splashScreen = new SplashScreen();
        getSupportFragmentManager().beginTransaction().add(R.id.mainContainer, splashScreen).commit();


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainScreen mainScreen = new MainScreen();
                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, mainScreen).commit();
            }
        }, 2500);
    }
}