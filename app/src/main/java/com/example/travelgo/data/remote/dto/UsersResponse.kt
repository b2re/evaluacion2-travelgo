package com.example.travelgo.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UsersResponse(
    @SerializedName("users") val users: List<UserDto> = emptyList(),
    @SerializedName("total") val total: Int? = null,
    @SerializedName("skip")  val skip: Int? = null,
    @SerializedName("limit") val limit: Int? = null
)
