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
    viewModel: GestionEnvioViewModel
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
                title = { Text("Proceso de Envío", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF673AB7)
                )
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // 🔴 BOTÓN CONFIRMAR ENVÍO (GUARDA EN BACKEND)
                Button(
                    onClick = {
                        viewModel.crearEnvio(
                            agencia = agenciaSeleccionada,
                            fecha = formatter.format(fechaEnvio),
                            estado = estadoEnvio
                        )
                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = purple,
                        contentColor = Color.White
                    )
                ) {
                    Text("CONFIRMAR ENVÍO", fontWeight = FontWeight.Bold)
                }

                // 🔙 BOTÓN VOLVER
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = purple,
                        contentColor = Color.White
                    )
                ) {
                    Text("VOLVER", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
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
                    onClick = { agenciaSeleccionada = "Correos de Chile" }
                )
                Text("Correos de Chile")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = agenciaSeleccionada == "Starken Envíos",
                    onClick = { agenciaSeleccionada = "Starken Envíos" }
                )
                Text("Starken Envíos")
            }

            Divider()

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
                fontSize = 18.sp
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                Button(
                    onClick = {
                        calendar.time = fechaEnvio
                        calendar.add(Calendar.DAY_OF_MONTH, 1)
                        fechaEnvio = calendar.time
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = purple,
                        contentColor = Color.White
                    )
                ) {
                    Text("+ Día")
                }

                Button(
                    onClick = {
                        val hoy = Calendar.getInstance().time
                        calendar.time = fechaEnvio
                        calendar.add(Calendar.DAY_OF_MONTH, -1)
                        if (!calendar.time.before(hoy)) {
                            fechaEnvio = calendar.time
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = purple,
                        contentColor = Color.White
                    )
                ) {
                    Text("- Día")
                }
            }

            Divider()

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
                tonalElevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Estado actual:", fontWeight = FontWeight.Bold)
                    Text(
                        estadoEnvio,
                        fontSize = 18.sp,
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // -------------------------
            // SIMULACIÓN DE AVANCE
            // -------------------------
            Button(
                onClick = {
                    estadoEnvio = when (estadoEnvio) {
                        "En espera de despacho" -> "En proceso"
                        "En proceso" -> "En camino hacia tu hogar"
                        else -> "En camino hacia tu hogar"
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = purple,
                    contentColor = Color.White
                )
            ) {
                Text("Actualizar estado", fontWeight = FontWeight.Bold)
            }
        }
    }
}
