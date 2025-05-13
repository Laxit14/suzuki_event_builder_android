package com.multitv.eventbuilder.model.lamplightingmodel

import com.google.gson.annotations.SerializedName

data class PartsResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("page_tittle")
    val pageTittle: String,

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<PartItem>,

    @SerializedName("localization_banner")
    val localizationBanner: String
)



data class PartItem(
    @SerializedName("id")
    val id: Int,

    @SerializedName("event_id")
    val eventId: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("image")
    val image: String
)

