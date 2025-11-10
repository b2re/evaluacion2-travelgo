
package com.example.travelgo.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("authToken") val authToken: String?, // nombre típico en Xano
    @SerializedName("token") val token: String?,         // por si el campo se llama 'token'
    @SerializedName("user") val user: UserDto?           // algunos devuelven el usuario
) {
    fun resolveToken(): String? = authToken ?: token
}
