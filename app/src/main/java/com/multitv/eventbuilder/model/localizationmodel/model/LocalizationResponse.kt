package com.multitv.eventbuilder.model.localizationmodel.model

import com.google.gson.annotations.SerializedName

data class LocalizationResponse(
    val message: String,
    val page_tittle: String,
    val success: Boolean,
    val data: List<LocalizationItem>,
    @SerializedName("localization_banner")
    val localizationBanner: String
)

data class LocalizationItem(
    val id: Int,
    @SerializedName("event_id")
    val eventId: Int,
    val name: String,
    val description: String,
    val image: String
)

