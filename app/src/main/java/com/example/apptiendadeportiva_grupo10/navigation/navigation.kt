package com.example.apptiendadeportiva_grupo10.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.apptiendadeportiva_grupo10.ui.screen.LoginScreen
import com.example.apptiendadeportiva_grupo10.ui.screen.HomeScreen
import com.example.apptiendadeportiva_grupo10.ui.screen.CatalogoScreen
import com.example.apptiendadeportiva_grupo10.ui.screenspackage.RegisterScreen
import com.example.apptiendadeportiva_grupo10.ui.screens.CarritoScreen
import com.example.apptiendadeportiva_grupo10.ui.screens.DetalleProductoScreen // Importar la pantalla de detalle
import com.example.apptiendadeportiva_grupo10.viewmodel.AuthViewModel
import com.example.apptiendadeportiva_grupo10.viewmodel.CatalogoViewModel
import com.example.apptiendadeportiva_grupo10.viewmodel.CarritoViewModel

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    carritoViewModel: CarritoViewModel
) {
    val navController = rememberNavController()

    // 1. Crear una única instancia de CatalogoViewModel para todo el NavHost
    // Esto es crucial para que la pantalla de detalle acceda a la lista de productos cargada.
    val catalogoViewModel: CatalogoViewModel = viewModel()

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
            // Se usa la instancia de CatalogoViewModel creada arriba
            CatalogoScreen(navController = navController, viewModel = catalogoViewModel)
        }

        // --- RUTA DETALLE CORREGIDA ---
        composable(
            route = "detalle/{productoId}",
            arguments = listOf(navArgument("productoId") { type = NavType.IntType }) // Importar NavType
        ) { backStackEntry ->
            // Obtener el ID como entero
            val idProducto = backStackEntry.arguments?.getInt("productoId") ?: 0

            DetalleProductoScreen(
                productoId = idProducto,
                catalogoViewModel = catalogoViewModel, // Usamos el VM que tiene la función buscarProductoPorId
                carritoViewModel = carritoViewModel // Pasamos el VM para la acción de agregar al carrito
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