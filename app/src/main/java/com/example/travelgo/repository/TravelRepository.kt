package com.example.travelgo.repository

import android.content.Context
import com.example.travelgo.data.local.SessionManager
import com.example.travelgo.data.remote.ApiService
import com.example.travelgo.data.remote.RetrofitClient
import com.example.travelgo.data.remote.dto.*

class TravelRepository(context: Context) {

    private val retrofit = RetrofitClient.build(context)
    private val api: ApiService = retrofit.create(ApiService::class.java)
    private val session = SessionManager(context)

    suspend fun login(email: String, password: String): Result<Unit> = runCatching {
        val res = api.login(LoginRequest(email, password))
        val token = res.token ?: error("La API no devolvió token")
        session.setToken(token)
    }

    suspend fun logout(): Result<Unit> = runCatching { session.clear() }

    suspend fun packages(): Result<List<PackageDto>> = runCatching {
        try {
            api.listPackagesSingular()
        } catch (_: Exception) {
            api.listPackagesPlural().list
        }
    }

    suspend fun reserve(
        packageId: Int,
        name: String,
        email: String,
        travelers: Int
    ): Result<ReservationResponse> = runCatching {
        api.createReservation(
            ReservationRequest(
                package_id = packageId,
                customer_name = name,
                customer_email = email,
                travelers = travelers
            )
        )
    }
}
