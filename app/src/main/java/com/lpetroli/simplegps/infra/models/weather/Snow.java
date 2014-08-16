package com.lpetroli.simplegps.infra.models.weather;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

/**
 * Created by Lorenzo on 16/08/2014.
 */
public class Snow {
    @SerializedName("3h")
    private BigDecimal mLast3h;
}
