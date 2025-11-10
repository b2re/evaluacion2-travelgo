package com.example.travelgo.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

// 🔹 Modelo de usuario temporal (solo para compilar este archivo sin dependencias externas)
data class User(
    val id: Int = 0,
    val name: String = "",
    val email: String = ""
)

// 🔹 Estado de la pantalla de perfil
data class ProfileUiState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val error: String? = null,
    val formattedCreatedAt: String = "",
    val avatarUri: Uri? = null // ⭐ Nuevo campo
)

// 🔹 ViewModel del perfil
class ProfileViewModel : ViewModel() {

    // Estado interno mutable
    private val _uiState = MutableStateFlow(ProfileUiState())

    // Estado expuesto solo de lectura
    val uiState: StateFlow<ProfileUiState> = _uiState

    /**
     * Actualiza la URI del avatar del usuario.
     */
    fun updateAvatar(uri: Uri?) {
        _uiState.update { it.copy(avatarUri = uri) }
    }
}
