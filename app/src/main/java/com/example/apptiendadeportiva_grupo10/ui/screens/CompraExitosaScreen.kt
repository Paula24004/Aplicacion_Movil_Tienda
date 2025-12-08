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
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompraExitosaScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    carritoViewModel: CarritoViewModel,
    totalRecibido: Double
) {
    val formato = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")) }
    val uiState = authViewModel.uiState

    val subtotal = totalRecibido

    val iva = subtotal * 0.19
    val total = subtotal + iva

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
                        navController.navigate("carrito") {
                            popUpTo("compra_exitosa") { inclusive = true }
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
                title = { Text("Compra Exitosa", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF673AB7)
                )
            )
        }
    ) { padding ->

        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                "Pago realizado con éxito",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Subtotal: ${formato.format(subtotal)}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("IVA (19%): ${formato.format(iva)}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Total: ${formato.format(total)}", fontSize = 22.sp, fontWeight = FontWeight.Black)

            Spacer(modifier = Modifier.height(28.dp))

            Text("Dirección registrada:", fontSize = 22.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "${uiState.region}, ${uiState.comuna}, ${uiState.direccion}",
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

            if (authViewModel.nuevaDireccion.isNotBlank()) {

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    "Nueva dirección añadida:",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    color = Color(0xFFD1C4E9),
                    tonalElevation = 4.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {

                        Text(
                            "Región: ${authViewModel.nuevaRegion}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "Comuna: ${authViewModel.nuevaComuna}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "Dirección: ${authViewModel.nuevaDireccion}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    "Seleccione dirección de despacho:",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = !authViewModel.usarNuevaDireccion,
                        onClick = { authViewModel.usarNuevaDireccion = false }
                    )
                    Text("Usar dirección registrada")
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = authViewModel.usarNuevaDireccion,
                        onClick = { authViewModel.usarNuevaDireccion = true }
                    )
                    Text("Usar nueva dirección añadida")
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Dirección seleccionada para su despacho"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8A2BE2),
                        contentColor = Color.White
                    )
                ) {
                    Text("ACEPTAR", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
