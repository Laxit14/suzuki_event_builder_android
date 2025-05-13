package com.multitv.eventbuilder.model.aboutdoha.model

import com.google.gson.annotations.SerializedName

data class AboutDohaResponse(
    @SerializedName("message")
    val message: String,



    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: AboutDohaItems
)

data class AboutDohaItems(
    @SerializedName("banner_tittle")
    val bannerTitle: String,

    @SerializedName("banner_image")
    val bannerImage: String,

    @SerializedName("page_tittle")
    val pageTittle: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("Sightseeing")
    val sightseeing: List<CategoryItem>,

    @SerializedName("Rastaurents")
    val rastaurents: List<CategoryItem>,

    @SerializedName("Shopping")
    val shopping: List<CategoryItem>
)

data class CategoryItem(
    @SerializedName("id")
    val id: Int,

    @SerializedName("doha_id")
    val dohaId: Int,

    @SerializedName("tittle")
    val title: String,

    @SerializedName("category")
    val category: String,

    @SerializedName("status")
    val status: Int,

    @SerializedName("created")
    val created: String,

    @SerializedName("location")
    val location: List<LocationItem>
)

data class LocationItem(
    @SerializedName("image")
    val image: String,

    @SerializedName("location")
    val location: String,

    @SerializedName("link")
    val link: String
)
