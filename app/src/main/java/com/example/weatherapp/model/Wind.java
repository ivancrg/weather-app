package com.example.weatherapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wind {
    @SerializedName("speed")
    @Expose
    private Double speed;

    @SerializedName("deg")
    @Expose
    private Double direction;

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getDirection() {
        return direction;
    }

    public void setDirection(Double direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "Wind{" +
                "speed=" + speed +
                ", direction=" + direction +
                '}';
    }
}
