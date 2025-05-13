package com.multitv.eventbuilder.model.travel_detail


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TravelDetailResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<TravelDetailItem>
) : Serializable

data class TravelDetailItem(
    @SerializedName("id")
    val id: Int,

    @SerializedName("travel_id")
    val travelId: Int,

    @SerializedName("travel_details")
    val travelDetails: String,

    @SerializedName("arrival_info")
    val arrivalInfo: String,

    @SerializedName("departure_info")
    val departureInfo: String,

    @SerializedName("hotel_details")
    val hotelDetails: String,

    @SerializedName("travel_itinerary")
    val travelItinerary: String,

    @SerializedName("status")
    val status: Int,

    @SerializedName("created")
    val created: String
) : Serializable
