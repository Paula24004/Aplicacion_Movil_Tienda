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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                    containerColor = Color(0xFF8A2BE2)
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("catalogo") {
                            popUpTo("carrito") { inclusive = true }
                        }
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                }
            )
        },

        snackbarHost = { SnackbarHost(snackbarHostState) },

        bottomBar = {
            if (items.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Button(
                        onClick = {
                            if (!authViewModel.isLoggedIn) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Debes iniciar sesión para pagar")
                                }
                                navController.navigate("iniciar_sesion")
                                return@Button
                            }

                            val totalCompra = total
                            navController.navigate("compra_exitosa/$totalCompra")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF8A2BE2),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Pagar", fontSize = 18.sp)
                    }

                    OutlinedButton(
                        onClick = {
                            navController.navigate("catalogo") {
                                popUpTo("carrito") { inclusive = true }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF8A2BE2)
                        )
                    ) {
                        Text("Volver", fontSize = 18.sp)
                    }
                }
            }
        }
    ) { padding ->

        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Tu carrito está vacío")
            }
        } else {

            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                items(items) { item ->

                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        tonalElevation = 2.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(20.dp)) {

                            Text(
                                item.producto.nombre,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.height(10.dp))

                            Text(
                                "Talla: ${item.talla}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.height(6.dp))

                            Text(
                                "Cantidad: ${item.cantidad}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.height(6.dp))

                            Text(
                                "Subtotal: ${formato.format(item.cantidad * (item.producto.precio ?: 0.0))}",
                                color = Color(0xFF673AB7),
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.height(20.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {

                                    Button(
                                        onClick = {
                                            val nueva = item.cantidad - 1
                                            if (nueva <= 0) viewModel.quitar(item.producto.id)
                                            else viewModel.cambiarCantidad(item.producto.id, nueva)
                                        },
                                        modifier = Modifier.size(width = 54.dp, height = 45.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF5E35B1),
                                            contentColor = Color.White
                                        )
                                    ) {
                                        Text("-", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                                    }

                                    Button(
                                        onClick = {
                                            viewModel.cambiarCantidad(item.producto.id, item.cantidad + 1)
                                        },
                                        modifier = Modifier.size(width = 54.dp, height = 45.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF5E35B1),
                                            contentColor = Color.White
                                        )
                                    ) {
                                        Text("+", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                TextButton(
                                    onClick = { viewModel.quitar(item.producto.id) }
                                ) {
                                    Text(
                                        "Quitar",
                                        fontSize = 18.sp,
                                        color = Color(0xFF5E35B1),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
