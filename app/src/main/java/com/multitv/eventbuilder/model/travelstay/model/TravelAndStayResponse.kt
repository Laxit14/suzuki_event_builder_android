package com.multitv.eventbuilder.model.travelstay.model

import com.google.gson.annotations.SerializedName

data class TravelAndStayResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("page_tittle")
    val pageTittle: String,

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<TravelInfoItem>
)

data class TravelInfoItem(
    @SerializedName("id")
    val id: Int,

    @SerializedName("parent_id")
    val parentId: Int,

    @SerializedName("event_id")
    val eventId: Int,

    @SerializedName("file")
    val file: String,

    @SerializedName("order_index")
    val orderIndex: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("slug")
    val slug: String
)

