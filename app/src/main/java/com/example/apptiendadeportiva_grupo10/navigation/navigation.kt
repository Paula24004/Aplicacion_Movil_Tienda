package com.example.apptiendadeportiva_grupo10.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.apptiendadeportiva_grupo10.ui.screen.LoginScreen
import com.example.apptiendadeportiva_grupo10.ui.screen.HomeScreen
import com.example.apptiendadeportiva_grupo10.ui.screen.CatalogoScreen
import com.example.apptiendadeportiva_grupo10.ui.screenspackage.RegisterScreen
import com.example.apptiendadeportiva_grupo10.viewmodel.AuthViewModel
import com.example.apptiendadeportiva_grupo10.viewmodel.CatalogoViewModel

@Composable
fun AppNavigation(authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "Home"
    ) {
        composable(route = "login") {
            LoginScreen (
                viewModel = authViewModel,
                onLoginSuccess = {
                    // Redirigir a la pantalla de catálogo
                    navController.navigate("catalogo"){
                        popUpTo("login") { inclusive = true}
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
                    // Redirigir a la pantalla de catálogo
                    navController.navigate("catalogo") {
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

        composable(route = "catalogo") {
            val catalogoViewModel: CatalogoViewModel = viewModel()

            CatalogoScreen(navController = navController, viewModel = catalogoViewModel)
        }

        composable(route = "detalle/{productoId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("productoId")?.toIntOrNull() ?: 0

            DetalleScreen(navController = navController, idProducto = id, authViewModel = authViewModel)
        }

        composable(route = "Home") {
            HomeScreen(navController, authViewModel)
        }
    }
}

@Composable
fun DetalleScreen(navController: NavHostController, idProducto: Int, authViewModel: AuthViewModel) {
    TODO("Not yet implemented")
}
