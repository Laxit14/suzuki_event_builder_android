package com.multitv.eventbuilder.model

data class TripItem(
    val title: String,
    val duration: String,
    val distance: String,
    val source: String,
    val destination: String,
    val icon: Int, // Drawable resource ID for the icon (flight or car)
    val image: Int // Drawable resource ID for the icon (flight or car)
)
