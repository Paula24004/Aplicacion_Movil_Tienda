package com.example.apptiendadeportiva_grupo10.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.apptiendadeportiva_grupo10.viewmodel.CatalogoViewModel
import com.example.apptiendadeportiva_grupo10.viewmodel.CarritoViewModel
import com.example.apptiendadeportiva_grupo10.viewmodel.AuthViewModel
import com.example.apptiendadeportiva_grupo10.model.ProductoEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    navController: NavController,
    viewModel: CatalogoViewModel,
    carritoViewModel: CarritoViewModel,
    authViewModel: AuthViewModel
) {
    val uiState = authViewModel.uiState
    val context = LocalContext.current
    val productos by viewModel.productos.collectAsState()
    val loading by viewModel.loading.collectAsState(initial = false)
    val error by viewModel.error.collectAsState(initial = null)

    var categoriaSeleccionada by remember { mutableStateOf("Todos") }

    val categorias = remember(productos) {
        val listaUnica = productos.mapNotNull { it.categoria?.trim() }.distinct().sorted()
        listOf("Todos") + listaUnica
    }

    val productosFiltrados = remember(productos, categoriaSeleccionada) {
        if (categoriaSeleccionada == "Todos") {
            productos
        } else {
            productos.filter { it.categoria == categoriaSeleccionada }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.cargarProductos(context)
    }

    Scaffold(
        bottomBar = {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        if (authViewModel.isLoggedIn) {
                            navController.popBackStack()
                        } else {
                            navController.navigate("home") {
                                popUpTo("catalogo") { inclusive = true }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A2BE2))
                ) {
                    Text("VOLVER", fontWeight = FontWeight.Bold)
                }
            }
        },
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Catálogo", color = Color.White, fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF8A2BE2)),
                    actions = {
                        if (uiState.esAdmin) {
                            IconButton(onClick = { navController.navigate("admin_panel") }) {
                                Icon(Icons.Default.Build, "Admin", tint = Color.Red)
                            }
                        }
                        IconButton(onClick = { navController.navigate("perfil") }) {
                            Icon(Icons.Default.AccountCircle, "Perfil", tint = Color.White)
                        }
                        IconButton(onClick = { navController.navigate("carrito") }) {
                            Icon(Icons.Default.ShoppingCart, "Carrito", tint = Color.White)
                        }
                    }
                )

                ScrollableTabRow(
                    selectedTabIndex = categorias.indexOf(categoriaSeleccionada),
                    containerColor = Color(0xFF8A2BE2),
                    contentColor = Color.White,
                    edgePadding = 16.dp
                ) {
                    categorias.forEach { cat ->
                        Tab(
                            selected = categoriaSeleccionada == cat,
                            onClick = { categoriaSeleccionada = cat },
                            text = { Text(cat.uppercase(), fontSize = 12.sp) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when {
                loading -> CircularProgressIndicator(Modifier.align(Alignment.Center), color = Color(0xFF8A2BE2))
                error != null -> Text("Error: $error", Modifier.align(Alignment.Center), color = Color.Red)
                else -> LazyColumn(
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(productosFiltrados, key = { it.id }) { producto ->
                        ProductoCard(producto = producto) {
                            navController.navigate("detalle/${producto.id}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductoCard(producto: ProductoEntity, onClick: () -> Unit) {
    // Lógica para decodificar Base64
    val bitmap = remember(producto.imagenUrl) {
        producto.imagenUrl?.let { url ->
            if (url.startsWith("data:image")) {
                try {
                    val base64Data = url.substringAfter(",")
                    val decodedBytes = Base64.decode(base64Data, Base64.DEFAULT)
                    BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                } catch (e: Exception) { null }
            } else null
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F1F5)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {

            // IMAGEN DINÁMICA
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(90.dp).clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = rememberAsyncImagePainter(producto.imagenUrl),
                    contentDescription = null,
                    modifier = Modifier.size(90.dp).clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(producto.categoria?.uppercase() ?: "GENERAL", fontSize = 10.sp, color = Color(0xFF8A2BE2), fontWeight = FontWeight.Bold)
                Text(producto.nombre ?: "Producto", fontSize = 19.sp, fontWeight = FontWeight.Bold)
                val precio = String.format("%,.0f", producto.precio ?: 0.0).replace(',', '.')
                Text(text = "$$precio", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF4A148C))
            }
        }
    }
}