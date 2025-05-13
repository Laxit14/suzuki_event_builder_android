package com.multitv.eventbuilder.model


enum class MediaType {
    IMAGE, VIDEO
}

/*
data class BannerDataItem(
    val imageResId: Int, // Drawable resource ID
    val text: String
)
*/

data class BannerDataItem(
    val mediaType: MediaType,
    val imageResId: Int,
    val mediaUrl: String, // Can be image URL or video URL
    val text: String
)
