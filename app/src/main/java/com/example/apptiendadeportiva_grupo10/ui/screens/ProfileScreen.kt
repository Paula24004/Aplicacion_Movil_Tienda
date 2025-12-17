package com.example.apptiendadeportiva_grupo10.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val state = authViewModel.uiState
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", color = Color.White, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF650099))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFCF9EF))
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar con Inicial
            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = Color(0xFF650099).copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = state.username.take(1).uppercase(),
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF650099)
                    )
                }
            }

            Spacer(Modifier.height(30.dp))

            // Datos del Usuario
            InfoCard(label = "Nombre de Usuario", value = state.username)
            InfoCard(label = "Correo Electrónico", value = state.email)
            InfoCard(label = "RUT", value = state.rut)
            InfoCard(label = "Ubicación", value = "${state.comuna}, ${state.region}")
            InfoCard(label = "Dirección", value = state.direccion)

            Spacer(Modifier.weight(1f))

            // 🔥 BOTÓN CERRAR SESIÓN
            OutlinedButton(
                onClick = {
                    authViewModel.logout()
                    navController.navigate("home") { // O la ruta de tu login
                        popUpTo(0) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                border = BorderStroke(1.dp, Color(0xFF650099)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF650099))
            ) {
                Text("CERRAR SESIÓN", fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(12.dp))

            // Botón Eliminar
            Button(
                onClick = { showDialog = true },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("ELIMINAR MI CUENTA", fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(Modifier.height(16.dp))
        }
    }

    // Diálogo de Alerta para Eliminación (Se mantiene igual...)
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("¿Estás realmente seguro?") },
            text = { Text("Esta acción eliminará permanentemente tus datos de nuestro sistema y no podrás recuperarlos.") },
            confirmButton = {
                TextButton(onClick = {
                    android.util.Log.d("DELETE_DEBUG", "ID en el estado: ${authViewModel.uiState.id}")
                    showDialog = false
                    authViewModel.eliminarCuenta {
                        navController.navigate("home") { popUpTo(0) }
                    }
                }) {
                    Text("SÍ, ELIMINAR", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("CANCELAR")
                }
            }
        )
    }
}
@Composable
fun InfoCard(label: String, value: String) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
    ) {
        Text(text = label, fontSize = 13.sp, color = Color.Gray)
        Text(text = value.ifBlank { "No especificado" }, fontSize = 18.sp, fontWeight = FontWeight.Medium)
        HorizontalDivider(modifier = Modifier.padding(top = 4.dp), thickness = 0.5.dp, color = Color.LightGray)
    }
}