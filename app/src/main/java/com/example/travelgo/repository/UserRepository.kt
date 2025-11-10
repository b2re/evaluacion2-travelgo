package com.example.travelgo.repository

import android.content.Context
import com.example.travelgo.data.remote.ApiService
import com.example.travelgo.data.remote.RetrofitClient
import com.example.travelgo.data.remote.dto.UserDto

class UserRepository(context: Context) {

    private val api: ApiService = RetrofitClient
        .build(context)
        .create(ApiService::class.java)

    /**
     * Obtiene el usuario autenticado desde /auth/me
     */
    suspend fun fetchUser(): Result<UserDto> = runCatching {
        api.me()
    }
}
