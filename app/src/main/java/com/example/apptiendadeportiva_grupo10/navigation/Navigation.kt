package com.example.apptiendadeportiva_grupo10.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.apptiendadeportiva_grupo10.ui.screens.FraseScreen
import com.example.apptiendadeportiva_grupo10.ui.screens.HomeScreen
import com.example.apptiendadeportiva_grupo10.ui.screens.LoginAdmin
import com.example.apptiendadeportiva_grupo10.ui.screens.RegistroAdmin
import com.example.apptiendadeportiva_grupo10.ui.screens.HomeAdmin
import com.example.apptiendadeportiva_grupo10.viewmodel.AuthViewModel
import com.example.apptiendadeportiva_grupo10.viewmodel.CatalogoViewModel
import com.example.apptiendadeportiva_grupo10.viewmodel.CarritoViewModel
import com.example.apptiendadeportiva_grupo10.viewmodel.QuoteViewModel
import com.example.apptiendadeportiva_grupo10.model.toProducto // <-- Importación necesaria del mapper

@Composable
fun RootScreen() {
    val quoteViewModel: QuoteViewModel = viewModel()


    // Controlador de navegación
    val navController = rememberNavController()

    // ViewModels compartidos
    val authViewModel: AuthViewModel = viewModel()
    val catalogoViewModel: CatalogoViewModel = viewModel()
    val carritoViewModel: CarritoViewModel = viewModel()

    // Productos cargados (AÚN SON ProductoEntity)
    val productos by catalogoViewModel.productos.collectAsState()




// Solo inicializa cuando hay productos por primera vez.
// Gracias al guard interno, no se “repite” ni borra nada luego.
    LaunchedEffect(productos.isNotEmpty()) {
        if (productos.isNotEmpty()) {
            // FIX: Aplicamos .map { it.toProducto() } para convertir de List<ProductoEntity> a List<Producto>
            carritoViewModel.initStock(productos.map { it.toProducto() })
        }
    }

    // Navegación principal
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        // --- Pantalla principal ---
        composable("home") {
            HomeScreen(
                navController = navController,
                viewModel = authViewModel,
                onNavigateToLogin = { navController.navigate("iniciar_sesion") },
                onNavigateToAdmin = { navController.navigate("admin_iniciar") },
                onNavigateToCatalogo = { navController.navigate("catalogo") }
            )
        }

        // --- Login de usuario ---
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

        // --- Registro de usuario ---
        composable("registrarse") {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate("catalogo") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate("iniciar_sesion") {
                        popUpTo("registrarse") { inclusive = true }
                    }
                }
            )
        }

        // --- Catálogo de productos ---
        composable("catalogo") {
            CatalogoScreen(
                navController = navController,
                viewModel = catalogoViewModel,
                carritoViewModel = carritoViewModel
            )
        }

        // --- Detalle de producto ---
        composable(
            route = "detalle/{idProducto}",
            arguments = listOf(navArgument("idProducto") { type = NavType.IntType })
        ) { entry ->
            val idProducto = entry.arguments?.getInt("idProducto") ?: 0
            DetalleProductoScreen(
                navController = navController,
                productoId = idProducto,
                catalogoViewModel = catalogoViewModel,
                carritoViewModel = carritoViewModel
            )

        }

        // --- Carrito de compras ---
        composable("carrito") {
            CarritoScreen(
                navController = navController,
                viewModel = carritoViewModel
            )
        }

        // --- Login de administrador ---
        composable("admin_iniciar") {
            LoginAdmin(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate("admin_panel") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate("admin_registrar") }
            )
        }

        // --- Registro de administrador ---
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

        composable("frases") {
            FraseScreen(viewModel = quoteViewModel)
        }


        // --- Panel del administrador ---
        composable("admin_panel") {
            HomeAdmin( // Aquí se asegura que solo se pasen los parámetros requeridos
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