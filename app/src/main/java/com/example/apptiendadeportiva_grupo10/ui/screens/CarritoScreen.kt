package com.example.apptiendadeportiva_grupo10.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.apptiendadeportiva_grupo10.viewmodel.CarritoViewModel
import com.example.apptiendadeportiva_grupo10.viewmodel.AuthViewModel
import java.text.NumberFormat
import java.util.Locale
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    navController: NavController,
    viewModel: CarritoViewModel,
    authViewModel: AuthViewModel
) {
    val items by viewModel.items.collectAsState()
    val total by viewModel.total.collectAsState()
    val formato = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF8A2BE2) // Morado
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("catalogo") {
                            popUpTo("carrito") { inclusive = true }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (items.isNotEmpty()) {
                Surface(tonalElevation = 2.dp) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total: ${formato.format(total)}")

                        Button(
                            onClick = {
                                if (!authViewModel.isLoggedIn) {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Debes iniciar sesión para realizar el pago",
                                            actionLabel = "OK",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                    navController.navigate("login")
                                    return@Button
                                }

                                viewModel.vaciar()
                                navController.navigate("compra_exitosa")


                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "¡Pago realizado con éxito!",
                                        actionLabel = "OK",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF8A2BE2), // Morado
                                contentColor = Color.White
                            )
                        ) {
                            Text("Pagar")
                        }
                    }
                }
            }
        }
    ) { padding ->

        if (items.isEmpty()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Tu carrito está vacío")
            }
        } else {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items) { it ->
                    ElevatedCard(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(12.dp)) {
                            Text(it.producto.nombre)
                            Spacer(Modifier.height(4.dp))
                            Text("Talla: ${it.talla}  •  Cantidad: ${it.cantidad}")
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Subtotal: ${formato.format(it.cantidad * (it.producto.precio ?: 0.0))}",
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(onClick = {
                                    val nueva = (it.cantidad - 1).coerceAtLeast(0)
                                    if (nueva == 0) viewModel.quitar(it.producto.id)
                                    else viewModel.cambiarCantidad(it.producto.id, nueva)
                                }) { Text("-") }

                                OutlinedButton(onClick = {
                                    viewModel.cambiarCantidad(it.producto.id, it.cantidad + 1)
                                }) { Text("+") }

                                Spacer(Modifier.weight(1f))

                                TextButton(onClick = {
                                    viewModel.quitar(it.producto.id)
                                }) { Text("Quitar") }
                            }
                        }
                    }
                }
            }
        }
    }
}
