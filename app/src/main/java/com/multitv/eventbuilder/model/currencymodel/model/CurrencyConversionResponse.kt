package com.multitv.eventbuilder.model.currencymodel.model



import com.google.gson.annotations.SerializedName

data class CurrencyConversionResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("from_currency")
    val fromCurrency: String,

    @SerializedName("to_currency")
    val toCurrency: String,

    @SerializedName("amount")
    val amount: Double,

    @SerializedName("converted_amount")
    val convertedAmount: Double,

    @SerializedName("rate")
    val rate: Double,

    @SerializedName("date")
    val date: String
)
