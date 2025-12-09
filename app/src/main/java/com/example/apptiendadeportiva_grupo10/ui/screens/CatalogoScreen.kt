package com.example.apptiendadeportiva_grupo10.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.apptiendadeportiva_grupo10.viewmodel.CatalogoViewModel
import com.example.apptiendadeportiva_grupo10.viewmodel.CarritoViewModel
import com.example.apptiendadeportiva_grupo10.viewmodel.AuthViewModel
import com.example.apptiendadeportiva_grupo10.model.ProductoEntity
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    navController: NavController,
    viewModel: CatalogoViewModel,
    carritoViewModel: CarritoViewModel,
    authViewModel: AuthViewModel   // ⭐ AGREGADO PARA VALIDAR LOGIN
) {

    val context = LocalContext.current
    val productos by viewModel.productos.collectAsState()
    val loading: Boolean by viewModel.loading.collectAsState(initial = false)
    val error: String? by viewModel.error.collectAsState(initial = null)

    LaunchedEffect(Unit) {
        viewModel.cargarProductos(context)
    }

    Scaffold(

        // ⭐ BOTÓN VOLVER CON LÓGICA DE LOGIN
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Button(
                    onClick = {

                        if (authViewModel.isLoggedIn) {
                            // Usuarios logeados vuelven al HOME
                            navController.navigate("home") {
                                popUpTo("catalogo") { inclusive = true }
                            }


                    } else {
                            // ⭐ Usuario NO logeado → volver al HomeScreen
                            navController.navigate("home") {
                                popUpTo("catalogo") { inclusive = true }
                            }
                        }

                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8A2BE2),
                        contentColor = Color.White
                    )
                ) {
                    Text("VOLVER")
                }
            }
        },

        topBar = {
            TopAppBar(
                title = { Text("Catálogo", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF8A2BE2)
                ),
                actions = {
                    IconButton(onClick = { navController.navigate("carrito") }) {
                        Icon(
                            imageVector = Icons.Filled.ShoppingCart,
                            contentDescription = "Carrito",
                            tint = Color.White
                        )
                    }
                }
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
                ) { CircularProgressIndicator() }

                error != null -> Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { Text("Error: $error") }

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
    producto: ProductoEntity,
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

            val painter = rememberAsyncImagePainter(producto.imagenUrl)

            Image(
                painter = painter,
                contentDescription = producto.nombre,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {

                Text(
                    producto.nombre ?: "Nombre Desconocido",
                    style = MaterialTheme.typography.titleMedium
                )

                val precioFormateado = String.format("%,.0f", producto.precio ?: 0.0)
                    .replace(',', '.')

                Text(
                    "Precio: $$precioFormateado",
                    style = MaterialTheme.typography.bodyMedium
                )

                producto.descripcion?.let {
                    Text(it, maxLines = 2)
                }
            }
        }
    }
}
