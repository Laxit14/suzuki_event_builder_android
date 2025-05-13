package com.multitv.eventbuilder.ui.dialog_fragment.postnotes.model

import com.google.gson.annotations.SerializedName

data class PostNotesResponse(
    @SerializedName("message") val message : String,
    @SerializedName("status") val status : String

)
