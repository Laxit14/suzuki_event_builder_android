package com.multitv.eventbuilder.model.otpmodel.model

import com.google.gson.annotations.SerializedName

data class OtpResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("data")
    val data: OtpData?
)

data class OtpData(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("mobile")
    val mobile: String,

    @field:SerializedName("image5")
    val image5: String,

    @field:SerializedName("otp")
    val otp: String
)
