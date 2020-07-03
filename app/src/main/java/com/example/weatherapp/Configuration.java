package com.example.weatherapp;

public abstract class Configuration {
    private static final String BASE_URL = "https://api.openweathermap.org";
    private static final String APP_ID = "dcc06c34cf7bba0ff30aabd3269d249b";

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static String getAppId() {
        return APP_ID;
    }
}
