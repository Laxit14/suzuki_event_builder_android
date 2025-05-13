package com.multitv.eventbuilder.model.confrence_detail.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DetailAgendaResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("page_tittle")
    val pageTittle: String,

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<DetailAgendaResponseDateItem>
) : Serializable

data class DetailAgendaResponseDateItem(
    @SerializedName("date")
    val date: String,

    @SerializedName("data")
    val data: List<DetailAgendaResponseDataItem>
) : Serializable

data class DetailAgendaResponseDataItem(
    @SerializedName("id")
    val id: Int,

    @SerializedName("agenda_id")
    val agendaId: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("place")
    val place: String,

    @SerializedName("schedule_time")
    val scheduleTime: String
) : Serializable
