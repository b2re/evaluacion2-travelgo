
package com.example.travelgo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.travelgo.repository.TravelRepository
import com.example.travelgo.data.remote.dto.PackageDto
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext

@Composable
fun HomeScreen(navController: NavController) {
    val ctx = LocalContext.current
    val repo = remember { TravelRepository(ctx) }
    var list by remember { mutableStateOf<List<PackageDto>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            val res = repo.packages()
            loading = false
            res.onSuccess { list = it }
               .onFailure { error = it.message ?: "Fallo al cargar paquetes" }
        }
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("TravelGo • Paquetes", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))

        when {
            loading -> { CircularProgressIndicator() }
            error != null -> { Text("⚠️ " + error!!, color = MaterialTheme.colorScheme.error) }
            else -> {
                LazyColumn {
                    items(list) { p ->
                        Card(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                            Column(Modifier.padding(12.dp)) {
                                Text(p.title ?: "Paquete ${p.id}", style = MaterialTheme.typography.titleMedium)
                                if (!p.description.isNullOrBlank()) {
                                    Spacer(Modifier.height(4.dp)); Text(p.description!!)
                                }
                                if (p.price != null) {
                                    Spacer(Modifier.height(4.dp)); Text("Precio: $${p.price}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
