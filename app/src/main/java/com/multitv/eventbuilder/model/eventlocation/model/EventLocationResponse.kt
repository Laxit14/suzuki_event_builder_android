package com.multitv.eventbuilder.model.eventlocation.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class EventLocationResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("page_tittle")
    val pageTittle: String,

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: EventLocationData
) : Serializable

data class EventLocationData(
    @SerializedName("event_location")
    val eventLocation: EventLocation
) : Serializable

data class EventLocation(
    @SerializedName("title")
    val title: String,

    @SerializedName("segments")
    val segments: List<Segment>
) : Serializable

data class Segment(
    @SerializedName("type")
    val type: String,

    @SerializedName("title")
    val title: String,

    /*@SerializedName("duration")
    val duration: String,*/

    @SerializedName("from")
    val from: LocationInfo,

    @SerializedName("to")
    val to: LocationInfo,

    @SerializedName("icon")
    val icon: String,

    @SerializedName("image")
    val image: String,

    @SerializedName("link")
    val link: String

) : Serializable

data class LocationInfo(
    @SerializedName("name")
    val name: String,

    @SerializedName("place_type")
    val placeType: String
) : Serializable
