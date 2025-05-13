package com.multitv.eventbuilder.constant

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object Helper {

    fun String.capitalizeWords(): String {
        return this.split(" ")
            .joinToString(" ") { word ->
                word.lowercase().replaceFirstChar { it.uppercase() }
            }
    }

    // Extension function to convert "yyyy-MM-dd" to "28th April"
    fun String.toFormattedDate(): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = inputFormat.parse(this)
            val calendar = Calendar.getInstance().apply { time = date!! }
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = SimpleDateFormat("MMMM", Locale.getDefault()).format(calendar.time)
            "${day.withSuffix()} $month"
        } catch (e: Exception) {
            this // fallback to original string if parsing fails
        }
    }

    // Helper function to add ordinal suffix to day
    fun Int.withSuffix(): String {
        return if (this in 11..13) "${this}th"
        else when (this % 10) {
            1 -> "${this}st"
            2 -> "${this}nd"
            3 -> "${this}rd"
            else -> "${this}th"
        }
    }
}