package com.example.travelgo.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ReservationRequest(
    @SerializedName("package_id")   val package_id: Int,
    @SerializedName("customer_name")  val customer_name: String,
    @SerializedName("customer_email") val customer_email: String,
    @SerializedName("travelers")      val travelers: Int
)

data class ReservationResponse(
    @SerializedName("reservation_id") val reservation_id: Int? = null,
    @SerializedName("status")         val status: String? = null,
    @SerializedName("message")        val message: String? = null
)
