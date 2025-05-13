package com.multitv.eventbuilder.model.dresscode.model

import com.google.gson.annotations.SerializedName

data class DressCodeResponse(
    @SerializedName("message") val message: String,
    @SerializedName("page_tittle") val pageTittle: String,
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<DressCodeSubItem>
)

data class DressCodeSubItem(
    @SerializedName("id") val id: Int,
    @SerializedName("dress_id") val dressId: Int,
    @SerializedName("for_dress") val forDress: String,
    @SerializedName("dress_type") val dressType: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("image") val image: String
)
