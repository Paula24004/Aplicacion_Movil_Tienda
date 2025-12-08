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
import com.example.apptiendadeportiva_grupo10.viewmodel.AuthViewModelFactory
import com.example.apptiendadeportiva_grupo10.viewmodel.CatalogoViewModel
import com.example.apptiendadeportiva_grupo10.viewmodel.CarritoViewModel
import com.example.apptiendadeportiva_grupo10.viewmodel.QuoteViewModel
import com.example.apptiendadeportiva_grupo10.model.toDomain
import com.example.apptiendadeportiva_grupo10.repository.ProductoRepository
import com.example.apptiendadeportiva_grupo10.ui.screens.CompraExitosaScreen
import com.example.apptiendadeportiva_grupo10.ui.screens.EditarDireccionScreen
@Composable
fun RootScreen() {
    // Obtener la instancia de Application (Necesaria para AuthViewModel y Room)
    val application = LocalContext.current.applicationContext as Application

    // Instanciar Repositorios (
    val productoRepository = remember { ProductoRepository() } // Asume constructor vacío

    // 2. Crear las Factories para los ViewModels que tienen dependencias (AuthViewModel)
    val authViewModelFactory = remember {
        AuthViewModelFactory(application, productoRepository)
    }

    // ViewModels compartidos (Instanciación usando Factory)
    val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory) // ¡AQUÍ EL CAMBIO!

    // ViewModels sin dependencias pueden usar la función por defecto
    val quoteViewModel: QuoteViewModel = viewModel()
    val catalogoViewModel: CatalogoViewModel = viewModel()
    val carritoViewModel: CarritoViewModel = viewModel()


    // Controlador de navegación
    val navController = rememberNavController()

    // Productos cargados (AÚN SON ProductoEntity)
    val productos by catalogoViewModel.productos.collectAsState()





// Solo inicializa cuando hay productos por primera vez.
// Gracias al guard interno, no se “repite” ni borra nada luego.
    LaunchedEffect(productos.isNotEmpty()) {
        if (productos.isNotEmpty()) {
            // FIX: Aplicamos .map { it.toProducto() } para convertir de List<ProductoEntity> a List<Producto>
            carritoViewModel.initStock(productos.map { it.toDomain() })
        }
    }

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                navController = navController,
                viewModel = authViewModel,
                onNavigateToLogin = { navController.navigate("iniciar_sesion") },
                onNavigateToAdmin = { navController.navigate("admin_iniciar") },
                onNavigateToCatalogo = { navController.navigate("catalogo") }
            )
        }

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

        composable("registrarse") {
            RegisterScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable("catalogo") {
            CatalogoScreen(
                navController = navController,
                viewModel = catalogoViewModel,
                carritoViewModel = carritoViewModel
            )
        }

        composable(
            "detalle/{idProducto}",
            arguments = listOf(navArgument("idProducto") { type = NavType.IntType })
        ) { entry ->
            val idProducto = entry.arguments?.getInt("idProducto") ?: 0
            DetalleProductoScreen(
                navController = navController,
                viewModel = catalogoViewModel,
                carritoViewModel = carritoViewModel,
                productoId = idProducto
            )
        }

        composable("carrito") {
            CarritoScreen(
                navController = navController,
                viewModel = carritoViewModel,
                authViewModel = authViewModel
            )
        }

        composable("admin_iniciar") {
            LoginAdmin(
                navController = navController,
                viewModel = authViewModel,
                onNavigateToRegister = { navController.navigate("admin_registrar") }
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
        composable("compra_exitosa") {
            CompraExitosaScreen(
                navController = navController,
                authViewModel = authViewModel,
                carritoViewModel = carritoViewModel
            )
        }

        composable("editar_direccion") {
            EditarDireccionScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable("frases") {
            FraseScreen(viewModel = quoteViewModel)
        }

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