package com.multitv.eventbuilder.model.hotelstaymodel.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class HotelStayResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("page_tittle")
    val pageTittle: String,

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<HotelStayItem>
)

data class HotelStayItem(
    @SerializedName("id")
    val id: Int,

    @SerializedName("hotel_id")
    val hotelId: Int,

    @SerializedName("image")
    val image: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("about")
    val about: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("location")
    val location: String,

    @SerializedName("link")
    val link: String,

    @SerializedName("status")
    val status: Int,

    @SerializedName("created")
    val created: String,

    @SerializedName("contact_info")
    val contactInfo: List<ContactInfo>
)
@Parcelize
data class ContactInfo(
    @SerializedName("title")
    val title: String,

    @SerializedName("value")
    val value: String
) : Parcelable
