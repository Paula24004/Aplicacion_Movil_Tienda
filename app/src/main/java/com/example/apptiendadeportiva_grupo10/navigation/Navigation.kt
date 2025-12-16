package com.example.apptiendadeportiva_grupo10.navigation

import android.app.Application
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.apptiendadeportiva_grupo10.ui.screens.*
import com.example.apptiendadeportiva_grupo10.viewmodel.*
import com.example.apptiendadeportiva_grupo10.repository.*
import com.example.apptiendadeportiva_grupo10.data.remote.RetrofitClient
import com.example.apptiendadeportiva_grupo10.model.toDomain

@Composable
fun RootScreen() {

    val application = LocalContext.current.applicationContext as Application

    // ----------------------------
    // REPOSITORIES
    // ----------------------------
    val productoRepository = remember { ProductoRepository() }
    val boletaRepository = remember {
        BoletaRepository(RetrofitClient.boletaApi)
    }
    val gestionEnvioRepository = remember {
        GestionEnvioRepository(RetrofitClient.gestionEnvioApi)
    }

    // ----------------------------
    // VIEWMODEL FACTORIES
    // ----------------------------
    val authViewModelFactory = remember {
        AuthViewModelFactory(application, productoRepository)
    }

    // ----------------------------
    // VIEWMODELS
    // ----------------------------
    val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)
    val catalogoViewModel: CatalogoViewModel = viewModel()
    val quoteViewModel: QuoteViewModel = viewModel()

    val carritoViewModel = remember {
        CarritoViewModel(boletaRepository)
    }

    val gestionEnvioViewModel = remember {
        GestionEnvioViewModel(gestionEnvioRepository)
    }

    val navController = rememberNavController()

    // ----------------------------
    // CARGA PRODUCTOS / STOCK
    // ----------------------------
    val productos by catalogoViewModel.productos.collectAsState()

    LaunchedEffect(productos.isNotEmpty()) {
        if (productos.isNotEmpty()) {
            carritoViewModel.initStock(productos.map { it.toDomain() })
        }
    }

    // ----------------------------
    // NAVIGATION
    // ----------------------------
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
                navController = navController,
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
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable("catalogo") {
            CatalogoScreen(
                navController = navController,
                viewModel = catalogoViewModel,
                carritoViewModel = carritoViewModel,
                authViewModel = authViewModel
            )
        }

        composable(
            route = "detalle/{idProducto}",
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

        composable("carrito") {
            CarritoScreen(
                navController = navController,
                viewModel = carritoViewModel,
                authViewModel = authViewModel
            )
        }

        composable("compra_exitosa/{total}") { backStackEntry ->
            val totalArg =
                backStackEntry.arguments?.getString("total")?.toDouble() ?: 0.0

            CompraExitosaScreen(
                navController = navController,
                authViewModel = authViewModel,
                carritoViewModel = carritoViewModel,
                totalRecibido = totalArg
            )
        }

        // ✅ CORRECCIÓN: proceso_envio con dirección + total
        composable(
            route = "proceso_envio/{direccion}/{total}",
            arguments = listOf(
                navArgument("direccion") { type = NavType.StringType },
                navArgument("total") { type = NavType.FloatType }
            )
        ) { backStackEntry ->

            val direccion = backStackEntry.arguments?.getString("direccion") ?: ""
            val total = backStackEntry.arguments?.getFloat("total")?.toDouble() ?: 0.0

            ProcesoEnvioScreen(
                navController = navController,
                viewModel = gestionEnvioViewModel,
                direccionDespacho = direccion,
                total = total
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

        composable(
            route = "pedido_enviado/{agencia}/{fecha}/{direccion}/{total}",
            arguments = listOf(
                navArgument("agencia") { type = NavType.StringType },
                navArgument("fecha") { type = NavType.StringType },
                navArgument("direccion") { type = NavType.StringType },
                navArgument("total") { type = NavType.FloatType }
            )
        ) { backStackEntry ->

            PedidoEnviadoScreen(
                navController = navController,
                agencia = backStackEntry.arguments?.getString("agencia") ?: "",
                fecha = backStackEntry.arguments?.getString("fecha") ?: "",
                direccion = backStackEntry.arguments?.getString("direccion") ?: "",
                total = backStackEntry.arguments?.getFloat("total")?.toDouble() ?: 0.0
            )
        }

        composable("perfil") {
            ProfileScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }
    }

}
