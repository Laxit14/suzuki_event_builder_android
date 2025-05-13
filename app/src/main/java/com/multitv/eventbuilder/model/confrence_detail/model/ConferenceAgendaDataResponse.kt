package com.multitv.eventbuilder.model.confrence_detail.model

import com.google.gson.annotations.SerializedName

data class ConferenceAgendaDataResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("page_tittle")
    val pageTittle: String,

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<ConferenceAgendaDateItem>
)

data class ConferenceAgendaDateItem(
    @SerializedName("date")
    val date: String,

    @SerializedName("data")
    val data: List<ConferenceDataItem>
)

data class ConferenceDataItem(
    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("start_date")
    val startDate: String
)
