package com.example.travelgo.ui.navegation  // usa el mismo paquete que ya tienes

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.travelgo.ui.screens.HomeScreen
import com.example.travelgo.ui.screens.LoginScreen
import com.example.travelgo.ui.screens.ProfileScreen

/**
 * Define la navegación de la app.
 */
@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = "login" // pantalla inicial
    ) {
        composable(route = "login") {
            LoginScreen(navController)
        }
        composable(route = "home") {
            HomeScreen(navController)
        }
        composable(route = "profile") {
            // ProfileScreen NO recibe NavController; solo su ViewModel interno
            ProfileScreen()
        }
    }
}
