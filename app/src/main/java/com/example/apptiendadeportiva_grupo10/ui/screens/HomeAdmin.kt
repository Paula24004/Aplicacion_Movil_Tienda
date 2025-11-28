package com.example.apptiendadeportiva_grupo10.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.apptiendadeportiva_grupo10.viewmodel.AuthViewModel
import com.example.apptiendadeportiva_grupo10.model.Producto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAdmin(
    viewModel: AuthViewModel,
    onLogout: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.cargarProductos()
    }

    var nuevoNombre by remember { mutableStateOf("") }
    var nuevoPrecioText by remember { mutableStateOf("") }
    var nuevaDescripcion by remember { mutableStateOf("") }
    var nuevaImagen by remember { mutableStateOf("") }
    val productosList by viewModel.listaProductos
    val precioDouble = nuevoPrecioText.toDoubleOrNull()
    val camposCompletos = nuevoNombre.isNotBlank() &&
            nuevoPrecioText.isNotBlank() &&
            nuevaDescripcion.isNotBlank() &&
            nuevaImagen.isNotBlank() &&
            precioDouble != null &&
            precioDouble >= 0.0


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel Administrador") },
                actions = {
                    Button(onClick = onLogout) {
                        Text("Cerrar Sesión")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Gestión de Productos", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))

            // --- SECCIÓN PARA AGREGAR PRODUCTO ---
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Agregar Nuevo Producto", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(8.dp))

                    // Nombre del Producto
                    OutlinedTextField(
                        value = nuevoNombre,
                        onValueChange = { nuevoNombre = it },
                        label = { Text("Nombre del Producto") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(Modifier.height(8.dp))

                    // Descripción del Producto
                    OutlinedTextField(
                        value = nuevaDescripcion,
                        onValueChange = { nuevaDescripcion = it },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))

                    // Precio (FIX: Acepta decimales y convierte a Double)
                    OutlinedTextField(
                        value = nuevoPrecioText,
                        // Permite dígitos y un punto decimal
                        onValueChange = {
                            nuevoPrecioText = it.filter { char -> char.isDigit() || char == '.' }
                        },
                        label = { Text("Precio (número decimal)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        // Cambiado a Decimal para permitir el punto
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                    Spacer(Modifier.height(8.dp))

                    // URL/Ruta de Imagen
                    OutlinedTextField(
                        value = nuevaImagen,
                        onValueChange = { nuevaImagen = it },
                        label = { Text("Ruta/URL de Imagen") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = {
                            // Se utiliza el Double ya validado
                            if (camposCompletos && precioDouble != null) {

                                // Generación de ID (Simplificado: Usar una ID negativa temporal o un UUID
                                // si la BD/API genera la ID. Aquí usamos el incremento local por ahora).
                                val nuevoId = (productosList.maxOfOrNull { it.id } ?: 0) + 1

                                val stockInicialPorTalla = mapOf("unica" to 10)

                                val nuevoProducto = Producto(
                                    id = nuevoId,
                                    nombre = nuevoNombre,
                                    descripcion = nuevaDescripcion,
                                    precio = precioDouble,
                                    imagenUrl = nuevaImagen,
                                    stockPorTalla = stockInicialPorTalla
                                )
                                // Llama a la función del ViewModel que guarda en Room
                                viewModel.agregarProducto(nuevoProducto)

                                // Limpiar campos después de agregar
                                nuevoNombre = ""
                                nuevoPrecioText = ""
                                nuevaDescripcion = ""
                                nuevaImagen = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = camposCompletos
                    ) {
                        Text("Guardar Producto")
                    }
                }
            }

            // --- SECCIÓN PARA LISTAR Y ELIMINAR ---
            Text("Productos en el Catálogo (${productosList.size})", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            if (productosList.isEmpty()) {
                Text("No hay productos en el catálogo.", color = MaterialTheme.colorScheme.error)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // La lista se observa automáticamente gracias al 'by'
                    items(productosList, key = { it.id }) { producto ->
                        ProductoAdminItem(
                            producto = producto,
                            // Llama a la función del ViewModel que elimina de Room
                            onDelete = { viewModel.eliminarProducto(producto.id) }
                        )
                    }
                }
            }
        }
    }
}

// Composable auxiliar para cada elemento de la lista de productos (sin cambios)
@Composable
fun ProductoAdminItem(producto: Producto, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre ?: "Producto Desconocido",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "ID: ${producto.id} | Precio: $${String.format("%,.2f", producto.precio)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            // Botón de Eliminación
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Eliminar Producto",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}