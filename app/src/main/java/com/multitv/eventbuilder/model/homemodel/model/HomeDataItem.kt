package com.multitv.eventbuilder.model.homemodel.model

import com.google.gson.annotations.SerializedName

data class HomeDataItem(
    @SerializedName("message") val message: String,
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: DataItem
)

data class DataItem(
    @SerializedName("banner_data") val bannerData: List<BannerData>? = null,
    @SerializedName("pages_data") val pagesData: List<PagesData>? = null,
    @SerializedName("notification_data") val notificationData: List<NotificationData>? = null,
    @SerializedName("marque_text") val marqueeText: String? = null

)

data class BannerData(
    @SerializedName("id") val id: Int,
    @SerializedName("event_id") val eventId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("image") val image: String,
    @SerializedName("banner_link") val bannerLink: String,
    @SerializedName("order_index") val orderIndex: Int,
    @SerializedName("status") val status: Int,
    @SerializedName("created_at") val createdAt: String
)

data class PagesData(
    @SerializedName("id") val id: Int,
    @SerializedName("parent_id") val parentId: Int,
    @SerializedName("event_id") val eventId: Int,
    @SerializedName("file") val file: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("order_index") val orderIndex: Int,
    @SerializedName("status") val status: Int,
    @SerializedName("title") val title: String
)

data class NotificationData(
    @SerializedName("id") val id: Int,
    @SerializedName("event_id") val eventId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("created") val created: String
)

