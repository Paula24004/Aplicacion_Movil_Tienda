package com.example.apptiendadeportiva_grupo10.navigation


import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.apptiendadeportiva_grupo10.ui.screen.*
import com.example.apptiendadeportiva_grupo10.ui.screenspackage.RegisterScreen
import com.example.apptiendadeportiva_grupo10.viewmodel.AuthViewModel


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val AuthViewModel: AuthViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "Home"
    ) {
        composable(route = "login") {
            LoginScreen (
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
            productosScreen(navController, AuthViewModel)
        }
        composable(route = "Home") {
            HomeScreen(navController, AuthViewModel)
        }
    }
}
