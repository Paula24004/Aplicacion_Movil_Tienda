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
    var nuevaCategory by remember { mutableStateOf("") }
    var nuevaSize by remember { mutableStateOf("") }
    var nuevaColor by remember { mutableStateOf("") }
    var nuevoStockPorTallaText by remember { mutableStateOf("") }  // Para stock por talla (e.g., "S:10,M:5")

    val productosList by viewModel.listaProductos
    val precioDouble = nuevoPrecioText.toDoubleOrNull()

    // Función para parsear el stock por talla (e.g., "S:10,M:5" -> mapOf("S" to 10, "M" to 5))
    fun parseStockPorTalla(text: String): Map<String, Int>? {
        if (text.isBlank()) return null
        val map = mutableMapOf<String, Int>()
        val pairs = text.split(",").map { it.trim() }
        for (pair in pairs) {
            val parts = pair.split(":")
            if (parts.size == 2) {
                val talla = parts[0].trim()
                val stock = parts[1].trim().toIntOrNull()
                if (talla.isNotBlank() && stock != null && stock >= 0) {
                    map[talla] = stock
                } else {
                    return null  // Invalid format
                }
            } else {
                return null  // Invalid format
            }
        }
        return if (map.isNotEmpty()) map else null
    }

    val stockPorTallaParsed = parseStockPorTalla(nuevoStockPorTallaText)

    // Validación completa: incluye todos los campos y stock por talla válido
    val camposCompletos = nuevoNombre.isNotBlank() &&
            nuevoPrecioText.isNotBlank() &&
            nuevaDescripcion.isNotBlank() &&
            nuevaImagen.isNotBlank() &&
            precioDouble != null &&
            nuevaCategory.isNotBlank() &&
            nuevaSize.isNotBlank() &&
            nuevaColor.isNotBlank() &&
            nuevoStockPorTallaText.isNotBlank() &&
            stockPorTallaParsed != null &&
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

                    // Precio
                    OutlinedTextField(
                        value = nuevoPrecioText,
                        onValueChange = {
                            nuevoPrecioText = it.filter { char -> char.isDigit() || char == '.' }
                        },
                        label = { Text("Precio (número decimal)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                    Spacer(Modifier.height(8.dp))

                    // Categoria
                    OutlinedTextField(
                        value = nuevaCategory,
                        onValueChange = { nuevaCategory = it },
                        label = { Text("Categoria") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))

                    // Talla
                    OutlinedTextField(
                        value = nuevaSize,
                        onValueChange = { nuevaSize = it },
                        label = { Text("Talla") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))

                    // Color
                    OutlinedTextField(
                        value = nuevaColor,
                        onValueChange = { nuevaColor = it },
                        label = { Text("Color") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))

                    // Stock por Talla (formato: "S:10,M:5,L:8")
                    OutlinedTextField(
                        value = nuevoStockPorTallaText,
                        onValueChange = { nuevoStockPorTallaText = it },
                        label = { Text("Stock por Talla (ej: S:10,M:5)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = nuevoStockPorTallaText.isNotBlank() && stockPorTallaParsed == null  // Muestra error si formato inválido
                    )
                    if (nuevoStockPorTallaText.isNotBlank() && stockPorTallaParsed == null) {
                        Text("Formato inválido. Usa 'talla:stock,talla:stock'", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }
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
                            if (camposCompletos && precioDouble != null && stockPorTallaParsed != null) {
                                val nuevoId = (productosList.maxOfOrNull { it.id } ?: 0) + 1

                                val nuevoProducto = Producto(
                                    id = nuevoId,
                                    nombre = nuevoNombre,
                                    descripcion = nuevaDescripcion,
                                    precio = precioDouble,
                                    categoria = nuevaCategory,
                                    size = nuevaSize,
                                    color = nuevaColor,
                                    imagenUrl = nuevaImagen,
                                    stockPorTalla = stockPorTallaParsed
                                )
                                viewModel.agregarProducto(nuevoProducto)

                                // Limpiar todos los campos
                                nuevoNombre = ""
                                nuevoPrecioText = ""
                                nuevaDescripcion = ""
                                nuevaImagen = ""
                                nuevaCategory = ""
                                nuevaSize = ""
                                nuevaColor = ""
                                nuevoStockPorTallaText = ""
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
                    items(productosList, key = { it.id }) { producto ->
                        ProductoAdminItem(
                            producto = producto,
                            onDelete = { viewModel.eliminarProducto(producto.id) }
                        )
                    }
                }
            }
        }
    }
}

// Composable auxiliar para cada elemento de la lista de productos
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
                Text(
                    text = "Categoría: ${producto.categoria ?: "N/A"} | Talla: ${producto.size ?: "N/A"} | Color: ${producto.color ?: "N/A"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "Stock: ${producto.stockPorTalla?.entries?.joinToString { "${it.key}: ${it.value}" } ?: "N/A"}",
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
