package com.example.travelgo.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PackageDto(
    val id: Int? = null,
    val title: String? = null,
    val description: String? = null,
    val price: Double? = null,
    @SerializedName("image") val imageUrl: String? = null
)
