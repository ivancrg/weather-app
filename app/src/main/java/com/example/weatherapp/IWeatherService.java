package com.example.weatherapp;

import com.example.weatherapp.model.WeatherData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IWeatherService {
    //@GET("/data/2.5/weather?q=London,uk&appid=dcc06c34cf7bba0ff30aabd3269d249b"){
    //@GET("/data/2.5/weather")
    @GET("/data/2.5/onecall")
    //Call<WeatherData> getCurrentWeatherData();
    //Call<WeatherData> getCurrentWeatherData(@Query("q") String cityAndCountry, @Query("appid") String APPID);
    Call<WeatherData> getCurrentWeatherData(@Query("lat") double lat, @Query("lon") double lon, @Query("appid") String APPID);
}
