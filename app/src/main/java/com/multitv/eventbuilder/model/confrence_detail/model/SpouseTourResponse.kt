package com.multitv.eventbuilder.model.confrence_detail.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SpouseTourResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("page_tittle")
    val pageTittle: String,

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<SpouseTourDateItem>
) : Serializable

data class SpouseTourDateItem(
    @SerializedName("date")
    val date: String,

    @SerializedName("data")
    val data: List<SpouseTourDataItem>
) : Serializable

data class SpouseTourDataItem(
    @SerializedName("id")
    val id: Int,

    @SerializedName("tittle")
    val title: String,

    @SerializedName("time")
    val time: String,

    @SerializedName("date")
    val dateTime: String,

    @SerializedName("about")
    val about: String,

    @SerializedName("status")
    val status: Int
) : Serializable

