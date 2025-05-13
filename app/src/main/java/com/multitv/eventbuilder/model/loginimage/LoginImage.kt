package com.multitv.eventbuilder.model.loginimage

import com.google.gson.annotations.SerializedName

data class LoginImage(
    @SerializedName("message") val message : String,
    @SerializedName("status") val status : Int,
    @SerializedName("publisher_response") val publisherResponse : String,
    @SerializedName("upload_image") val uploadImage : String

)
