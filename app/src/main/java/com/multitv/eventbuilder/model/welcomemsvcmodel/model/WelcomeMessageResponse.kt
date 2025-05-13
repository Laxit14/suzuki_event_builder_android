package com.multitv.eventbuilder.model.welcomemsvcmodel.model

import com.google.gson.annotations.SerializedName

data class WelcomeMessageResponse(
    @SerializedName("message") val message: String,
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<WelcomeMessageDataItem>
)

data class WelcomeMessageDataItem(
    @SerializedName("id") val id: Int,
    @SerializedName("banner_id") val bannerId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("img") val img: String,
    @SerializedName("conference") val conference: String,
    @SerializedName("history") val history: String,
    @SerializedName("action_plan") val actionPlan: String,
    @SerializedName("details") val details: String,
    @SerializedName("status") val status: Int,
    @SerializedName("created") val created: String
)
