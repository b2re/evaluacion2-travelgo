package com.example.travelgo.ui.screens

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ⬅️ AJUSTA este import al paquete REAL donde está tu ProfileUiState
import com.example.travelgo.viewmodel.ProfileUiState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ProfileContent(
    uiState: ProfileUiState,
    onRefresh: () -> Unit = {}
) {
    val context = LocalContext.current

    // Avatar local para no depender del ViewModel (evita "unresolved" en updateAvatar/avatarUri)
    var avatarUri by remember { mutableStateOf<Uri?>(null) }
    var showPicker by remember { mutableStateOf(false) }

    // Permisos según versión
    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        listOf(Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        listOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
    }
    val permissionsState = rememberMultiplePermissionsState(permissions)

    // Launchers
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) {
        // si la cámara devuelve OK, la URI ya estaba en avatarUri
        // (no llamamos a ViewModel para evitar referencias inexistentes)
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) avatarUri = uri
    }

    // Diálogo simple (reemplaza ImagePickerDialog)
    if (showPicker) {
        AlertDialog(
            onDismissRequest = { showPicker = false },
            title = { Text("Seleccionar imagen") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Elige cómo quieres seleccionar tu imagen.")
                    Button(
                        onClick = {
                            showPicker = false
                            val allGranted = permissionsState.permissions.all {
                                it.status is PermissionStatus.Granted
                            }
                            if (!allGranted) {
                                permissionsState.launchMultiplePermissionRequest()
                            } else {
                                val tmp = createImageUri(context)
                                avatarUri = tmp
                                tmp?.let { cameraLauncher.launch(it) }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Tomar foto") }

                    OutlinedButton(
                        onClick = {
                            showPicker = false
                            val allGranted = permissionsState.permissions.all {
                                it.status is PermissionStatus.Granted
                            }
                            if (!allGranted) {
                                permissionsState.launchMultiplePermissionRequest()
                            } else {
                                galleryLauncher.launch("image/*")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Elegir de galería") }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPicker = false }) { Text("Cerrar") }
            }
        )
    }

    // === UI ===
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.size(120.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    if (avatarUri != null) {
                        AsyncImage(
                            model = avatarUri,
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .clickable { showPicker = true }
                                .background(MaterialTheme.colorScheme.primary),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable { showPicker = true },
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = "Seleccionar avatar",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(28.dp)
                            )
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .size(36.dp)
                            .clickable { showPicker = true },
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surface,
                        shadowElevation = 2.dp
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CameraAlt,
                            contentDescription = "Cambiar foto",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Nombre (robusto ante cambios de nombres en el estado)
                val displayName = uiState.tryGetString("userName")
                    ?: uiState.tryGetString("name")
                    ?: ""
                Text(
                    text = displayName,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(Modifier.height(4.dp))

                // Email (robusto ante cambios)
                val displayEmail = uiState.tryGetString("userEmail")
                    ?: uiState.tryGetString("email")
                    ?: ""
                Text(
                    text = displayEmail,
                    style = MaterialTheme.typography.bodyMedium
                )

                // Error (si tu estado lo trae)
                val errorText = uiState.tryGetString("error")
                if (!errorText.isNullOrBlank()) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = errorText,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(Modifier.height(16.dp))
                OutlinedButton(onClick = onRefresh) { Text("Refrescar") }
            }
        }
    }
}

/** Crea una Uri temporal para la cámara usando FileProvider */
fun createImageUri(context: Context): Uri? {
    val imageFile = File(
        context.cacheDir,
        "photo_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.jpg"
    )
    return androidx.core.content.FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        imageFile
    )
}

/** Helper: intenta leer un String por nombre de campo, sin romper si el modelo cambia */
private fun Any.tryGetString(field: String): String? = try {
    val f = this::class.java.getDeclaredField(field)
    f.isAccessible = true
    (f.get(this) as? String)
} catch (_: Throwable) { null }
