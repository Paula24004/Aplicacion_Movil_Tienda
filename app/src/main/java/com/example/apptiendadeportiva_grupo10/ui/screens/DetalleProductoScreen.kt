package com.example.apptiendadeportiva_grupo10.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.apptiendadeportiva_grupo10.model.Producto
import com.example.apptiendadeportiva_grupo10.viewmodel.CarritoViewModel
import com.example.apptiendadeportiva_grupo10.viewmodel.CatalogoViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleProductoScreen(productoId: Int,
                          catalogoViewModel: CatalogoViewModel,
                          carritoViewModel: CarritoViewModel = viewModel()
                        ) {
    val producto: Producto? = remember { catalogoViewModel.buscarProductoPorId(productoId) }
    val formato = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")) }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Detalle del Producto") })
    }) { padding ->
        producto?.let { p ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start

                 ) {
                val painter = rememberAsyncImagePainter(p.imagen)
                Image(painter = painter, contentDescription = p.nombre, modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                //nombre
                Text(
                    text = p.nombre,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                //precio
                Text(
                    text = formato.format(p.precio),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // Descripción
                Text(
                    text = p.descripcion ?: "Sin descripción disponible.",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.weight(1f)) // Empuja el botón hacia abajo

                // Botón AGREGAR AL CARRITO (Conexión clave)
                Button(
                    onClick = {
                        // LLAMADA CLAVE: Agrega el objeto Producto al CarritoViewModel
                        carritoViewModel.agregar(p)
                        // Opcional: mostrar un Snackbar o navegar a la vista del carrito
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("🛒 Agregar al carrito", fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error: Producto no encontrado")
        }
    }
}


