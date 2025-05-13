package com.multitv.eventbuilder.model.digitalexhibitionfeedbackmodel.model

import com.google.gson.annotations.SerializedName

data class DigitalExhibitionFeedbackResponse (
    @SerializedName("message") val message : String,
    @SerializedName("status") val status : String
)