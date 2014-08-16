package com.lpetroli.simplegps.infra.models.weather;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

/**
 * Created by Lorenzo on 15/08/2014.
 */
public class Coordinates {
    @SerializedName("lon")
    BigDecimal mLongitude;

    @SerializedName("lat")
    BigDecimal mLatitude;
}
