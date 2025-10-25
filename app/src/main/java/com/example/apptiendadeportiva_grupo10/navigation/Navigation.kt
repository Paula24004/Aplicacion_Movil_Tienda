package com.example.apptiendadeportiva_grupo10.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.apptiendadeportiva_grupo10.ui.screens.LoginScreen
import com.example.apptiendadeportiva_grupo10.ui.screens.RegisterScreen
import com.example.apptiendadeportiva_grupo10.ui.screens.CatalogoScreen
import com.example.apptiendadeportiva_grupo10.ui.screens.CarritoScreen
import com.example.apptiendadeportiva_grupo10.ui.screens.DetalleProductoScreen
import com.example.apptiendadeportiva_grupo10.ui.screens.HomeScreen

import com.example.apptiendadeportiva_grupo10.ui.screens.LoginAdmin
import com.example.apptiendadeportiva_grupo10.ui.screens.RegistroAdmin
import com.example.apptiendadeportiva_grupo10.ui.screens.HomeAdmin

import com.example.apptiendadeportiva_grupo10.viewmodel.AuthViewModel
import com.example.apptiendadeportiva_grupo10.viewmodel.CatalogoViewModel
import com.example.apptiendadeportiva_grupo10.viewmodel.CarritoViewModel

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    carritoViewModel: CarritoViewModel
) {
    val navController = rememberNavController()
    val catalogoViewModel: CatalogoViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "iniciar_sesion"
    ) {

        composable("iniciar_sesion") {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("catalogo") {
                        popUpTo("iniciar_sesion") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("registrarse")
                }
            )
        }

        composable("registrarse") {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate("catalogo") {
                        popUpTo("registrarse") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate("iniciar_sesion") {
                        popUpTo("registrarse") { inclusive = true }
                    }
                }
            )
        }

        composable("catalogo") {
            CatalogoScreen(
                navController = navController,
                viewModel = catalogoViewModel
            )
        }

        composable(
            route = "detalle/{idProducto}",
            arguments = listOf(navArgument("idProducto") { type = NavType.IntType })
        ) { entry ->
            val idProducto = entry.arguments?.getInt("idProducto") ?: 0
            DetalleProductoScreen(
                productoId = idProducto,
                catalogoViewModel = catalogoViewModel,
                carritoViewModel = carritoViewModel
            )
        }

        composable("carrito") {
            CarritoScreen(
                navController = navController,
                viewModel = carritoViewModel
            )
        }

        composable("admin_iniciar") {
            LoginAdmin(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("admin_panel") {
                        popUpTo("admin_iniciar") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("admin_registrar")
                }
            )
        }

        composable("admin_registrar") {
            RegistroAdmin(
                viewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate("admin_iniciar") {
                        popUpTo("admin_registrar") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate("admin_iniciar") {
                        popUpTo("admin_registrar") { inclusive = true }
                    }
                }
            )
        }

        composable("admin_panel") {
            HomeAdmin(
                viewModel = authViewModel,
                onLogout = {
                    authViewModel.loginAdmin()
                    navController.navigate("admin_iniciar") {
                        popUpTo("admin_panel") { inclusive = true }
                    }
                }
            )
        }
    }
}
