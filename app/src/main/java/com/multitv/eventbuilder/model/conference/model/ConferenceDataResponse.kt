package com.multitv.eventbuilder.model.conference.model

import com.google.gson.annotations.SerializedName

data class ConferenceDataResponse(
    @SerializedName("message") val message: String,
    @SerializedName("page_tittle") val pageTittle: String,
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<ConferenceSubDataItem>
)

data class ConferenceSubDataItem(
    @SerializedName("id") val id: Int,
    @SerializedName("parent_id") val parentId: Int,
    @SerializedName("event_id") val eventId: Int,
    @SerializedName("file") val file: String,
    @SerializedName("order_index") val orderIndex: Int,
    @SerializedName("title") val title: String,
    @SerializedName("slug") val slug: String
)
