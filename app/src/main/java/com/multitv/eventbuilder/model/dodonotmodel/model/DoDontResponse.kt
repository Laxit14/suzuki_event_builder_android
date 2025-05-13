package com.multitv.eventbuilder.model.dodonotmodel.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DoDontResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("page_tittle")
    val pagetittle: String,

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<DoDontItem>
) : Serializable

data class DoDontItem(
    @SerializedName("id")
    val id: Int,

    @SerializedName("do_and_dont_id")
    val doAndDontId: Int,

    @SerializedName("type")
    val type: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("status")
    val status: Int,

    @SerializedName("created")
    val created: String
) : Serializable
