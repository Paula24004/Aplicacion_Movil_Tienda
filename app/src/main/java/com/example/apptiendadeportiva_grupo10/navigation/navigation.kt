package com.example.apptiendadeportiva_grupo10.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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
    carritoAuthViewModel: CarritoViewModel
) {
    Column {
        Text("Detalle del Producto ID: $idProducto")

        // Aquí iría la información detallada del producto

        Button(
            // Al hacer clic, llama a la función de tu ViewModel para agregar el producto
            onClick = {
                // 1. Crear un objeto Producto o pasar la ID
                // (Debes tener una función 'agregarProducto' en CarritoViewModel)
                carritoAuthViewModel.agregar(idProducto)

                // Opcional: Navegar al carrito o mostrar un mensaje
                // navController.navigate("carrito")
            }
        ) {
            Text("Agregar al Carrito 🛒")
        }
    }
}
