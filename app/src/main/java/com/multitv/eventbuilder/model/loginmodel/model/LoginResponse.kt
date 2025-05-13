package com.multitv.eventbuilder.model.loginmodel.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class LoginResponse(
    @field:SerializedName("message") val message: String,
    @field:SerializedName("success") val success: Boolean,
    @field:SerializedName("data") val data: Data
)

@Parcelize
data class Data(
    @field:SerializedName("id") val id: Long,
    @field:SerializedName("name") val name: String? = null,
    @field:SerializedName("email") val email: String? = null,
    @field:SerializedName("mobile") val mobile: String? = null,
    @field:SerializedName("category") val category: String? = null,
    @field:SerializedName("otp") val otp: Long,
    @field:SerializedName("upload_image") val uploadImage: String? = null
) : Parcelable
