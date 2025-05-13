package com.multitv.eventbuilder.model.digitalexhibitionmodel.model

import com.google.gson.annotations.SerializedName

data class DigitalExhibitionResponse(

    @SerializedName("message")
    val message: String?=null,

    @SerializedName("page_title")
    val pageTittle: String?=null,

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<DigitalExhibitionItem>
)

data class DigitalExhibitionItem(
    @SerializedName("id")
    val id: Int,

    @SerializedName("event_id")
    val eventId: Int,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("batch_no")
    val batchNo: String?=null,

    @SerializedName("date")
    val date: String?=null,

    @SerializedName("description")
    val description: String?=null,

    @SerializedName("title")
    val title: String?=null,

    @SerializedName("time")
    val time: String?=null,

    @SerializedName("image")
    val image: List<String>,

    @SerializedName("status")
    val status: Int,

    @SerializedName("created")
    val created: String?=null
)
