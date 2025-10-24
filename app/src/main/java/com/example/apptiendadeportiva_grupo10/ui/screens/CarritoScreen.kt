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
import androidx.navigation.NavHostController
import com.example.apptiendadeportiva_grupo10.viewmodel.CarritoViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(navController: NavHostController,viewModel: CarritoViewModel) {

    val items = viewModel.items.collectAsState().value
    val total = viewModel.total.collectAsState().value
    val formato = remember { NumberFormat.getCurrencyInstance(Locale("es","CL")) }

    Scaffold(topBar = { TopAppBar(title = { Text("Tu Carrito") }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            if (items.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Tu carrito está vacío")
                }
                return@Column
            }

            LazyColumn(Modifier.weight(1f)) {
                items(items) { item ->
                    Column(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        Text("${item.producto.nombre} x${item.cantidad}")
                        Text("Subtotal: ${formato.format(item.producto.precio * item.cantidad)}")
                        Row {
                            OutlinedButton(onClick = {
                                viewModel.cambiarCantidad(item.producto.id, item.cantidad - 1)
                            }) { Text("–") }
                            Spacer(Modifier.width(8.dp))
                            OutlinedButton(onClick = {
                                viewModel.cambiarCantidad(item.producto.id, item.cantidad + 1)
                            }) { Text("+") }
                            Spacer(Modifier.width(8.dp))
                            TextButton(onClick = { viewModel.quitar(item.producto.id) }) { Text("Eliminar") }
                        }
                        Divider()
                    }
                }
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total", style = MaterialTheme.typography.titleMedium)
                Text(formato.format(total), style = MaterialTheme.typography.titleMedium)
            }
            Spacer(Modifier.height(8.dp))
            Button(onClick = { viewModel.vaciar() }, modifier = Modifier.fillMaxWidth()) {
                Text("Finalizar compra")
            }
        }
    }
}