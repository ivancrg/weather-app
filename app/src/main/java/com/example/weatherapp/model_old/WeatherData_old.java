package com.example.weatherapp.model_old;

import com.example.weatherapp.model.Weather;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WeatherData_old {

    @SerializedName("coord")
    @Expose
    private Coordinates_old coordinates;

    @SerializedName("weather")
    @Expose
    private List<Weather_old> weather = null;

    @SerializedName("main")
    @Expose
    private MainData_old mainData;

    @SerializedName("wind")
    @Expose
    private Wind_old wind;

    @SerializedName("clouds")
    @Expose
    private Clouds_old clouds;

    @SerializedName("sys")
    @Expose
    private SystemData_old systemData;

    @SerializedName("base")
    @Expose
    private String base;

    @SerializedName("visibility")
    @Expose
    private Integer visibility;

    @SerializedName("dt")
    @Expose
    private Integer dt;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("cod")
    @Expose
    private Integer cod;

    public Coordinates_old getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates_old coordinates) {
        this.coordinates = coordinates;
    }

    public List<Weather_old> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather_old> weather) {
        this.weather = weather;
    }

    public MainData_old getMainData() {
        return mainData;
    }

    public void setMainData(MainData_old mainData) {
        this.mainData = mainData;
    }

    public Wind_old getWind() {
        return wind;
    }

    public void setWind(Wind_old wind) {
        this.wind = wind;
    }

    public Clouds_old getClouds() {
        return clouds;
    }

    public void setClouds(Clouds_old clouds) {
        this.clouds = clouds;
    }

    public SystemData_old getSystemData() {
        return systemData;
    }

    public void setSystemData(SystemData_old systemData) {
        this.systemData = systemData;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public Integer getDt() {
        return dt;
    }

    public void setDt(Integer dt) {
        this.dt = dt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    @NotNull
    @Override
    public String toString() {
        return "WeatherData{" +
                "coordinates=" + coordinates +
                ", weather=" + weather +
                ", mainData=" + mainData +
                ", wind=" + wind +
                ", clouds=" + clouds +
                ", systemData=" + systemData +
                ", base='" + base + '\'' +
                ", visibility=" + visibility +
                ", dt=" + dt +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", cod=" + cod +
                '}';
    }
}