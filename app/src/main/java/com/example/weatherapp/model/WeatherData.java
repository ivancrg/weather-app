package com.example.weatherapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WeatherData {
    @SerializedName("lat")
    @Expose
    private double latitude;

    @SerializedName("lon")
    @Expose
    private double longitude;

    @SerializedName("timezone")
    @Expose
    private String timezone;

    @SerializedName("timezone_offset")
    @Expose
    private float timezone_offset;

    @SerializedName("current")
    @Expose
    private Current current;

    @SerializedName("minutely")
    @Expose
    private List<Minutely> minutely;

    @SerializedName("hourly")
    @Expose
    private List<Hourly> hourly;

    @SerializedName("daily")
    @Expose
    private List<Daily> daily;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public float getTimezone_offset() {
        return timezone_offset;
    }

    public void setTimezone_offset(float timezone_offset) {
        this.timezone_offset = timezone_offset;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public List<Minutely> getMinutely() {
        return minutely;
    }

    public void setMinutely(List<Minutely> minutely) {
        this.minutely = minutely;
    }

    public List<Hourly> getHourly() {
        return hourly;
    }

    public void setHourly(List<Hourly> hourly) {
        this.hourly = hourly;
    }

    public List<Daily> getDaily() {
        return daily;
    }

    public void setDaily(List<Daily> daily) {
        this.daily = daily;
    }

    @Override
    public String toString() {
        return "WeatherData{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", timezone='" + timezone + '\'' +
                ", timezone_offset=" + timezone_offset +
                ", current=" + current +
                //", minutely=" + minutely +
                //", hourly=" + hourly +
                ", daily=" + daily +
                '}';
    }
}