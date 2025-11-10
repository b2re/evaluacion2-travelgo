package com.example.travelgo.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.travelgo.ui.profile.ProfileViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    // ---- Permisos (usa los que define tu ViewModel según la versión) ----
    val permissionsState = rememberMultiplePermissionsState(
        permissions = viewModel.requiredPermissions().toList()
    )

    // ---- Launchers ----
    // Galería -> devuelve Uri?
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.onGalleryPicked(uri)
    }

    // Cámara -> requiere Uri de destino y devuelve success:Boolean
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        viewModel.onCameraResult(success)
    }

    // URI preparado para la cámara
    var preparedCameraUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(Unit) { viewModel.loadUser() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            state.error != null -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "✖ Error",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = state.error!!,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { viewModel.loadUser() }) {
                        Text("Reintentar")
                    }
                }
            }

            else -> {
                Column(
                    modifier = Modifier.align(Alignment.TopCenter),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Perfil de Usuario",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    // --------- Avatar ---------
                    Card {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                model = state.avatarUri,
                                contentDescription = "Avatar",
                                modifier = Modifier.size(120.dp)
                            )
                            if (state.avatarUri == null) {
                                Spacer(Modifier.height(6.dp))
                                Text(
                                    text = "Sin foto de perfil",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(Modifier.height(12.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Button(onClick = {
                                    if (!permissionsState.allPermissionsGranted) {
                                        permissionsState.launchMultiplePermissionRequest()
                                    } else {
                                        preparedCameraUri = viewModel.createCameraImageUri()
                                        // ✅ FIX: solo lanza si no es null
                                        preparedCameraUri?.let { cameraLauncher.launch(it) }
                                    }
                                }) {
                                    Text("Tomar foto")
                                }

                                OutlinedButton(onClick = {
                                    if (!permissionsState.allPermissionsGranted) {
                                        permissionsState.launchMultiplePermissionRequest()
                                    } else {
                                        galleryLauncher.launch("image/*")
                                    }
                                }) {
                                    Text("Elegir de galería")
                                }
                            }
                        }
                    }

                    // --------- Datos: nombre ---------
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Nombre",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = state.userName,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    // --------- Datos: email ---------
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Email",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = state.userEmail,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { viewModel.loadUser() }) {
                        Text("Refrescar")
                    }
                }
            }
        }
    }
}
