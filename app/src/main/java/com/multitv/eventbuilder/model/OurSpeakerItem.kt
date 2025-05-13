package com.multitv.eventbuilder.model

import android.os.Parcelable
import androidx.versionedparcelable.VersionedParcelize
import kotlinx.parcelize.Parcelize

@Parcelize
data class OurSpeakerItem(
    val imageResId: Int,
    val name: String,
    val position: String,
    val company: String
    ) : Parcelable
