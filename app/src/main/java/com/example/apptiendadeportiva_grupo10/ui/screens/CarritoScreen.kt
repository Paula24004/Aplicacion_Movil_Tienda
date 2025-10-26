package com.example.apptiendadeportiva_grupo10.ui.screens

import android.R.attr.duration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.apptiendadeportiva_grupo10.viewmodel.CarritoViewModel
import java.text.NumberFormat
import java.util.Locale
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(navController: NavHostController, viewModel: CarritoViewModel) {

    val items by viewModel.items.collectAsState()
    val total by viewModel.total.collectAsState()
    val formato = remember { NumberFormat.getCurrencyInstance(Locale("es","CL")) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    Scaffold(topBar = { TopAppBar(title = { Text("Tu Carrito") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)) {

            if (items.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Tu carrito está vacío")
                }
                return@Column
            }

            LazyColumn(Modifier.weight(1f)) {
                items(items) { item ->
                    Column(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${item.producto.nombre} (x${item.cantidad})",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = formato.format(item.producto.precio * item.cantidad),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Botón de Disminuir: Elimina si llega a 1.
                            OutlinedButton(
                                onClick = {
                                    if (item.cantidad <= 1) {
                                        viewModel.quitar(item.producto.id)
                                    } else {
                                        viewModel.cambiarCantidad(item.producto.id, item.cantidad - 1)
                                    }
                                },
                                enabled = item.cantidad > 0,
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                            ) { Text("–") }

                            Spacer(Modifier.width(8.dp))

                            // Botón de Aumentar
                            OutlinedButton(
                                onClick = {
                                    viewModel.cambiarCantidad(item.producto.id, item.cantidad + 1)
                                },
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                            ) { Text("+") }

                            Spacer(Modifier.width(16.dp))

                            // Botón de Eliminar
                            TextButton(onClick = { viewModel.quitar(item.producto.id) }) {
                                Text("Eliminar")
                            }
                        }

                        Spacer(Modifier.height(8.dp))
                        Divider()
                    }
                }
            }

            // Zona del total y botones de simulación
            Spacer(Modifier.height(16.dp))

            // Fila del Total
            Row(
                Modifier.fillMaxWidth().padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total a pagar:", style = MaterialTheme.typography.titleLarge)
                Text(
                    formato.format(total),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }

            // --- NUEVOS BOTONES DE SIMULACIÓN ---

            Text(
                "Simular Transacción:",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(8.dp))

            // Simular Compra Exitosa (Vacía el carrito)
            Button(
                onClick = {
                    viewModel.vaciar()
                    scope.launch {
                        snackbarHostState.showSnackbar("Compra Exitosa ✅")
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Simular Compra Exitosa ✅")
            }

            Spacer(Modifier.height(8.dp))

            // Botón 2: Simular Compra Rechazada (Vacía el carrito)
            Button(
                onClick = {
                    viewModel.vaciar()
                    scope.launch {
                        snackbarHostState.showSnackbar("Compra Rechazada ❌")
                    }

                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Simular Compra Rechazada ❌")
            }

            Spacer(Modifier.height(16.dp))
        }
        }
}