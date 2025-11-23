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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(
    productoId: Int,
    viewModel: CatalogoViewModel,
    carritoViewModel: CarritoViewModel,
    navController: NavHostController
) {
    val contexto = LocalContext.current

    var producto by remember { mutableStateOf<com.example.apptiendadeportiva_grupo10.model.ProductoEntity?>(null) }

    LaunchedEffect(productoId) {
        producto = viewModel.buscarPorId(contexto, productoId)
    }

    Scaffold(topBar = { TopAppBar(title = { Text(producto?.nombre ?: "Detalle") }) }) { padd ->
        producto?.let { p ->
            Column(Modifier.padding(padd).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Image(
                    painter = rememberAsyncImagePainter(p.imagen),
                    contentDescription = p.nombre,
                    modifier = Modifier.fillMaxWidth().height(220.dp),
                    contentScale = ContentScale.Crop
                )
                Text(p.nombre, style = MaterialTheme.typography.titleLarge)
                Text("Precio: $${String.format("%,d", p.precio).replace(',', '.')}", style = MaterialTheme.typography.titleMedium)
                Text(p.descripcion ?: "", style = MaterialTheme.typography.bodyMedium)
                Button(onClick = { /* Ustedes ya hicieron un carrito, tengo Fe */ }, modifier = Modifier.fillMaxWidth()) {
                    Text("Agregar al carrito")
                }
            }
        } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Producto no encontrado")
        }
    }
}
