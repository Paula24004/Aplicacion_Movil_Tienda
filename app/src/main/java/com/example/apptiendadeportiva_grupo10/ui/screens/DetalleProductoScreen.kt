package com.example.apptiendadeportiva_grupo10.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.apptiendadeportiva_grupo10.model.Producto
import com.example.apptiendadeportiva_grupo10.viewmodel.CarritoViewModel
import com.example.apptiendadeportiva_grupo10.viewmodel.CatalogoViewModel
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.foundation.layout.Arrangement

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    navController: NavController,
    productoId: Int,
    catalogoViewModel: CatalogoViewModel,
    carritoViewModel: CarritoViewModel
) {
    // Lee siempre el estado actual del producto (sin remember) para reflejar cambios de stock
    val producto: Producto? = catalogoViewModel.buscarProductoPorId(productoId)
    val formato = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")) }
    var tallaSeleccionada by remember { mutableStateOf<String?>(null) }
    var mensajeStock by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Detalle del Producto") }) }
    ) { padding ->
        producto?.let { p ->
            val tallasDisponibles = p.stockPorTalla.keys.toList().sorted()
            val stockActual = tallaSeleccionada?.let { p.stockPorTalla[it] } ?: 0
            val hayStock = stockActual > 0

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                val painter = rememberAsyncImagePainter(p.imagen)
                Image(
                    painter = painter,
                    contentDescription = p.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = p.nombre,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = formato.format(p.precio),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                Text(
                    text = p.descripcion ?: "Sin descripción disponible.",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Selecciona la talla:", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(tallasDisponibles) { talla ->
                        val stockParaTalla = p.stockPorTalla[talla] ?: 0
                        val isSelected = talla == tallaSeleccionada
                        val isEnabled = stockParaTalla > 0

                        val borderColor = when {
                            !isEnabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                            isSelected -> MaterialTheme.colorScheme.primary
                            else -> Color.Gray
                        }
                        val containerColor =
                            if (isSelected) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surface
                        val labelColor =
                            if (isEnabled) MaterialTheme.colorScheme.onSurface else Color.Gray

                        AssistChip(
                            onClick = {
                                if (isEnabled) {
                                    tallaSeleccionada = talla
                                    mensajeStock = null
                                }
                            },
                            label = {
                                Text(
                                    talla,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            enabled = isEnabled,
                            border = BorderStroke(2.dp, borderColor),
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = containerColor,
                                labelColor = labelColor,
                                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (tallaSeleccionada != null) {
                        "Stock disponible para talla $tallaSeleccionada: $stockActual unidades"
                    } else {
                        "Por favor, selecciona una talla."
                    },
                    color = when {
                        tallaSeleccionada == null -> MaterialTheme.colorScheme.onSurface
                        hayStock -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.error
                    },
                    style = MaterialTheme.typography.bodyLarge
                )

                if (mensajeStock != null) {
                    Text(
                        mensajeStock!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Agregar al carrito
                Button(
                    onClick = {
                        if (tallaSeleccionada == null) {
                            mensajeStock = "Debes seleccionar una talla."
                        } else if (!hayStock) {
                            mensajeStock = "Stock agotado para la talla seleccionada."
                        } else {
                            val ok = catalogoViewModel.reducirStock(p.id, tallaSeleccionada!!, 1)
                            if (ok) {
                                carritoViewModel.agregar(p, tallaSeleccionada!!)
                                mensajeStock = "Agregado al carrito: Talla $tallaSeleccionada"
                            } else {
                                mensajeStock = "Error al agregar el producto."
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = tallaSeleccionada != null && hayStock
                ) {
                    Text("Agregar al carrito", fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Ver carrito
                OutlinedButton(
                    onClick = { navController.navigate("carrito") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("Ver carrito")
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        } ?: Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Error: Producto no encontrado")
        }
    }
}
