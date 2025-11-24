package com.example.apptiendadeportiva_grupo10.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.apptiendadeportiva_grupo10.viewmodel.CarritoViewModel
import com.example.apptiendadeportiva_grupo10.viewmodel.CatalogoViewModel
import com.example.apptiendadeportiva_grupo10.model.ProductoEntity // Importar ProductoEntity para seguridad
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

    var producto by remember { mutableStateOf<ProductoEntity?>(null) } // Usamos ProductoEntity directamente

    LaunchedEffect(productoId) {
        // Aseguramos que solo buscamos si el productoId es válido, aunque Compose ya lo garantiza
        producto = viewModel.buscarPorId(contexto, productoId)
    }

    Scaffold(topBar = { TopAppBar(title = { Text(producto?.nombre ?: "Detalle") }) }) { padd ->
        producto?.let { p ->
            Column(Modifier.padding(padd).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Image(
                    painter = rememberAsyncImagePainter(p.imagenUrl),
                    contentDescription = p.nombre,
                    modifier = Modifier.fillMaxWidth().height(220.dp),
                    contentScale = ContentScale.Crop
                )
                Text(p.nombre ?: "Nombre desconocido", style = MaterialTheme.typography.titleLarge)

                // CORRECCIÓN CRÍTICA: Usamos "%,.0f" (flotante sin decimales) en lugar de "%,d" (entero)
                val precioFormateado = String.format("%,.0f", p.precio ?: 0.0).replace(',', '.')
                Text(
                    "Precio: $$precioFormateado",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(p.descripcion ?: "", style = MaterialTheme.typography.bodyMedium)

                // Botón de ejemplo para agregar al carrito (debes conectar la lógica de CarritoViewModel aquí)
                Button(
                    onClick = {
                        // Ejemplo: buscar el producto en formato de dominio para agregarlo.
                        val productoDomain = p.toDomain()
                        carritoViewModel.agregar(productoDomain, "M", 1) // Asume talla "M" por defecto
                        navController.navigate("carrito")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Agregar al carrito")
                }
            }
        } ?: Box(Modifier.fillMaxSize().padding(padd), contentAlignment = Alignment.Center) {
            Text("Cargando o Producto no encontrado")
        }
    }
}