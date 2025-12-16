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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable

fun HomeContent(
    navController: NavController? = null,
    onNavigationLogin: (() -> Unit)? = null,
    onNavigationAdmin: () -> Unit,
    onNavigationCatalogo: () -> Unit
) {
    val purple = Color(0xFF8A00C2)  // Morado más intenso tipo imagen

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

            // Logo centrado
            ImgManagement(
                null,
                modifier = Modifier.padding(bottom = 28.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(26.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // 🔵 Botón Iniciar Sesión
                Button(
                    onClick = { onNavigationLogin?.invoke() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp),
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = purple,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        "Iniciar Sesión / Registrarse",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // 🔵 Botón Frase del día
                Button(
                    onClick = { navController?.navigate("frases") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp),
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = purple,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        "Frase del día",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // 🔵 Botón Ver Catálogo
                Button(
                    onClick = onNavigationCatalogo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp),
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = purple,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        "Ver Catálogo sin Iniciar Sesión",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
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