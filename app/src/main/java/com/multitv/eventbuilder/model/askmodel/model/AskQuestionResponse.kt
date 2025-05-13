package com.multitv.eventbuilder.model.askmodel.model

import retrofit2.http.Field

import com.google.gson.annotations.SerializedName
data class AskQuestionResponse(
     @SerializedName("code") val code : Int,
     @SerializedName("result") val result : String

)
