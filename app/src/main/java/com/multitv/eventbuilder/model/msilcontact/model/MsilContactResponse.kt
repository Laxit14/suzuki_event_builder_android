package com.multitv.eventbuilder.model.msilcontact.model

import com.google.gson.annotations.SerializedName

data class MsilContactResponse(
    @SerializedName("message") val message: String,
    @SerializedName("page_tittle") val pageTittle: String,
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<ContactGroup>
)

data class ContactGroup(
    @SerializedName("title") val title: String,
    @SerializedName("data") val contacts: List<ContactItem>
)

data class ContactItem(
    @SerializedName("id") val id: Int,
    @SerializedName("msil_id") val msilId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("mobile") val mobile: String,
    @SerializedName("status") val status: Int,
    @SerializedName("title") val title: String
)
