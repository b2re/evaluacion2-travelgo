package com.example.travelgo.ui.profile

import android.app.Application
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelgo.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val userEmail: String = "",
    val error: String? = null,
    // ⭐ Nuevo: URI de la imagen de perfil (galería o cámara)
    val avatarUri: Uri? = null
)

class ProfileViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = UserRepository(app)

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    // ⭐ Interno: guardamos el URI donde la cámara escribirá la foto
    private var pendingCameraUri: Uri? = null

    // ---------------------------
    // CARGA DE USUARIO (tu código)
    // ---------------------------
    fun loadUser() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            repo.fetchUser().fold(
                onSuccess = { user ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        userName = user.name ?: "Sin nombre",
                        userEmail = user.email ?: "Sin email",
                        error = null
                    )
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Error desconocido"
                    )
                }
            )
        }
    }

    // ----------------------------------------
    // 📸 Cámara: crear URI y manejar el resultado
    // ----------------------------------------

    /**
     * Crea (y guarda internamente) un URI en MediaStore donde la cámara
     * guardará la foto. No requiere FileProvider ni editar el Manifest.
     *
     * Úsalo así desde tu UI:
     * val uri = vm.createCameraImageUri()
     * cameraLauncher.launch(uri)
     */
    fun createCameraImageUri(): Uri? {
        val resolver = getApplication<Application>().contentResolver
        val contentValues = ContentValues().apply {
            put(
                MediaStore.Images.Media.DISPLAY_NAME,
                "avatar_${System.currentTimeMillis()}.jpg"
            )
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }

        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        pendingCameraUri = resolver.insert(collection, contentValues)

        // Marcar como no-pending en Q+ para que sea visible
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && pendingCameraUri != null) {
            contentValues.clear()
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
            resolver.update(pendingCameraUri!!, contentValues, null, null)
        }

        return pendingCameraUri
    }

    /**
     * Llama esto en el callback de ActivityResultContracts.TakePicture().
     * Si success = true, movemos ese URI al estado como avatar.
     */
    fun onCameraResult(success: Boolean) {
        if (success && pendingCameraUri != null) {
            _uiState.value = _uiState.value.copy(avatarUri = pendingCameraUri)
        }
        // Limpia el pending para el próximo uso
        pendingCameraUri = null
    }

    // ----------------------------------------
    // 🖼️ Galería: guardar el URI seleccionado
    // ----------------------------------------
    /**
     * Llama esto en el callback de ActivityResultContracts.GetContent().
     */
    fun onGalleryPicked(uri: Uri?) {
        if (uri != null) {
            _uiState.value = _uiState.value.copy(avatarUri = uri)
        }
    }

    // ----------------------------------------
    // 🔐 Permisos requeridos según la versión
    // ----------------------------------------
    /**
     * Devuelve la lista de permisos que deberías solicitar en la UI.
     * - Android 13+ : CAMERA + READ_MEDIA_IMAGES
     * - Android 12- : CAMERA + READ_EXTERNAL_STORAGE
     */
    fun requiredPermissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_MEDIA_IMAGES
            )
        } else {
            arrayOf(
                android.Manifest.permission.CAMERA,
                @Suppress("DEPRECATION")
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }
}
