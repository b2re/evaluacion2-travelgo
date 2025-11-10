
package com.example.travelgo.data.remote.dto

// Xano suele aceptar email+password o username+password. Usamos email.
data class LoginRequest(
    val email: String,
    val password: String
)
