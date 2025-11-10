
package com.example.travelgo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.example.travelgo.repository.TravelRepository
import androidx.compose.ui.platform.LocalContext

@Composable
fun LoginScreen(navController: NavController) {
    val ctx = LocalContext.current
    val repo = remember { TravelRepository(ctx) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(Modifier.padding(16.dp)) {
        Text("TravelGo • Iniciar sesión", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, singleLine = true)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, singleLine = true)
        Spacer(Modifier.height(16.dp))
        Button(
            enabled = !loading,
            onClick = {
                loading = true
                error = null
                scope.launch {
                    val res = repo.login(email.trim(), password)
                    loading = false
                    res.onSuccess { navController.navigate("home") { popUpTo("login") { inclusive = true } } }
                       .onFailure { error = it.message ?: "Error de login" }
                }
            }
        ) { Text(if (loading) "Ingresando..." else "Entrar") }

        if (error != null) {
            Spacer(Modifier.height(8.dp))
            Text("⚠️ " + error!!, color = MaterialTheme.colorScheme.error)
        }
    }
}
