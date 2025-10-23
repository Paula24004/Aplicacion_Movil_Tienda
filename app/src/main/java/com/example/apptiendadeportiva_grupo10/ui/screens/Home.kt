package com.example.apptiendadeportiva_grupo10.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.apptiendadeportiva_grupo10.viewmodel.AuthViewModel
import com.example.apptiendadeportiva_grupo10.R

@Composable
fun HomeScreenContent(
    onNavigationProductos: (() -> Unit)? = null,
    onNavigationLogin: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Fondo consistente
    ) {

        // --- ## 1. Banner Principal / Imagen de Marca ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp) // Altura considerable para el banner
                .background(MaterialTheme.colorScheme.primaryContainer), // Un color de fondo primario
            contentAlignment = Alignment.Center
        ) {
            // Placeholder: Si tuvieras una imagen de fondo o logo para la tienda
            Image(
                painter = painterResource(id = R.drawable.logotienda), // Reemplaza con tu imagen
                contentDescription = "logo de tienda",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)
            ) {
                Text(
                    text = "SportWear", // Nombre de la Tienda
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold// Color del texto
                )
                Text(
                    text = "Tu destino para el mejor equipo.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
        }

        // Área de Botones Principales
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp), // Más padding para que respire
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            // Botón de Login/Registro
            Button(
                onClick =  { onNavigationLogin?.invoke() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer, // Un color secundario
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Text(
                    text = "👤 Iniciar Sesión / Registrarse",
                    fontSize = 16.sp
                )
            }
        }
    }
}

// --- HomeScreen se mantiene igual para la navegación ---
@Composable
fun HomeScreen(navController: NavController, viewModel: AuthViewModel) {
    HomeScreenContent(
        onNavigationProductos = { navController.navigate("productos") },
        onNavigationLogin = { navController.navigate("login") }
    )
}