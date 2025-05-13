package com.multitv.eventbuilder.model

import com.google.gson.annotations.SerializedName

data class ResponseNotification(
    @field:SerializedName("data")
    val data: List<NotificationDataItem?>? = null,
    @field:SerializedName("success")
    val success: Boolean? = null,
    @field:SerializedName("message")
    val message: String? = null
)
data class NotificationDataItem(
    @field:SerializedName("image")
    val image: String? = null,
    @field:SerializedName("event_id")
    val eventId: Int? = null,
    @field:SerializedName("page_name")
    val pageName: String? = null,
    @field:SerializedName("text_field")
    val textField: String? = null,
    @field:SerializedName("created_at")
    val createdAt: String? = null,
    @field:SerializedName("id")
    val id: Int? = null,
    @field:SerializedName("category")
    val category: String? = null
)

