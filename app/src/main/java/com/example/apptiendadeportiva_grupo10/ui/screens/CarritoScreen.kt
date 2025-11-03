package com.example.apptiendadeportiva_grupo10.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.apptiendadeportiva_grupo10.viewmodel.CarritoViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    navController: NavController,
    viewModel: CarritoViewModel   // ← usa el que viene de RootScreen
) {
    val items by viewModel.items.collectAsState()
    val total by viewModel.total.collectAsState()
    val formato = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito") }
            )
        },
        bottomBar = {
            if (items.isNotEmpty()) {
                Surface(tonalElevation = 2.dp) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total: ${formato.format(total)}", style = MaterialTheme.typography.titleMedium)
                        Button(onClick = { /* TODO: flujo de pago */ }) {
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items) { it ->
                    ElevatedCard(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(12.dp)) {
                            Text(it.producto.nombre, style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(4.dp))
                            Text("Talla: ${it.talla}  •  Cantidad: ${it.cantidad}")
                            Spacer(Modifier.height(4.dp))
                            Text("Subtotal: ${formato.format(it.cantidad * it.producto.precio)}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(onClick = {
                                    val nueva = (it.cantidad - 1).coerceAtLeast(0)
                                    if (nueva == 0) {
                                        viewModel.quitar(it.producto.id)
                                    } else {
                                        viewModel.cambiarCantidad(it.producto.id, nueva)
                                    }
                                }) { Text("-") }
                                OutlinedButton(onClick = {
                                    viewModel.cambiarCantidad(it.producto.id, it.cantidad + 1)
                                }) { Text("+") }
                                Spacer(Modifier.weight(1f))
                                TextButton(onClick = { viewModel.quitar(it.producto.id) }) { Text("Quitar") }
                            }
                        }
                    }
                }
            }
        }
    }
}
