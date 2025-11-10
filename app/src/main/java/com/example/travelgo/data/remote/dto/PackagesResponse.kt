package com.example.travelgo.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Para el fallback que usas: api.listPackagesPlural().list
 * Si tu backend usa otro nombre (p. ej. "data" o "packages"),
 * cambia el SerializedName de abajo.
 */
data class PackagesResponse(
    @SerializedName("list")
    val list: List<PackageDto> = emptyList()
)
