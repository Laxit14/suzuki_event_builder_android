package com.multitv.eventbuilder.model.feedbackmodel.model

import com.google.gson.annotations.SerializedName

data class FeedbackResponse(
    @SerializedName("message") val message : String,
    @SerializedName("status") val status : Int
)
