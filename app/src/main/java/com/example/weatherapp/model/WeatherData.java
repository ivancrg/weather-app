package com.example.weatherapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WeatherData {

    @SerializedName("coord")
    @Expose
    private Coordinates coordinates;

    @SerializedName("weather")
    @Expose
    private List<Weather> weather = null;

    @SerializedName("main")
    @Expose
    private MainData mainData;

    @SerializedName("wind")
    @Expose
    private Wind wind;

    @SerializedName("clouds")
    @Expose
    private Clouds clouds;

    @SerializedName("sys")
    @Expose
    private SystemData systemData;

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

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public MainData getMainData() {
        return mainData;
    }

    public void setMainData(MainData mainData) {
        this.mainData = mainData;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public SystemData getSystemData() {
        return systemData;
    }

    public void setSystemData(SystemData systemData) {
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