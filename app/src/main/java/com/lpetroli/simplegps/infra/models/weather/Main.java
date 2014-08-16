package com.lpetroli.simplegps.infra.models.weather;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

/**
 * Created by Lorenzo on 15/08/2014.
 */
public class Main {
    @SerializedName("temp")
    BigDecimal mTemperature;

    @SerializedName("pressure")
    BigDecimal mPressure;

    @SerializedName("humidity")
    BigDecimal mHumidity;

    @SerializedName("temp_min")
    BigDecimal mMinTemperature;

    @SerializedName("temp_max")
    BigDecimal mMaxTemperature;

    @SerializedName("sea_level")
    BigDecimal mSeaPressure;

    @SerializedName("grnd_level")
    BigDecimal mGroundPressure;

    public float getTemperature() {
        return mTemperature.floatValue();
    }
}
