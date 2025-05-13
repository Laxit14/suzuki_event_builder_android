package com.multitv.eventbuilder.model.fooddetailmodel.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FoodScheduleResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("page_tittle")
    val pageTittle: String,

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<FoodDateItem>
) : Serializable

data class FoodDateItem(
    @SerializedName("date")
    val date: String,

    @SerializedName("data")
    val data: List<FoodDetailItem>
) : Serializable

data class FoodDetailItem(
    @SerializedName("id")
    val id: Int,

    @SerializedName("food_id")
    val foodId: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("time")
    val time: String,

    @SerializedName("about")
    val about: String,

    @SerializedName("status")
    val status: Int
) : Serializable

