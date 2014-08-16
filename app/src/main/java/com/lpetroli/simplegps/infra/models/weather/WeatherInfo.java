package com.lpetroli.simplegps.infra.models.weather;

import com.google.gson.annotations.SerializedName;


/**
 * Created by Lorenzo on 15/08/2014.
 */
public class WeatherInfo {
    public static final int CODE_OK = 200;

    @SerializedName("message")
    private String mMessage;

    @SerializedName("cod")
    private Integer mCode;

    @SerializedName("count")
    private Integer mCount;

    @SerializedName("list")
    private List[] mList;

    public int getCode() {
        return mCode;
    }

    public String getCityName() {
        // For now it is always true that there will be only 1 weather entry.
        return mList[0].getName();
    }

    public String getCountryName() {

        // For now it is always true that there will be only 1 weather entry.
        return mList[0].getSystem().getCountry();
    }

    public Integer getCityCount() {
        return mCount;
    }

    public float getTemperature() {
        return mList[0].getMain().getTemperature();
    }
}
