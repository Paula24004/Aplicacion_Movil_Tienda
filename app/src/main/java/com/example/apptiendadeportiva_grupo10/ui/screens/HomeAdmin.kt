package com.example.apptiendadeportiva_grupo10.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.apptiendadeportiva_grupo10.viewmodel.AuthViewModel
import com.example.apptiendadeportiva_grupo10.model.Producto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAdmin(
    viewModel: AuthViewModel,
    onLogout: () -> Unit
) {
    LaunchedEffect(Unit) { viewModel.cargarProductos() }

    var nuevoNombre by remember { mutableStateOf("") }
    var nuevoPrecioText by remember { mutableStateOf("") }
    var nuevaDescripcion by remember { mutableStateOf("") }
    var nuevaImagen by remember { mutableStateOf("") }
    var nuevaCategory by remember { mutableStateOf("") }
    var nuevaSize by remember { mutableStateOf("") }
    var nuevaColor by remember { mutableStateOf("") }
    var nuevoStockPorTallaText by remember { mutableStateOf("") }

    var productoAEditar by remember { mutableStateOf<Producto?>(null) }

    val productosList by viewModel.listaProductos
    val precioDouble = nuevoPrecioText.toDoubleOrNull()

    // ---------------- FILE PICKER ----------------
    val filePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { nuevaImagen = it.toString() }
    }

    // ---------------- FUNCTION PARSE STOCK ----------------
    fun parseStockPorTalla(text: String): Map<String, Int>? {
        if (text.isBlank()) return null
        return text.split(",").mapNotNull { pair ->
            val parts = pair.split(":")
            if (parts.size == 2) {
                val talla = parts[0].trim()
                val stock = parts[1].trim().toIntOrNull()
                if (stock != null) talla to stock else null
            } else null
        }.toMap().takeIf { it.isNotEmpty() }
    }

    val stockPorTallaParsed = parseStockPorTalla(nuevoStockPorTallaText)

    val camposCompletos =
        nuevoNombre.isNotBlank() &&
                nuevoPrecioText.isNotBlank() &&
                nuevaDescripcion.isNotBlank() &&
                nuevaImagen.isNotBlank() &&
                nuevaCategory.isNotBlank() &&
                nuevaSize.isNotBlank() &&
                nuevaColor.isNotBlank() &&
                stockPorTallaParsed != null &&
                precioDouble != null

    // ---------------- UI ----------------
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Panel Administrador",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                actions = {
                    Button(
                        onClick = onLogout,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7D3C98),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Cerrar Sesión")
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            item {
                Text(
                    "Gestión de Productos",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // ---------------- FORMULARIO AGREGAR ----------------
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(Modifier.padding(20.dp)) {

                        Text(
                            "Agregar Nuevo Producto",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(16.dp))

                        CustomInput("Nombre del Producto", nuevoNombre) { nuevoNombre = it }
                        CustomInput("Descripción", nuevaDescripcion) { nuevaDescripcion = it }

                        CustomInput("Precio (decimal)", nuevoPrecioText, KeyboardType.Decimal) {
                            nuevoPrecioText = it.filter { ch -> ch.isDigit() || ch == '.' }
                        }

                        CustomInput("Categoría", nuevaCategory) { nuevaCategory = it }
                        CustomInput("Talla", nuevaSize) { nuevaSize = it }
                        CustomInput("Color", nuevaColor) { nuevaColor = it }

                        CustomInput("Stock por Talla (S:10,M:5)", nuevoStockPorTallaText) {
                            nuevoStockPorTallaText = it
                        }

                        Spacer(Modifier.height(12.dp))

                        Text("Imagen del Producto", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Button(onClick = { filePicker.launch("image/*") }) {
                                Text("Seleccionar Imagen")
                            }

                            Spacer(Modifier.width(12.dp))

                            if (nuevaImagen.isNotBlank()) {
                                Text("Imagen seleccionada", color = Color(0xFF4CAF50))
                            }
                        }

                        if (nuevaImagen.isNotBlank()) {
                            Image(
                                painter = rememberAsyncImagePainter(nuevaImagen),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(140.dp)
                                    .padding(top = 10.dp),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(Modifier.height(16.dp))

                        Button(
                            onClick = {
                                val nuevoProducto = Producto(
                                    id = 0,
                                    nombre = nuevoNombre,
                                    descripcion = nuevaDescripcion,
                                    precio = precioDouble!!,
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
                            },
                            enabled = camposCompletos,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF7D3C98),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Guardar Producto", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // ---------------- LISTA DE PRODUCTOS ----------------
            item {
                Text(
                    "Productos en el Catálogo (${productosList.size})",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (productosList.isEmpty()) {
                item { Text("No hay productos registrados.", color = Color.Red) }
            } else {
                items(
                    items = productosList,
                    key = { prod: Producto -> prod.id }
                ) { prod: Producto ->
                    ProductoAdminItem(
                        producto = prod,
                        onDelete = { viewModel.eliminarProducto(prod.id) },
                        onEdit = { productoEditado: Producto ->
                            productoAEditar = productoEditado
                        }
                    )
                }
            }
        }
    }

    // ---------------- DIALOGO EDITAR PRODUCTO ----------------
    productoAEditar?.let { producto ->
        EditProductDialog(
            producto = producto,
            onDismiss = { productoAEditar = null },
            onSave = { prodModificado: Producto ->
                viewModel.modificarProducto(prodModificado)
                productoAEditar = null
            }
        )
    }
}

// =================================================================================
// INPUT
// =================================================================================

@Composable
fun CustomInput(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = {
            Text(label, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}

// =================================================================================
// ITEM LISTA ADMIN
// =================================================================================

@Composable
fun ProductoAdminItem(
    producto: Producto,
    onDelete: () -> Unit,
    onEdit: (Producto) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5))
    ) {

        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(modifier = Modifier.weight(1f)) {

                Text(producto.nombre, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("ID: ${producto.id}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("Precio: $${String.format("%,.2f", producto.precio)}", fontSize = 16.sp)
                Text("Categoría: ${producto.categoria}", fontSize = 16.sp)
                Text("Talla: ${producto.size} | Color: ${producto.color}", fontSize = 16.sp)
                Text(
                    "Stock: ${producto.stockPorTalla?.entries?.joinToString { "${it.key}:${it.value}" }}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row {
                IconButton(onClick = { onEdit(producto) }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Editar",
                        tint = Color(0xFF6A1B9A)
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Eliminar",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductDialog(
    producto: Producto,
    onDismiss: () -> Unit,
    onSave: (Producto) -> Unit
) {
    var nombre by remember { mutableStateOf(producto.nombre) }
    var descripcion by remember { mutableStateOf(producto.descripcion ?: "") }
    var precioText by remember { mutableStateOf(producto.precio.toString()) }
    var categoria by remember { mutableStateOf(producto.categoria ?: "") }
    var size by remember { mutableStateOf(producto.size ?: "") }
    var color by remember { mutableStateOf(producto.color ?: "") }
    var imagenUrl by remember { mutableStateOf(producto.imagenUrl ?: "") }
    var stockText by remember {
        mutableStateOf(
            producto.stockPorTalla?.entries?.joinToString(",") { "${it.key}:${it.value}" } ?: ""
        )
    }

    fun parseStock(text: String): Map<String, Int>? {
        if (text.isBlank()) return null
        return text.split(",").mapNotNull { pair ->
            val parts = pair.split(":")
            if (parts.size == 2) {
                val talla = parts[0].trim()
                val stock = parts[1].trim().toIntOrNull()
                if (stock != null) talla to stock else null
            } else null
        }.toMap().takeIf { it.isNotEmpty() }
    }

    val stockMap = parseStock(stockText)
    val precioDouble = precioText.toDoubleOrNull()

    val camposValidos = nombre.isNotBlank() &&
            descripcion.isNotBlank() &&
            categoria.isNotBlank() &&
            size.isNotBlank() &&
            color.isNotBlank() &&
            imagenUrl.isNotBlank() &&
            precioDouble != null &&
            stockMap != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Modificar Producto (ID: ${producto.id})", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = precioText,
                    onValueChange = { precioText = it.filter { ch -> ch.isDigit() || ch == '.' } },
                    label = { Text("Precio") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = categoria,
                    onValueChange = { categoria = it },
                    label = { Text("Categoría") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = size,
                    onValueChange = { size = it },
                    label = { Text("Talla") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = color,
                    onValueChange = { color = it },
                    label = { Text("Color") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = stockText,
                    onValueChange = { stockText = it },
                    label = { Text("Stock por Talla (ej: S:10,M:5)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = imagenUrl,
                    onValueChange = { imagenUrl = it },
                    label = { Text("URL Imagen") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val productoActualizado = producto.copy(
                        nombre = nombre,
                        descripcion = descripcion,
                        precio = precioDouble ?: producto.precio,
                        categoria = categoria,
                        size = size,
                        color = color,
                        imagenUrl = imagenUrl,
                        stockPorTalla = stockMap
                    )
                    onSave(productoActualizado)
                },
                enabled = camposValidos
            ) {
                Text("Guardar Cambios", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
