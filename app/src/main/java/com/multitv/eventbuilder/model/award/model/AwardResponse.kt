package com.multitv.eventbuilder.model.award.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AwardResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("page_tittle")
    val pageTittle: String,

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<AwardDataItem>
) : Serializable

data class AwardDataItem(
    @SerializedName("image5")
    val image5: List<String>
) : Serializable
