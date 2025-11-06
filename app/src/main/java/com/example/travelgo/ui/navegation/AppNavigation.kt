package com.example.travelgo.ui.navegation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.travelgo.ui.screens.ProfileScreen
import com.example.travelgo.ui.screens.LoginScreen
import com.example.travelgo.ui.screens.HomeScreen

/**
 * Define la navegación de la app.
 */
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login" // Pantalla inicial
    ) {
        composable("login") {
            LoginScreen(navController)
        }
        composable("home") {
            HomeScreen(navController)
        }
        composable("profile") {
            ProfileScreen(navController)
        }
    }
}