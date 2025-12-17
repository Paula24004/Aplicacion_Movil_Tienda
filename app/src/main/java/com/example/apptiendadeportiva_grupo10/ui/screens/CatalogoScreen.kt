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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset

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

    // --- ESTADO DE FILTRADO ---
    var categoriaSeleccionada by remember { mutableStateOf("Todos") }

    // Obtenemos las categorías únicas de la lista de productos
    val categorias = remember(productos) {
        val listaUnica = productos.mapNotNull { it.categoria?.trim() }.distinct().sorted()
        listOf("Todos") + listaUnica
    }

    // Lista filtrada que se mostrará
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
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
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8A2BE2),
                        contentColor = Color.White
                    )
                ) {
                    Text("VOLVER", fontWeight = FontWeight.Bold)
                }
            }
        },
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Catálogo", color = Color.White, fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF8A2BE2)
                    ),
                    actions = {
                        if (uiState.esAdmin) {
                            IconButton(onClick = { navController.navigate("admin_panel") }) {
                                Icon(
                                    imageVector = Icons.Default.Build,
                                    contentDescription = "Panel de Admin",
                                    tint = Color.Red
                                )
                            }
                        }
                        IconButton(onClick = { navController.navigate("perfil") }) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Mi Perfil",
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = { navController.navigate("carrito") }) {
                            Icon(
                                imageVector = Icons.Filled.ShoppingCart,
                                contentDescription = "Carrito",
                                tint = Color.White
                            )
                        }
                    }
                )

                // --- FILTRO DE CATEGORÍAS ---
                ScrollableTabRow(
                    selectedTabIndex = categorias.indexOf(categoriaSeleccionada),
                    containerColor = Color(0xFF8A2BE2),
                    contentColor = Color.White,
                    edgePadding = 16.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(tabPositions[categorias.indexOf(categoriaSeleccionada)]),
                            color = Color.White,
                            height = 3.dp
                        )
                    },
                    divider = {}
                ) {
                    categorias.forEach { cat ->
                        Tab(
                            selected = categoriaSeleccionada == cat,
                            onClick = { categoriaSeleccionada = cat },
                            text = {
                                Text(
                                    text = cat.uppercase(),
                                    fontSize = 12.sp,
                                    fontWeight = if (categoriaSeleccionada == cat) FontWeight.ExtraBold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when {
                loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF8A2BE2))
                }

                error != null -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: $error", color = Color.Red)
                }

                productosFiltrados.isEmpty() -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay productos en esta categoría", fontWeight = FontWeight.Medium)
                }

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
fun ProductoCard(
    producto: ProductoEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F1F5)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val painter = rememberAsyncImagePainter(producto.imagenUrl)
            Image(
                painter = painter,
                contentDescription = producto.nombre,
                modifier = Modifier.size(90.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Pequeña etiqueta de categoría
                Text(
                    text = producto.categoria?.uppercase() ?: "GENERAL",
                    fontSize = 10.sp,
                    color = Color(0xFF8A2BE2),
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = producto.nombre ?: "Producto",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                val precioFormateado = String.format("%,.0f", producto.precio ?: 0.0)
                    .replace(',', '.')

                Text(
                    text = "$$precioFormateado",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF4A148C)
                )

                producto.descripcion?.let {
                    Text(
                        text = it,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        maxLines = 1
                    )
                }
            }
        }
    }
}