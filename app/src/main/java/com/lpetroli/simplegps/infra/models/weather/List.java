package com.lpetroli.simplegps.infra.models.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lorenzo on 16/08/2014.
 */
public class List {
    @SerializedName("id")
    private Long mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("coord")
    private Coordinates mCoordinates;

    @SerializedName("main")
    private Main mMain;

    @SerializedName("dt")
    private Long mDataTime;

    @SerializedName("wind")
    private Wind mWind;

    @SerializedName("sys")
    private System mSystem;

    @SerializedName("clouds")
    private Clouds mClouds;

    @SerializedName("weather")
    private Weather[] mWeather;

    @SerializedName("rain")
    private Rain mRain;

    @SerializedName("snow")
    private Snow mSnow;

    public String getName() {
        return mName;
    }

    public System getSystem() {
        return mSystem;
    }

    public Main getMain() {
        return mMain;
    }
}
