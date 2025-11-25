package com.example.apptiendadeportiva_grupo10.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.apptiendadeportiva_grupo10.viewmodel.CarritoViewModel
import com.example.apptiendadeportiva_grupo10.viewmodel.CatalogoViewModel
import com.example.apptiendadeportiva_grupo10.model.ProductoEntity
import com.example.apptiendadeportiva_grupo10.model.toDomain

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    productoId: Int,
    viewModel: CatalogoViewModel,
    carritoViewModel: CarritoViewModel,
    navController: NavHostController
) {
    val contexto = LocalContext.current

    // ESTADO LOCAL DE LA PANTALLA
    var producto by remember { mutableStateOf<ProductoEntity?>(null) }
    var quantity by remember { mutableStateOf(1) } // Cantidad deseada
    var selectedSize by remember { mutableStateOf<String?>(null) } // Talla seleccionada

    LaunchedEffect(productoId) {
        // La firma del ViewModel requiere el contexto
        producto = viewModel.getProductoById(contexto, productoId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(producto?.nombre ?: "Detalle del Producto") }
            )
        }
    ) { padd ->
        producto?.let { p ->
            val productDomain = p.toDomain()

            // ✅ CORRECCIÓN CRÍTICA: Usamos el nuevo campo stockPorTalla (asumiendo Map<String, Int> en ProductoEntity)
            val stockMap = p.stockPorTalla ?: emptyMap()
            val availableSizes = stockMap.keys.toList().sorted()

            // Calcular stock total y stock para la talla seleccionada
            val totalStock = stockMap.values.sum()
            val stockForSelectedSize = selectedSize?.let { stockMap[it] } ?: 0

            // Inicialización: Establecer la primera talla con stock como seleccionada al cargar.
            LaunchedEffect(availableSizes) {
                if (availableSizes.isNotEmpty() && selectedSize == null) {
                    selectedSize = availableSizes.firstOrNull { (stockMap[it] ?: 0) > 0 } ?: availableSizes.first()
                }
            }

            // Habilitación del botón "Agregar al carrito"
            val isAddToCartEnabled = selectedSize != null && stockForSelectedSize > 0 && quantity > 0 && quantity <= stockForSelectedSize

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padd)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                // Imagen
                Image(
                    painter = rememberAsyncImagePainter(p.imagenUrl),
                    contentDescription = p.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .padding(bottom = 16.dp),
                    contentScale = ContentScale.Crop
                )

                // Nombre y Descripción
                Text(p.nombre ?: "Nombre desconocido", style = MaterialTheme.typography.titleLarge)
                Text(p.descripcion ?: "", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(bottom = 16.dp))

                // Precio
                val precioFormateado = String.format("%,.0f", p.precio ?: 0.0).replace(',', '.')
                Text(
                    "Precio: $$precioFormateado",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // ----------------------------------------------------
                // 1. INFORMACIÓN DE STOCK TOTAL
                // ----------------------------------------------------

                Text(
                    "Stock total disponible: $totalStock unidades",
                    style = MaterialTheme.typography.bodyMedium,
                    color = when {
                        totalStock == 0 -> MaterialTheme.colorScheme.error
                        totalStock < 5 -> MaterialTheme.colorScheme.tertiary
                        else -> MaterialTheme.colorScheme.onSurface
                    },
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // ----------------------------------------------------
                // 2. SELECTOR DE TALLA
                // ----------------------------------------------------
                if (availableSizes.isNotEmpty()) {
                    Text("Seleccionar Talla:", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        availableSizes.forEach { size ->
                            val isSelected = size == selectedSize
                            val sizeStock = stockMap[size] ?: 0
                            val isEnabled = sizeStock > 0

                            OutlinedButton(
                                onClick = { selectedSize = size; quantity = 1 }, // Reset quantity when size changes
                                enabled = isEnabled,
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent,
                                    contentColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                    disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                ),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                                ),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(size)
                            }
                        }
                    }

                    // Mostrar stock específico de la talla seleccionada
                    if (selectedSize != null) {
                        Text(
                            "Stock para talla $selectedSize: $stockForSelectedSize unidades",
                            style = MaterialTheme.typography.bodySmall,
                            color = when {
                                stockForSelectedSize == 0 -> MaterialTheme.colorScheme.error
                                stockForSelectedSize < 5 -> MaterialTheme.colorScheme.tertiary
                                else -> MaterialTheme.colorScheme.onSurface
                            },
                            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                        )
                    } else if (totalStock > 0) {
                        Text("Selecciona una talla para continuar.", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(bottom = 16.dp))
                    }
                }

                // ----------------------------------------------------
                // 3. SELECTOR DE CANTIDAD
                // ----------------------------------------------------
                if (stockForSelectedSize > 0) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Cantidad:", style = MaterialTheme.typography.bodyLarge)
                        Spacer(Modifier.width(16.dp))

                        // Botón Decrementar
                        Button(
                            onClick = { if (quantity > 1) quantity-- },
                            enabled = quantity > 1,
                            shape = RoundedCornerShape(4.dp)
                        ) { Text("-") }

                        Text(
                            "$quantity",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )

                        // Botón Incrementar
                        Button(
                            onClick = { if (quantity < stockForSelectedSize) quantity++ }, // Usar stockForSelectedSize
                            enabled = quantity < stockForSelectedSize,
                            shape = RoundedCornerShape(4.dp)
                        ) { Text("+") }
                    }
                } else if (totalStock > 0 && selectedSize != null) {
                    Text("No hay stock para la talla seleccionada.", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(vertical = 8.dp))
                }


                // ----------------------------------------------------
                // 4. BOTÓN AGREGAR AL CARRITO
                // ----------------------------------------------------
                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = {
                        // Pasar la talla seleccionada al CarritoViewModel
                        val size = selectedSize ?: "Desconocida"
                        carritoViewModel.agregar(productDomain, size, quantity)
                        navController.navigate("carrito")
                    },
                    enabled = isAddToCartEnabled,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        when {
                            totalStock == 0 -> "Agotado (Sin Stock)"
                            selectedSize == null -> "Seleccionar Talla"
                            stockForSelectedSize == 0 -> "Agotado (Talla $selectedSize)"
                            else -> "Agregar $quantity unid. (Talla $selectedSize) a Carrito"
                        }
                    )
                }
            }
        } ?: Box(Modifier.fillMaxSize().padding(padd), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
            Text("Cargando Producto o ID no válido", modifier = Modifier.padding(top = 50.dp))
        }
    }
}