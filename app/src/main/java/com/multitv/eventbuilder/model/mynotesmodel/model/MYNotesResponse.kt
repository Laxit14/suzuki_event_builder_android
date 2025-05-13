package com.multitv.eventbuilder.model.mynotesmodel.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class MYNotesResponse(
    @SerializedName("message") val message: String,
    @SerializedName("page_tittle") val pageTittle: String,
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: List<MyNotesItem>
)
@Parcelize
data class MyNotesItem(
    @SerializedName("id") val id: Int,
    @SerializedName("speaker_id") val speakerId: Int,
    @SerializedName("event_id") val eventId: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("tittle") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("text") val text: String?,
    @SerializedName("status") val status: Int,
    @SerializedName("created") val created: String
): Parcelable
