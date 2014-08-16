package com.lpetroli.simplegps.infra.models.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lorenzo on 15/08/2014.
 */
public class System {
    @SerializedName("message")
    String mMessage;

    @SerializedName("country")
    String mCountry;

    @SerializedName("sunrise")
    Long mSunrise;

    @SerializedName("sunset")
    Long mSunset;

    public String getCountry() {
        return mCountry;
    }
}
