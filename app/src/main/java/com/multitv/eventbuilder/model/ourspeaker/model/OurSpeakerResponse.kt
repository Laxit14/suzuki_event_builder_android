package com.multitv.eventbuilder.model.ourspeaker.model

import com.google.gson.annotations.SerializedName

data class OurSpeakerResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("page_tittle")
    val pageTittle: String,

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<SpeakerItem>
)

data class SpeakerItem(
    @SerializedName("id")
    val id: Int,

    @SerializedName("event_id")
    val eventId: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("designation")
    val designation: String,

    @SerializedName("company")
    val company: String,

    @SerializedName("image")
    val image: String,

    @SerializedName("pdf")
    val pdf: String
)
