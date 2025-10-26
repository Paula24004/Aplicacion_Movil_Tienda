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
import com.example.apptiendadeportiva_grupo10.model.Producto // Asegúrate de que esta importación sea correcta

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAdmin(
    viewModel: AuthViewModel,
    onLogout: () -> Unit
) {
    // 1. Estados locales para el formulario de nuevo producto (CORREGIDO Y AMPLIADO)
    var nuevoNombre by remember { mutableStateOf("") }
    var nuevoPrecioText by remember { mutableStateOf("") }
    var nuevaDescripcion by remember { mutableStateOf("") } // ✅ NUEVO ESTADO
    var nuevaImagen by remember { mutableStateOf("") }      // ✅ NUEVO ESTADO

    // Accede a la lista reactiva de productos directamente desde el ViewModel
    val productosList = viewModel.listaProductos

    // Validar si todos los campos requeridos (asumiendo nombre, precio, descripción, imagen) están llenos
    val camposCompletos = nuevoNombre.isNotBlank() &&
            nuevoPrecioText.isNotBlank() &&
            nuevaDescripcion.isNotBlank() &&
            nuevaImagen.isNotBlank()

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

                    // Descripción del Producto (✅ NUEVO CAMPO)
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
                        onValueChange = { nuevoPrecioText = it.filter { it.isDigit() || it == '.' } },
                        label = { Text("Precio") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(Modifier.height(8.dp))

                    // URL/Ruta de Imagen (✅ NUEVO CAMPO)
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
                            val precio = nuevoPrecioText.toFloatOrNull()
                            // Lógica de validación
                            if (camposCompletos && precio != null && precio > 0) {

                                val nuevoId = (productosList.maxOfOrNull { it.id } ?: 0) + 1

                                // ✅ USO DE VARIABLES CORREGIDO
                                val nuevoProducto = Producto(
                                    id = nuevoId,
                                    nombre = nuevoNombre,
                                    descripcion = nuevaDescripcion, // Usando el nuevo estado
                                    precio = nuevoPrecioText.toInt(),
                                    imagen = nuevaImagen          // Usando el nuevo estado
                                    // Asegúrate de que los nombres de los parámetros (descripcion, imagen) coincidan con tu clase Producto
                                )
                                viewModel.agregarProducto(nuevoProducto)
                                // Limpiar campos después de agregar
                                nuevoNombre = ""
                                nuevoPrecioText = ""
                                nuevaDescripcion = ""
                                nuevaImagen = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = camposCompletos // Usando la validación completa
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
                    text = "ID: ${producto.id} | Precio: $${String.format("%.2f", producto.precio.toFloat())}",
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