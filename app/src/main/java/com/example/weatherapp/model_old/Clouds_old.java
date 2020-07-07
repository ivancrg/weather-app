package com.example.weatherapp.model_old;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Clouds_old {
    @SerializedName("all")
    @Expose
    private Integer coverage;

    public Integer getCoverage() {
        return coverage;
    }

    public void setCoverage(Integer coverage) {
        this.coverage = coverage;
    }

    @Override
    public String toString() {
        return "Clouds{" +
                "coverage=" + coverage +
                '}';
    }
}
