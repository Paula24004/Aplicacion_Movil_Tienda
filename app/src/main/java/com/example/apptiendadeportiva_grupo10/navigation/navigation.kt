package com.example.apptiendadeportiva_grupo10.navigation


import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.apptiendadeportiva_grupo10.ui.screen.*
import com.example.apptiendadeportiva_grupo10.ui.screenspackage.RegisterScreen


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val authViewModel: AuthViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "Home"
    ) {
        composable(route = "login") {
            LoginScreen (
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("productos"){
                        popUpTo("login") {inclusive = true}
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }
        composable(route = "register") {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate("productos") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }

        composable(route = "productos") {
            productosScreen(navController, authViewModel)
        }
        composable(route = "Home") {
            HomeScreen(navController, authViewModel)
        }
    }
}
