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
import com.example.apptiendadeportiva_grupo10.viewmodel.GestionEnvioViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProcesoEnvioScreen(
    navController: NavController,
    viewModel: GestionEnvioViewModel,
    direccionDespacho: String,
    total: Double
) {
    val purple = Color(0xFF8A2BE2)

    // -------------------------
    // ESTADOS
    // -------------------------
    var agenciaSeleccionada by remember { mutableStateOf("Correos de Chile") }
    var estadoEnvio by remember { mutableStateOf("En espera de despacho") }

    val calendar = remember { Calendar.getInstance() }
    var fechaEnvio by remember { mutableStateOf(calendar.time) }

    val formatter = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Proceso de Envío",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF673AB7)
                )
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // ✅ CONFIRMAR ENVÍO
                Button(
                    onClick = {
                        viewModel.crearEnvio(
                            agencia = agenciaSeleccionada,
                            fecha = formatter.format(fechaEnvio),
                            estado = estadoEnvio,
                            direccion = direccionDespacho
                        )

                        navController.navigate(
                            "pedido_enviado/" +
                                    "${android.net.Uri.encode(agenciaSeleccionada)}/" +
                                    "${android.net.Uri.encode(formatter.format(fechaEnvio))}/" +
                                    "${android.net.Uri.encode(direccionDespacho)}/" +
                                    total
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = purple,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        "CONFIRMAR ENVÍO",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // 🔙 VOLVER A COMPRA EXITOSA
                Button(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = purple,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        "VOLVER",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(28.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // -------------------------
            // AGENCIA DE ENVÍO
            // -------------------------
            Text(
                "Seleccione agencia de envío",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = agenciaSeleccionada == "Correos de Chile",
                    onClick = { agenciaSeleccionada = "Correos de Chile" },
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text("Correos de Chile", fontSize = 20.sp)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = agenciaSeleccionada == "Starken Envíos",
                    onClick = { agenciaSeleccionada = "Starken Envíos" },
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text("Starken Envíos", fontSize = 20.sp)
            }

            Divider(thickness = 2.dp)

            // -------------------------
            // FECHA DE ENVÍO
            // -------------------------
            Text(
                "Fecha de envío",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                "Fecha seleccionada: ${formatter.format(fechaEnvio)}",
                fontSize = 20.sp
            )

            Divider(thickness = 2.dp)

            // -------------------------
            // ESTADO DEL ENVÍO
            // -------------------------
            Text(
                "Seguimiento del envío",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Surface(
                color = Color(0xFFD1C4E9),
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 6.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Estado actual:", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(
                        estadoEnvio,
                        fontSize = 20.sp,
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
