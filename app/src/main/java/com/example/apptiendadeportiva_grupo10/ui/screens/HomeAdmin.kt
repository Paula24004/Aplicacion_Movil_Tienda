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
import com.example.apptiendadeportiva_grupo10.model.Producto
import com.example.apptiendadeportiva_grupo10.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAdmin(
    viewModel: AuthViewModel,
    onLogout: () -> Unit,
    onNavigateToCrearAdmin: () -> Unit
) {
    LaunchedEffect(Unit) { viewModel.cargarProductos() }

    val productosList by viewModel.listaProductos

    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precioText by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var imagen by remember { mutableStateOf("") }

    var tipoProducto by remember { mutableStateOf("ROPA") }
    val stockPorTalla = remember { mutableStateMapOf<String, String>() }

    val precio = precioText.toDoubleOrNull()

    val stockValido =
        stockPorTalla.isNotEmpty() &&
                stockPorTalla.all { it.value.isNotBlank() && it.value.toIntOrNull() != null }

    val camposCompletos =
        nombre.isNotBlank() &&
                descripcion.isNotBlank() &&
                categoria.isNotBlank() &&
                color.isNotBlank() &&
                imagen.isNotBlank() &&
                precio != null &&
                stockValido

    val filePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { imagen = it.toString() } }

    val tallasRopa = listOf("S", "M", "L", "XL", "XXL")
    val tallasCalzado = (36..42).map { it.toString() }
    val tallas = if (tipoProducto == "ROPA") tallasRopa else tallasCalzado

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Panel Administrador",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                actions = {
                    Button(onClick = onLogout) {
                        Text("Cerrar Sesión", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    ) { padding ->



        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF650099).copy(alpha = 0.1f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF650099))
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Gestión de Personal", fontWeight = FontWeight.Bold, color = Color(0xFF650099))
                            Text("Registra nuevas cuentas de administrador", fontSize = 12.sp)
                        }
                        Button(
                            onClick = onNavigateToCrearAdmin,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF650099))
                        ) {
                            Text("CREAR ADMIN")
                        }
                    }
                }
            }

            item {
                Text(
                    "Agregar Producto",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {

                        Text(
                            "DATOS DEL PRODUCTO",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        CustomInput("NOMBRE DEL PRODUCTO", nombre) { nombre = it }
                        CustomInput("DESCRIPCIÓN", descripcion) { descripcion = it }

                        CustomInput(
                            "PRECIO",
                            precioText,
                            KeyboardType.Decimal
                        ) {
                            precioText = it.filter { ch -> ch.isDigit() || ch == '.' }
                        }

                        CustomInput("CATEGORÍA", categoria) { categoria = it }
                        CustomInput("COLOR", color) { color = it }

                        Divider()

                        Text("TIPO DE PRODUCTO", fontWeight = FontWeight.Bold)

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = tipoProducto == "ROPA",
                                onClick = {
                                    tipoProducto = "ROPA"
                                    stockPorTalla.clear()
                                }
                            )
                            Text("ROPA", fontWeight = FontWeight.Bold)

                            Spacer(Modifier.width(16.dp))

                            RadioButton(
                                selected = tipoProducto == "CALZADO",
                                onClick = {
                                    tipoProducto = "CALZADO"
                                    stockPorTalla.clear()
                                }
                            )
                            Text("CALZADO", fontWeight = FontWeight.Bold)
                        }

                        Divider()

                        Text("STOCK POR TALLA", fontWeight = FontWeight.Bold)

                        tallas.forEach { talla ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = stockPorTalla.containsKey(talla),
                                    onCheckedChange = { checked ->
                                        if (checked) stockPorTalla[talla] = ""
                                        else stockPorTalla.remove(talla)
                                    }
                                )

                                Text(
                                    talla,
                                    modifier = Modifier.width(50.dp),
                                    fontWeight = FontWeight.Bold
                                )

                                if (stockPorTalla.containsKey(talla)) {
                                    OutlinedTextField(
                                        value = stockPorTalla[talla] ?: "",
                                        onValueChange = {
                                            stockPorTalla[talla] =
                                                it.filter { ch -> ch.isDigit() }
                                        },
                                        label = {
                                            Text("CANTIDAD", fontWeight = FontWeight.Bold)
                                        },
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Number
                                        ),
                                        modifier = Modifier.width(140.dp)
                                    )
                                }
                            }
                        }

                        Divider()

                        Text("IMAGEN DEL PRODUCTO", fontWeight = FontWeight.Bold)

                        Button(
                            onClick = { filePicker.launch("image/*") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("SELECCIONAR IMAGEN", fontWeight = FontWeight.Bold)
                        }

                        if (imagen.isNotBlank()) {
                            Image(
                                painter = rememberAsyncImagePainter(imagen),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(140.dp)
                                    .align(Alignment.CenterHorizontally),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        Button(
                            onClick = {
                                val producto = Producto(
                                    id = 0,
                                    nombre = nombre,
                                    descripcion = descripcion,
                                    precio = precio!!,
                                    categoria = categoria,
                                    size = null,
                                    color = color,
                                    imagenUrl = imagen,
                                    stockPorTalla = stockPorTalla.mapValues {
                                        it.value.toInt()
                                    }
                                )

                                viewModel.agregarProducto(producto)

                                nombre = ""
                                descripcion = ""
                                precioText = ""
                                categoria = ""
                                color = ""
                                imagen = ""
                                stockPorTalla.clear()
                            },
                            enabled = camposCompletos,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF6A1B9A),
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                "GUARDAR PRODUCTO",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    "PRODUCTOS (${productosList.size})",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(productosList, key = { it.id }) { prod ->
                ProductoAdminItem(
                    producto = prod,
                    onDelete = { viewModel.eliminarProducto(prod.id) }
                )
            }
        }
    }
}



/* ========================================================= */

@Composable
fun CustomInput(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onChange(it) },
        label = {
            Text(label, fontWeight = FontWeight.Bold)
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ProductoAdminItem(
    producto: Producto,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(producto.nombre, fontWeight = FontWeight.Bold)
                Text("PRECIO: $${producto.precio}", fontWeight = FontWeight.Bold)
                Text(
                    "STOCK: ${
                        producto.stockPorTalla?.entries?.joinToString {
                            "${it.key}:${it.value}"
                        } ?: "SIN STOCK"
                    }",
                    fontWeight = FontWeight.Bold
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color.Red
                )
            }
        }
    }
}
