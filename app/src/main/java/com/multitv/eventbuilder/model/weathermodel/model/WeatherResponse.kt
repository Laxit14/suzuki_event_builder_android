package com.multitv.eventbuilder.model.weathermodel.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class WeatherResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("location")
    val location: String,

    @SerializedName("current_temp_c")
    val currentTempC: Double,

    @SerializedName("current_condition")
    val currentCondition: String,

    @SerializedName("current_icon")
    val currentIcon: String,

    @SerializedName("page_tittle")
    val pageTittle: String,

    @SerializedName("forecast")
    val forecast: List<ForecastItem>
) : Serializable

data class ForecastItem(
    @SerializedName("date")
    val date: String,

    @SerializedName("avg_temp_c")
    val avgTempC: Double,

    @SerializedName("min_temp_c")
    val minTemp: Double,

    @SerializedName("max_temp_c")
    val maxTemp: Double,

    @SerializedName("humidity")
    val humidity: Int,

    @SerializedName("wind_kph")
    val wind_kph: Double,

    @SerializedName("condition")
    val condition: String,

    @SerializedName("icon")
    val icon: String
) : Serializable
