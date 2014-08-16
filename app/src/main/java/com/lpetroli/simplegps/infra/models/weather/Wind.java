package com.lpetroli.simplegps.infra.models.weather;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

/**
 * Created by Lorenzo on 15/08/2014.
 */
public class Wind {
    @SerializedName("speed")
    BigDecimal mSpeed;

    @SerializedName("deg")
    BigDecimal mDegree;

    @SerializedName("gust")
    BigDecimal mGust;
}
