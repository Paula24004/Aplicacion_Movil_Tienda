package com.example.apptiendadeportiva_grupo10.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn // Importación clave
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit // 💡 Importación para el botón de modificar
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

    // --- Variables de Estado para Agregar Producto ---
    var nuevoNombre by remember { mutableStateOf("") }
    var nuevoPrecioText by remember { mutableStateOf("") }
    var nuevaDescripcion by remember { mutableStateOf("") }
    var nuevaImagen by remember { mutableStateOf("") }
    var nuevaCategory by remember { mutableStateOf("") }
    var nuevaSize by remember { mutableStateOf("") }
    var nuevaColor by remember { mutableStateOf("") }
    var nuevoStockPorTallaText by remember { mutableStateOf("") }

    // --- NUEVO: Estado para Modificar Producto ---
    var productoAEditar by remember { mutableStateOf<Producto?>(null) }

    val productosList by viewModel.listaProductos
    val precioDouble = nuevoPrecioText.toDoubleOrNull()

    // Convierte "S:10,M:5" en un Map<String, Int>
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
                } else return null
            } else return null
        }
        return map.takeIf { it.isNotEmpty() }
    }

    val stockPorTallaParsed = parseStockPorTalla(nuevoStockPorTallaText)

    // Validación completa
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
                    Button(onClick = onLogout) { Text("Cerrar Sesión") }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Título de la sección
            item {
                Text("Gestión de Productos", style = MaterialTheme.typography.headlineMedium)
            }

            // ------------------- AGREGAR PRODUCTO (Formulario) -------------------
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Agregar Nuevo Producto", style = MaterialTheme.typography.titleLarge)
                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = nuevoNombre,
                            onValueChange = { nuevoNombre = it },
                            label = { Text("Nombre del Producto") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = nuevaDescripcion,
                            onValueChange = { nuevaDescripcion = it },
                            label = { Text("Descripción") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = nuevoPrecioText,
                            onValueChange = { nuevoPrecioText = it.filter { ch -> ch.isDigit() || ch == '.' } },
                            label = { Text("Precio (decimal)") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )
                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = nuevaCategory,
                            onValueChange = { nuevaCategory = it },
                            label = { Text("Categoría") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = nuevaSize,
                            onValueChange = { nuevaSize = it },
                            label = { Text("Talla") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = nuevaColor,
                            onValueChange = { nuevaColor = it },
                            label = { Text("Color") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = nuevoStockPorTallaText,
                            onValueChange = { nuevoStockPorTallaText = it },
                            label = { Text("Stock por Talla (ej: S:10,M:5)") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = nuevoStockPorTallaText.isNotBlank() && stockPorTallaParsed == null
                        )
                        if (nuevoStockPorTallaText.isNotBlank() && stockPorTallaParsed == null) {
                            Text("Formato inválido. Usa talla:stock,talla:stock",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = nuevaImagen,
                            onValueChange = { nuevaImagen = it },
                            label = { Text("URL / Ruta de Imagen") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(16.dp))

                        Button(
                            onClick = {
                                if (precioDouble != null && stockPorTallaParsed != null) {
                                    val nuevoProducto = Producto(
                                        id = 0,
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
            }

            // ------------------- Título del Catálogo -------------------
            item {
                Text("Productos en el Catálogo (${productosList.size})",
                    style = MaterialTheme.typography.titleLarge
                )
            }


            // ------------------- LISTA DE PRODUCTOS -------------------
            if (productosList.isEmpty()) {
                item {
                    Text("No hay productos registrados.", color = MaterialTheme.colorScheme.error)
                }
            } else {
                items(productosList, key = { it.id }) { producto ->
                    ProductoAdminItem(
                        producto = producto,
                        onDelete = { viewModel.eliminarProducto(producto.id) },
                        onEdit = { productoAEditar = it } // 💡 NUEVA ACCIÓN DE EDICIÓN
                    )
                }
            }
        }
    }

    // ------------------- LÓGICA DEL DIÁLOGO DE MODIFICACIÓN -------------------
    productoAEditar?.let { producto ->
        EditProductDialog(
            producto = producto,
            onDismiss = { productoAEditar = null }, // Cierra el diálogo
            onSave = { productoModificado ->
                viewModel.modificarProducto(productoModificado) // Llama a la función del ViewModel
                productoAEditar = null
            }
        )
    }
}


// =================================================================================
// COMPOSABLES REUTILIZABLES
// =================================================================================

// 💡 FUNCIÓN MODIFICADA: Ahora recibe el callback onEdit
@Composable
fun ProductoAdminItem(
    producto: Producto,
    onDelete: () -> Unit,
    onEdit: (Producto) -> Unit // NUEVO: Callback para el botón de modificar
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(producto.nombre, fontWeight = FontWeight.Bold)
                Text("ID: ${producto.id} | Precio: $${String.format("%,.2f", producto.precio)}")
                Text("Categoría: ${producto.categoria} | Talla: ${producto.size} | Color: ${producto.color}")
                Text("Stock: ${producto.stockPorTalla?.entries?.joinToString { "${it.key}: ${it.value}" }}")
            }

            // Contenedor para los dos botones
            Row {
                // 💡 NUEVO: Botón de Modificación
                IconButton(onClick = { onEdit(producto) }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Modificar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                // Botón de Eliminación (existente)
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

// 💡 NUEVA FUNCIÓN: Diálogo para la edición de productos
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductDialog(
    producto: Producto,
    onDismiss: () -> Unit,
    onSave: (Producto) -> Unit
) {
    // Función de ayuda local para parsear el stock (copiada de HomeAdmin para ser autocontenida)
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
                } else return null
            } else return null
        }
        return map.takeIf { it.isNotEmpty() }
    }

    // Estados inicializados con los valores del producto
    var nombre by remember { mutableStateOf(producto.nombre) }
    var descripcion by remember { mutableStateOf(producto.descripcion ?: "") }
    var precioText by remember { mutableStateOf(producto.precio.toString()) }
    var categoria by remember { mutableStateOf(producto.categoria ?: "") }
    var size by remember { mutableStateOf(producto.size ?: "") }
    var color by remember { mutableStateOf(producto.color ?: "") }
    var imagenUrl by remember { mutableStateOf(producto.imagenUrl ?: "") }
    var stockPorTallaText by remember {
        // Convierte el Map a string "S:10,M:5" para el TextField
        mutableStateOf(producto.stockPorTalla?.entries?.joinToString(",") { "${it.key}:${it.value}" } ?: "")
    }

    val precioDouble = precioText.toDoubleOrNull()
    val stockPorTallaParsed = parseStockPorTalla(stockPorTallaText)

    val camposCompletos = nombre.isNotBlank() &&
            precioText.isNotBlank() &&
            descripcion.isNotBlank() &&
            imagenUrl.isNotBlank() &&
            precioDouble != null &&
            categoria.isNotBlank() &&
            size.isNotBlank() &&
            color.isNotBlank() &&
            stockPorTallaText.isNotBlank() &&
            stockPorTallaParsed != null &&
            precioDouble >= 0.0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Modificar Producto ID: ${producto.id}") },
        text = {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                item {
                    OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre del Producto") }, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = precioText,
                        onValueChange = { precioText = it.filter { ch -> ch.isDigit() || ch == '.' } },
                        label = { Text("Precio (decimal)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoría") }, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(value = size, onValueChange = { size = it }, label = { Text("Talla") }, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(value = color, onValueChange = { color = it }, label = { Text("Color") }, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = stockPorTallaText,
                        onValueChange = { stockPorTallaText = it },
                        label = { Text("Stock por Talla (ej: S:10,M:5)") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = stockPorTallaText.isNotBlank() && stockPorTallaParsed == null
                    )
                    if (stockPorTallaText.isNotBlank() && stockPorTallaParsed == null) {
                        Text("Formato inválido. Usa talla:stock,talla:stock",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(value = imagenUrl, onValueChange = { imagenUrl = it }, label = { Text("URL / Ruta de Imagen") }, modifier = Modifier.fillMaxWidth())
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val productoModificado = producto.copy(
                        nombre = nombre,
                        descripcion = descripcion,
                        precio = precioDouble ?: producto.precio,
                        categoria = categoria,
                        size = size,
                        color = color,
                        imagenUrl = imagenUrl,
                        stockPorTalla = stockPorTallaParsed
                    )
                    onSave(productoModificado) // Llama al ViewModel
                },
                enabled = camposCompletos
            ) {
                Text("Guardar Cambios")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}