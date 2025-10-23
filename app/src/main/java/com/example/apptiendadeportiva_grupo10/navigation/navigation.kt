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
import com.example.apptiendadeportiva_grupo10.ui.screens.CarritoScreen
import com.example.apptiendadeportiva_grupo10.viewmodel.AuthViewModel
import com.example.apptiendadeportiva_grupo10.viewmodel.CatalogoViewModel
import com.example.apptiendadeportiva_grupo10.viewmodel.CarritoViewModel

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    carritoViewModel: CarritoViewModel
) {
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
            // CatalogoViewModel se crea con el ciclo de vida del composable
            val catalogoViewModel: CatalogoViewModel = viewModel()

            CatalogoScreen(navController = navController, viewModel = catalogoViewModel)
        }

        composable(route = "detalle/{productoId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("productoId")?.toIntOrNull() ?: 0

            DetalleScreen(
                navController = navController,
                idProducto = id,
                authViewModel = authViewModel,
                carritoAuthViewModel = carritoViewModel
            )
        }

        composable(route = "Home") {
            HomeScreen(navController, authViewModel)
        }

        composable(route = "carrito") {
            CarritoScreen(
                navController = navController,
                viewModel = carritoViewModel
            )
        }
    }
}

@Composable
fun DetalleScreen(
    navController: NavHostController,
    idProducto: Int,
    authViewModel: AuthViewModel,
    carritoAuthViewModel: CarritoViewModel // ✅ Nombre del parámetro en DetalleScreen
) {
    TODO("Implementación de DetalleScreen, incluyendo la lógica de 'Agregar al carrito' usando carritoAuthViewModel")
}
