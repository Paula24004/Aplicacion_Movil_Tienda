package com.example.apptiendadeportiva_grupo10.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.apptiendadeportiva_grupo10.viewmodel.CatalogoViewModel
import androidx.compose.runtime.collectAsState
import com.example.apptiendadeportiva_grupo10.viewmodel.CarritoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    navController: NavController,
    viewModel: CatalogoViewModel,
    carritoViewModel: CarritoViewModel
) {

    val context = LocalContext.current

    val productos by viewModel.productos.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarProductos(context)
    }

    Scaffold(
        topBar = {

            TopAppBar(
                title = { Text("Catálogo") }
            )
        }
    ) { padding ->
        Box(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when {
                loading -> Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

                error != null -> Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: $error")
                }

                else -> LazyColumn(
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(productos, key = { it.id }) { p ->
                        ProductoCard(producto = p) {
                            navController.navigate("detalle/${p.id}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductoCard(
    producto: com.example.apptiendadeportiva_grupo10.model.ProductoEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            val painter = rememberAsyncImagePainter(producto.imagen)

            Image(
                painter = painter,
                contentDescription = producto.nombre,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(producto.nombre, style = MaterialTheme.typography.titleMedium)

                Text(
                    "Precio: $${String.format("%,d", producto.precio).replace(',', '.')}",
                    style = MaterialTheme.typography.bodyMedium
                )

                producto.descripcion?.let {
                    Text(it, maxLines = 2)
                }
            }
        }
    }
}

