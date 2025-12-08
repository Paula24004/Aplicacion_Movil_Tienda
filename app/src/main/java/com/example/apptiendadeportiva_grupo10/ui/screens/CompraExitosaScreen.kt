package com.example.apptiendadeportiva_grupo10.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.apptiendadeportiva_grupo10.viewmodel.AuthViewModel
import com.example.apptiendadeportiva_grupo10.viewmodel.CarritoViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun CompraExitosaScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    carritoViewModel: CarritoViewModel,

)


 {
    val formato = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")) }

    // Datos finales
    val subtotal = carritoViewModel.total.collectAsState().value
    val iva = subtotal * 0.19
    val total = subtotal + iva

    // Dirección actual
    val direccionActual = authViewModel.uiState

    Scaffold(

        // ⭐ BOTÓN VOLVER ABAJO
        bottomBar = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Button(
                    onClick = {
                        navController.navigate("carrito") {
                            popUpTo("compra_exitosa") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8A2BE2), // Morado
                        contentColor = Color.White
                    )
                ) {
                    Text("VOLVER")
                }
            }
        },

        topBar = {
            TopAppBar(
                title = { Text("Compra Exitosa", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF673AB7)
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                "¡Pago realizado con éxito!",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Subtotal: ${formato.format(subtotal)}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("IVA (19%): ${formato.format(iva)}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("TOTAL: ${formato.format(total)}", fontSize = 22.sp, fontWeight = FontWeight.Black)

            Spacer(modifier = Modifier.height(28.dp))

            Text("Dirección de despacho:", fontSize = 22.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "${direccionActual.direccion}, ${direccionActual.comuna}, ${direccionActual.region}",
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { navController.navigate("editar_direccion") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF673AB7),
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Añadir nueva dirección", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
