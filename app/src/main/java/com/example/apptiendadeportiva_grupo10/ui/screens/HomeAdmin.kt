package com.example.apptiendadeportiva_grupo10.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.apptiendadeportiva_grupo10.model.Producto
import com.example.apptiendadeportiva_grupo10.viewmodel.AuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.InputStream

// ------------------ PROCESAR IMAGEN ------------------
fun procesarImagenParaBackend(context: Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bitmapOriginal = BitmapFactory.decodeStream(inputStream) ?: return null

        val aspectRatio = bitmapOriginal.width.toFloat() / bitmapOriginal.height.toFloat()
        val targetWidth = 800
        val targetHeight = (targetWidth / aspectRatio).toInt()
        val bitmapReducido = Bitmap.createScaledBitmap(bitmapOriginal, targetWidth, targetHeight, true)

        val outputStream = ByteArrayOutputStream()
        bitmapReducido.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
        val bytes = outputStream.toByteArray()

        "data:image/jpeg;base64," + Base64.encodeToString(bytes, Base64.NO_WRAP)
    } catch (e: Exception) {
        Log.e("IMG_ERROR", e.message.toString())
        null
    }
}

// ------------------ SCREEN ------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAdmin(
    viewModel: AuthViewModel,
    onLogout: () -> Unit,
    onNavigateToCrearAdmin: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) { viewModel.cargarProductos() }
    val productosList by viewModel.listaProductos

    // -------- FORM STATES --------
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precioText by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var imagenBase64 by remember { mutableStateOf("") }
    var procesandoImagen by remember { mutableStateOf(false) }

    var productoEditando by remember { mutableStateOf<Producto?>(null) }
    var esEdicion by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }
    var tipoProducto by remember { mutableStateOf("ROPA") }
    val stockPorTalla = remember { mutableStateMapOf<String, String>() }

    val categoriasExistentes = productosList.mapNotNull { it.categoria }.distinct()

    val precio = precioText.toDoubleOrNull()
    val stockValido = stockPorTalla.isNotEmpty() &&
            stockPorTalla.all { it.value.toIntOrNull() != null }

    val camposCompletos = nombre.isNotBlank() &&
            descripcion.isNotBlank() &&
            categoria.isNotBlank() &&
            color.isNotBlank() &&
            imagenBase64.isNotBlank() &&
            precio != null &&
            stockValido &&
            !procesandoImagen

    // -------- HELPERS --------
    fun limpiarFormulario() {
        nombre = ""
        descripcion = ""
        precioText = ""
        categoria = ""
        color = ""
        imagenBase64 = ""
        stockPorTalla.clear()
        esEdicion = false
        productoEditando = null
    }

    fun cargarProductoParaEditar(prod: Producto) {
        productoEditando = prod
        esEdicion = true

        nombre = prod.nombre
        descripcion = prod.descripcion ?: ""
        precioText = prod.precio.toString()
        categoria = prod.categoria ?: ""
        color = prod.color ?: ""
        imagenBase64 = prod.imagenUrl ?: ""

        stockPorTalla.clear()
        prod.stockPorTalla?.forEach {
            stockPorTalla[it.key] = it.value.toString()
        }

        scope.launch {
            listState.animateScrollToItem(0)
        }
    }

    // -------- IMAGE PICKER --------
    val filePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            procesandoImagen = true
            scope.launch {
                val img = withContext(Dispatchers.IO) {
                    procesarImagenParaBackend(context, it)
                }
                img?.let { imagenBase64 = it }
                procesandoImagen = false
            }
        }
    }

    val tallas = if (tipoProducto == "ROPA")
        listOf("S", "M", "L", "XL", "XXL")
    else (36..42).map { it.toString() }

    // ------------------ UI ------------------
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel Administrador", fontWeight = FontWeight.Bold) },
                actions = {
                    Button(onClick = onLogout) {
                        Text("Cerrar Sesión")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // -------- FORM --------
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    border = if (esEdicion)
                        BorderStroke(2.dp, Color(0xFF6A1B9A))
                    else null,
                    colors = CardDefaults.cardColors(
                        containerColor = if (esEdicion)
                            Color(0xFF6A1B9A).copy(alpha = 0.08f)
                        else MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                if (esEdicion) "✏️ Editando Producto" else "Agregar Producto",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (esEdicion) Color(0xFF6A1B9A) else Color.Unspecified
                            )

                            if (esEdicion) {
                                IconButton(onClick = { limpiarFormulario() }) {
                                    Icon(Icons.Default.Close, contentDescription = "Cancelar")
                                }
                            }
                        }

                        CustomInput("NOMBRE", nombre) { nombre = it }
                        CustomInput("DESCRIPCIÓN", descripcion) { descripcion = it }
                        CustomInput("PRECIO", precioText, KeyboardType.Decimal) { precioText = it }

                        ExposedDropdownMenuBox(expanded, { expanded = !expanded }) {
                            OutlinedTextField(
                                value = categoria,
                                onValueChange = { categoria = it },
                                label = { Text("Categoría") },
                                modifier = Modifier.fillMaxWidth().menuAnchor(),
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                                }
                            )
                            ExposedDropdownMenu(expanded, { expanded = false }) {
                                categoriasExistentes.forEach {
                                    DropdownMenuItem(
                                        text = { Text(it) },
                                        onClick = {
                                            categoria = it
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }

                        CustomInput("COLOR", color) { color = it }

                        tallas.forEach { talla ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = stockPorTalla.containsKey(talla),
                                    onCheckedChange = {
                                        if (it) stockPorTalla[talla] = ""
                                        else stockPorTalla.remove(talla)
                                    }
                                )
                                Text(talla, modifier = Modifier.width(40.dp))
                                if (stockPorTalla.containsKey(talla)) {
                                    OutlinedTextField(
                                        value = stockPorTalla[talla] ?: "",
                                        onValueChange = {
                                            stockPorTalla[talla] = it.filter { c -> c.isDigit() }
                                        },
                                        modifier = Modifier.width(80.dp),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                    )
                                }
                            }
                        }

                        Button(onClick = { filePicker.launch("image/*") }) {
                            Text(if (imagenBase64.isBlank()) "Seleccionar Imagen" else "Imagen Lista ✅")
                        }

                        if (imagenBase64.isNotBlank()) {
                            Image(
                                painter = rememberAsyncImagePainter(imagenBase64),
                                contentDescription = null,
                                modifier = Modifier.size(120.dp).align(Alignment.CenterHorizontally),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Button(
                            enabled = camposCompletos,
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            onClick = {
                                val producto = Producto(
                                    id = productoEditando?.id ?: 0,
                                    nombre = nombre,
                                    descripcion = descripcion,
                                    precio = precio!!,
                                    categoria = categoria,
                                    color = color,
                                    imagenUrl = imagenBase64,
                                    stockPorTalla = stockPorTalla.mapValues { it.value.toInt() }
                                )

                                if (esEdicion) {
                                    viewModel.modificarProducto(producto)
                                } else {
                                    viewModel.agregarProducto(producto)
                                }

                                limpiarFormulario()
                            }
                        ) {
                            Text(
                                if (esEdicion) "ACTUALIZAR PRODUCTO" else "GUARDAR PRODUCTO",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // -------- LIST --------
            item {
                Text(
                    "PRODUCTOS (${productosList.size})",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(productosList, key = { it.id }) { prod ->
                ProductoAdminItem(
                    producto = prod,
                    onEdit = { cargarProductoParaEditar(prod) },
                    onDelete = { viewModel.eliminarProducto(prod.id) }
                )
            }
        }
    }
}

// ------------------ COMPONENTS ------------------
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
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ProductoAdminItem(
    producto: Producto,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(producto.nombre, fontWeight = FontWeight.Bold)
                Text("$${producto.precio}", color = Color(0xFF6A1B9A))
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                }
            }
        }
    }
}
