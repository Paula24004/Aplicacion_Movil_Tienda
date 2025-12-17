package com.example.apptiendadeportiva_grupo10.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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

// --- FUNCIÓN DE PROCESAMIENTO DE IMAGEN ---
fun procesarImagenParaBackend(context: Context, uri: Uri): String? {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val bitmapOriginal = BitmapFactory.decodeStream(inputStream) ?: return null

        // Redimensionar para reducir peso del Base64 (Máximo 800px)
        val aspectRadio = bitmapOriginal.width.toFloat() / bitmapOriginal.height.toFloat()
        val targetWidth = 800
        val targetHeight = (targetWidth / aspectRadio).toInt()
        val bitmapReducido = Bitmap.createScaledBitmap(bitmapOriginal, targetWidth, targetHeight, true)

        val outputStream = ByteArrayOutputStream()
        bitmapReducido.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
        val bytes = outputStream.toByteArray()

        val base64String = Base64.encodeToString(bytes, Base64.NO_WRAP)
        "data:image/jpeg;base64,$base64String"
    } catch (e: Exception) {
        Log.e("IMAGEN_ERROR", "Error procesando imagen: ${e.message}")
        null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAdmin(
    viewModel: AuthViewModel,
    onLogout: () -> Unit,
    onNavigateToCrearAdmin: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) { viewModel.cargarProductos() }

    val productosList by viewModel.listaProductos

    // --- ESTADOS DEL FORMULARIO ---
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precioText by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var imagenBase64 by remember { mutableStateOf("") }
    var procesandoImagen by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }
    val categoriasExistentes = remember(productosList) {
        productosList.mapNotNull { it.categoria?.trim() }.distinct().sorted()
    }

    var tipoProducto by remember { mutableStateOf("ROPA") }
    val stockPorTalla = remember { mutableStateMapOf<String, String>() }

    // --- VALIDACIONES ---
    val precio = precioText.toDoubleOrNull()
    val stockValido = stockPorTalla.isNotEmpty() &&
            stockPorTalla.all { it.value.isNotBlank() && it.value.toIntOrNull() != null }

    val camposCompletos = nombre.isNotBlank() && descripcion.isNotBlank() &&
            categoria.isNotBlank() && color.isNotBlank() &&
            imagenBase64.isNotBlank() && precio != null && stockValido && !procesandoImagen

    val filePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            procesandoImagen = true
            scope.launch {
                val resultado = withContext(Dispatchers.IO) {
                    procesarImagenParaBackend(context, it)
                }
                if (resultado != null) {
                    imagenBase64 = resultado
                }
                procesandoImagen = false
            }
        }
    }

    val tallas = if (tipoProducto == "ROPA") listOf("S", "M", "L", "XL", "XXL")
    else (36..42).map { it.toString() }

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
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // GESTIÓN DE PERSONAL
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF650099).copy(alpha = 0.1f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF650099))
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Gestión de Personal", fontWeight = FontWeight.Bold, color = Color(0xFF650099))
                            Text("Cuentas de administrador", fontSize = 12.sp)
                        }
                        Button(onClick = onNavigateToCrearAdmin, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF650099))) {
                            Text("CREAR ADMIN")
                        }
                    }
                }
            }

            item { Text("Agregar Producto", fontSize = 26.sp, fontWeight = FontWeight.Bold) }

            // FORMULARIO
            item {
                Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(6.dp)) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        CustomInput("NOMBRE", nombre) { nombre = it }
                        CustomInput("DESCRIPCIÓN", descripcion) { descripcion = it }
                        CustomInput("PRECIO", precioText, KeyboardType.Decimal) { precioText = it }

                        // DROPDOWN CATEGORIA
                        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                            OutlinedTextField(
                                value = categoria,
                                onValueChange = { categoria = it },
                                label = { Text("Categoría") },
                                modifier = Modifier.fillMaxWidth().menuAnchor(),
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                            )
                            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                categoriasExistentes.forEach { op ->
                                    DropdownMenuItem(text = { Text(op) }, onClick = { categoria = op; expanded = false })
                                }
                            }
                        }

                        CustomInput("COLOR", color) { color = it }

                        Text("TIPO Y STOCK", fontWeight = FontWeight.Bold)
                        Row {
                            RadioButton(selected = tipoProducto == "ROPA", onClick = { tipoProducto = "ROPA"; stockPorTalla.clear() })
                            Text("ROPA", Modifier.align(Alignment.CenterVertically))
                            Spacer(Modifier.width(8.dp))
                            RadioButton(selected = tipoProducto == "CALZADO", onClick = { tipoProducto = "CALZADO"; stockPorTalla.clear() })
                            Text("CALZADO", Modifier.align(Alignment.CenterVertically))
                        }

                        tallas.forEach { talla ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(checked = stockPorTalla.containsKey(talla), onCheckedChange = { if (it) stockPorTalla[talla] = "" else stockPorTalla.remove(talla) })
                                Text(talla, modifier = Modifier.width(40.dp))
                                if (stockPorTalla.containsKey(talla)) {
                                    OutlinedTextField(
                                        value = stockPorTalla[talla] ?: "",
                                        onValueChange = { stockPorTalla[talla] = it.filter { c -> c.isDigit() } },
                                        modifier = Modifier.width(90.dp),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                    )
                                }
                            }
                        }

                        // IMAGEN
                        Button(
                            onClick = { filePicker.launch("image/*") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !procesandoImagen
                        ) {
                            Text(if (procesandoImagen) "PROCESANDO..." else if (imagenBase64.isEmpty()) "SELECCIONAR IMAGEN" else "IMAGEN LISTA ✅")
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
                            onClick = {
                                val producto = Producto(
                                    id = 0, nombre = nombre, descripcion = descripcion,
                                    precio = precio!!, categoria = categoria,
                                    color = color,
                                    imagenUrl = imagenBase64, // ENVIANDO EL BASE64 REAL
                                    stockPorTalla = stockPorTalla.mapValues { it.value.toIntOrNull() ?: 0 }
                                )
                                Log.d("ENVIO", "Enviando producto con imagen de largo: ${imagenBase64.length}")
                                viewModel.agregarProducto(producto)

                                // Reset
                                nombre = ""; descripcion = ""; precioText = ""; categoria = ""; color = ""; imagenBase64 = ""; stockPorTalla.clear()
                            },
                            enabled = camposCompletos,
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A))
                        ) {
                            Text("GUARDAR PRODUCTO", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // LISTADO
            item { Text("PRODUCTOS (${productosList.size})", fontSize = 22.sp, fontWeight = FontWeight.Bold) }
            items(productosList, key = { it.id }) { prod ->
                ProductoAdminItem(producto = prod, onDelete = { viewModel.eliminarProducto(prod.id) })
            }
        }
    }
}

@Composable
fun CustomInput(label: String, value: String, keyboardType: KeyboardType = KeyboardType.Text, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ProductoAdminItem(producto: Producto, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), elevation = CardDefaults.cardElevation(2.dp)) {
        Row(modifier = Modifier.padding(12.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(producto.nombre, fontWeight = FontWeight.Bold)
                Text("$${producto.precio}", color = Color(0xFF6A1B9A))
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
            }
        }
    }
}