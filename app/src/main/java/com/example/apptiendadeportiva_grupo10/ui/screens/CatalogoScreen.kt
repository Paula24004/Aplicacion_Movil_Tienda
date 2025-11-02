package com.example.apptiendadeportiva_grupo10.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.apptiendadeportiva_grupo10.model.Producto
import com.example.apptiendadeportiva_grupo10.viewmodel.CatalogoViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    navController: NavController,
    viewModel: CatalogoViewModel
) {
    val productos by viewModel.productos.collectAsState()

    LaunchedEffect(Unit) {
        if (productos.isEmpty()) viewModel.cargar()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Catálogo") }) }
    ) { padding ->
        if (productos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(180.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(productos) { p ->
                    ProductoCard(p) {
                        navController.navigate("detalle/${p.id}")
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductoCard(
    producto: Producto,
    onClick: () -> Unit
) {
    val formato = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")) }
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 260.dp)
            .clickable { onClick() }
    ) {
        Column(Modifier.fillMaxSize()) {
            val painter = rememberAsyncImagePainter(producto.imagen)
            Image(
                painter = painter,
                contentDescription = producto.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(8.dp))
            Column(Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = formato.format(producto.precio),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(8.dp))

                val totalDisponible = producto.stockPorTalla.values.sum()
                Text(
                    text = "Unidades disponibles: $totalDisponible",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
