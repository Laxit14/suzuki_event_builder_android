package com.multitv.eventbuilder.model.confrence_detail.model

import com.google.gson.annotations.SerializedName

data class ShuttleBusAgendaResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("page_tittle")
    val pageTittle: String,

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<ShuttleBusDateItem>
)

data class ShuttleBusDateItem(
    @SerializedName("date")
    val date: String,

    @SerializedName("data")
    val data: List<ShuttleBusDataItem>
)

data class ShuttleBusDataItem(
    @SerializedName("id")
    val id: Int,

    @SerializedName("shuttle_bus_id")
    val shuttleBusId: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("time")
    val time: String,

    @SerializedName("about")
    val about: String,

    @SerializedName("status")
    val status: Int
)
