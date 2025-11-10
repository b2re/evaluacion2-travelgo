package com.example.travelgo.data.remote

import com.example.travelgo.data.remote.dto.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * API contract para TravelGo.
 * Ajusta las rutas si tu backend usa otras.
 */
interface ApiService {

    // ---------- AUTH ----------
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): LoginResponse

    // Usuario autenticado (perfil)
    @GET("auth/me")
    suspend fun me(): UserDto

    @POST("auth/signup")
    suspend fun signup(@Body body: Map<String, String>): LoginResponse

    // ---------- TRAVEL (paquetes / reservas) ----------
    // En algunos backends la lista puede vivir en /package o /packages.
    // Probamos ambos: primero singular (lista plana) y luego plural (objeto con 'list').
    @GET("package")
    suspend fun listPackagesSingular(): List<PackageDto>

    @GET("packages")
    suspend fun listPackagesPlural(): PackagesResponse

    @GET("package/{id}")
    suspend fun getPackage(@Path("id") id: Int): PackageDto

    @POST("reservation")
    suspend fun createReservation(@Body body: ReservationRequest): ReservationResponse
}
