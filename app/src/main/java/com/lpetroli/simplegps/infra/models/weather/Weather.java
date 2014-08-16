package com.lpetroli.simplegps.infra.models.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lorenzo on 15/08/2014.
 */
public class Weather {
    @SerializedName("id")
    Integer mId;

    @SerializedName("main")
    String mMain;

    @SerializedName("description")
    String mDescription;

    @SerializedName("icon")
    String mIcon;
}
