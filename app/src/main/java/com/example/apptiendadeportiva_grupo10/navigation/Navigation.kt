package com.example.apptiendadeportiva_grupo10.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.apptiendadeportiva_grupo10.ui.screens.*
import com.example.apptiendadeportiva_grupo10.viewmodel.*
import com.example.apptiendadeportiva_grupo10.repository.ProductoRepository
import com.example.apptiendadeportiva_grupo10.model.toDomain

@Composable
fun RootScreen() {

    val application = LocalContext.current.applicationContext as Application

    // Repositorios y factories
    val productoRepository = remember { ProductoRepository() }
    val authViewModelFactory = remember { AuthViewModelFactory(application, productoRepository) }

    // ViewModels compartidos
    val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)
    val catalogoViewModel: CatalogoViewModel = viewModel()
    val carritoViewModel: CarritoViewModel = viewModel()
    val quoteViewModel: QuoteViewModel = viewModel()

    val navController = rememberNavController()

    val productos by catalogoViewModel.productos.collectAsState()

    // Inicializa stock en carrito cuando catálogo se cargue
    LaunchedEffect(productos.isNotEmpty()) {
        if (productos.isNotEmpty()) {
            carritoViewModel.initStock(productos.map { it.toDomain() })
        }
    }

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        // HOME
        composable("home") {
            HomeScreen(
                navController = navController,
                viewModel = authViewModel,
                onNavigateToLogin = { navController.navigate("iniciar_sesion") },
                onNavigateToAdmin = { navController.navigate("admin_iniciar") },
                onNavigateToCatalogo = { navController.navigate("catalogo") }
            )
        }

        // LOGIN
        composable("iniciar_sesion") {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("catalogo") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate("registrarse") }
            )
        }

        // REGISTRO
        composable("registrarse") {
            RegisterScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        // CATÁLOGO
        composable("catalogo") {
            CatalogoScreen(
                navController = navController,
                viewModel = catalogoViewModel,
                carritoViewModel = carritoViewModel
            )
        }

        // DETALLE PRODUCTO
        composable(
            "detalle/{idProducto}",
            arguments = listOf(navArgument("idProducto") { type = NavType.IntType })
        ) { entry ->
            val idProducto = entry.arguments?.getInt("idProducto") ?: 0
            DetalleProductoScreen(
                productoId = idProducto,
                viewModel = catalogoViewModel,
                carritoViewModel = carritoViewModel,
                navController = navController
            )
        }

        // CARRITO
        composable("carrito") {
            CarritoScreen(
                navController = navController,
                viewModel = carritoViewModel,
                authViewModel = authViewModel
            )
        }

        // COMPRA EXITOSA (SIN ARGUMENTOS)
        composable("compra_exitosa") {
            CompraExitosaScreen(
                navController = navController,
                authViewModel = authViewModel,
                carritoViewModel = carritoViewModel
            )
        }

        // EDITAR DIRECCIÓN
        composable("editar_direccion") {
            EditarDireccionScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        // FRASES
        composable("frases") {
            FraseScreen(viewModel = quoteViewModel)
        }

        // ADMIN LOGIN
        composable("admin_iniciar") {
            LoginAdmin(
                navController = navController,
                viewModel = authViewModel,
                onNavigateToRegister = { navController.navigate("admin_registrar") }
            )
        }

        // ADMIN REGISTRO
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

        // PANEL ADMIN
        composable("admin_panel") {
            HomeAdmin(
                viewModel = authViewModel,
                onLogout = {
                    navController.navigate("admin_iniciar") {
                        popUpTo("admin_panel") { inclusive = true }
                    }
                }
            )
        }
    }
}
