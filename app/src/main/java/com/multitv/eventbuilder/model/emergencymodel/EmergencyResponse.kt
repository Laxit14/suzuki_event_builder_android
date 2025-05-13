package com.multitv.eventbuilder.model.emergencymodel

import com.google.gson.annotations.SerializedName

data class EmergencyContact(
    val name: String,
    val phoneNumber: String
)
data class EmergencyData(
    val emerCont: List<EmergencyContact>
)

data class EmergencyResponse(
    val message: String,
    @SerializedName("page_tittle") val pageTittle : String,
    val success: Boolean,
    val data: EmergencyData
)

