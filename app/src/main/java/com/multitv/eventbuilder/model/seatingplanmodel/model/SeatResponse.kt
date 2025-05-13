package com.multitv.eventbuilder.model.seatingplanmodel.model

import com.google.gson.annotations.SerializedName

data class SeatResponse(
    @SerializedName("message") val message: String,
    @SerializedName("page_tittle") val pageTittle: String,
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<SeatData>
)

data class SeatData(
    @SerializedName("image6") val image6: String,
    @SerializedName("seat_no") val seat_no: String

)
