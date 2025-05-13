package com.multitv.eventbuilder.model.interactionmodel.model

import com.google.gson.annotations.SerializedName

data class InteractionResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("page_tittle")
    val pageTittle: String,

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<InteractionDataItem>
)

data class InteractionDataItem(
    @SerializedName("id")
    val id: Int,

    @SerializedName("parnt_id")
    val parentId: Int,

    @SerializedName("event_id")
    val eventId: Int,

    @SerializedName("file")
    val file: String,

    @SerializedName("order_index")
    val orderIndex: Int,

    @SerializedName("status")
    val status: Int,

    @SerializedName("title")
    val title: String,


    @SerializedName("slug")
    val slug: String
)
