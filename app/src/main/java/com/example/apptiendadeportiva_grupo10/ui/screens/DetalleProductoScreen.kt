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
import com.example.apptiendadeportiva_grupo10.data.remote.RetrofitClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    productoId: Int,
    viewModel: CatalogoViewModel,
    carritoViewModel: CarritoViewModel,
    navController: NavHostController
) {
    val contexto = LocalContext.current

    var producto by remember { mutableStateOf<ProductoEntity?>(null) }
    var quantity by remember { mutableStateOf(1) }
    var selectedSize by remember { mutableStateOf<String?>(null) }

    // ESTADOS PARA CONVERSIÓN
    var precioUsd by remember { mutableStateOf<Double?>(null) }
    var precioEur by remember { mutableStateOf<Double?>(null) }
    var convirtiendo by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(productoId) {
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
            val stockMap = p.stockPorTalla ?: emptyMap()
            val availableSizes = stockMap.keys.toList().sorted()

            val totalStock = stockMap.values.sum()
            val stockForSelectedSize = selectedSize?.let { stockMap[it] } ?: 0

            LaunchedEffect(availableSizes) {
                if (availableSizes.isNotEmpty() && selectedSize == null) {
                    selectedSize = availableSizes.firstOrNull { (stockMap[it] ?: 0) > 0 }
                        ?: availableSizes.first()
                }
            }

            val isAddToCartEnabled =
                selectedSize != null && stockForSelectedSize > 0 && quantity <= stockForSelectedSize

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padd)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.Start
            ) {

                // IMAGEN
                Image(
                    painter = rememberAsyncImagePainter(p.imagenUrl),
                    contentDescription = p.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .padding(bottom = 16.dp),
                    contentScale = ContentScale.Crop
                )

                Text(p.nombre ?: "Nombre desconocido", style = MaterialTheme.typography.titleLarge)
                Text(p.descripcion ?: "", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(bottom = 16.dp))

                // PRECIO FORMATEADO CLP (SIN DECIMALES)
                val precioFormateado = String.format("%,.0f", p.precio ?: 0.0).replace(',', '.')
                Text(
                    "Precio: $$precioFormateado",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // ⭐ BOTÓN CONVERSOR
                Button(
                    onClick = {
                        convirtiendo = true
                        val precioClp = (p.precio ?: 0.0).toInt()

                        coroutineScope.launch {
                            try {
                                val usd = RetrofitClient.apiService.convertir(precioClp, "USD")
                                val eur = RetrofitClient.apiService.convertir(precioClp, "EUR")

                                precioUsd = String.format("%.2f", usd).toDouble()
                                precioEur = String.format("%.2f", eur).toDouble()

                            } catch (e: Exception) {
                                precioUsd = null
                                precioEur = null
                            } finally {
                                convirtiendo = false
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text("Convertir precio a USD y EUR")
                }

                if (convirtiendo) {
                    Text("Convirtiendo...", color = Color.Gray)
                }

                precioUsd?.let {
                    Text("USD: $${it}", style = MaterialTheme.typography.bodyLarge)
                }

                precioEur?.let {
                    Text("EUR: €${it}", style = MaterialTheme.typography.bodyLarge)
                }

                // STOCK TOTAL
                Text(
                    "Stock total disponible: $totalStock unidades",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 16.dp)
                )

                // SELECTOR DE TALLA
                if (availableSizes.isNotEmpty()) {
                    Text("Seleccionar Talla:", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        availableSizes.forEach { size ->
                            val isSelected = size == selectedSize
                            val sizeStock = stockMap[size] ?: 0
                            val isEnabled = sizeStock > 0

                            OutlinedButton(
                                onClick = { selectedSize = size; quantity = 1 },
                                enabled = isEnabled,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent
                                ),
                                border = BorderStroke(1.dp, if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(size)
                            }
                        }
                    }

                    if (selectedSize != null) {
                        Text(
                            "Stock para talla $selectedSize: $stockForSelectedSize unidades",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }

                // SELECTOR DE CANTIDAD
                if (stockForSelectedSize > 0) {
                    Row(
                        Modifier.fillMaxWidth().padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Cantidad:", style = MaterialTheme.typography.bodyLarge)
                        Spacer(Modifier.width(16.dp))

                        Button(onClick = { if (quantity > 1) quantity-- }, enabled = quantity > 1) { Text("-") }
                        Text("$quantity", modifier = Modifier.padding(horizontal = 16.dp), style = MaterialTheme.typography.bodyLarge)
                        Button(onClick = { if (quantity < stockForSelectedSize) quantity++ }, enabled = quantity < stockForSelectedSize) { Text("+") }
                    }
                }

                // BOTÓN AGREGAR AL CARRITO
                Button(
                    onClick = {
                        carritoViewModel.agregar(productDomain, selectedSize ?: "Desconocida", quantity)
                        navController.navigate("carrito")
                    },
                    enabled = isAddToCartEnabled,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Agregar $quantity unid. (Talla $selectedSize) a Carrito")
                }
            }

        } ?: Box(
            Modifier.fillMaxSize().padding(padd),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
            Text("Cargando producto…", modifier = Modifier.padding(top = 50.dp))
        }
    }
}
