package com.example.apptiendadeportiva_grupo10.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.apptiendadeportiva_grupo10.viewmodel.AuthViewModel
import com.example.apptiendadeportiva_grupo10.R
import com.example.apptiendadeportiva_grupo10.components.ImgManagement

@Composable
fun HomeContent(
    navController: NavController? = null,  // Agrega navController como parámetro opcional
    onNavigationLogin: (() -> Unit)? = null,
    onNavigationAdmin: () -> Unit,
    onNavigationCatalogo: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.fondo_login),
            contentDescription = "Fondo de home",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Imagen del logo, centrada y no tan arriba
            ImgManagement(
                null,
                modifier = Modifier
                    .padding(bottom = 20.dp) // ajusta la distancia con los botones
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Columna de botones
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Botón Iniciar Sesión
                Button(
                    onClick = { onNavigationLogin?.invoke() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Text("Iniciar Sesión / Registrarse", fontSize = 16.sp)
                }

                // Botón Frase del día (ahora usa navController si está disponible)
                Button(
                    onClick = { navController?.navigate("frases") },  // Usa navController opcionalmente
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Frase del día", fontSize = 16.sp)
                }

                // Botón Catálogo
                Button(
                    onClick = onNavigationCatalogo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Ver Catálogo sin Iniciar Sesión", fontSize = 16.sp)
                }

                // Botón Administrador
                Button(
                    onClick = onNavigationAdmin,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                ) {
                    Text("Acceso de Administrador", fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
    onNavigateToAdmin: () -> Unit,
    onNavigateToCatalogo: () -> Unit
) {
    // Pasa navController a HomeContent
    HomeContent(
        navController = navController,  // Agrega esto
        onNavigationLogin = onNavigateToLogin,
        onNavigationAdmin = onNavigateToAdmin,
        onNavigationCatalogo = onNavigateToCatalogo
    )
}