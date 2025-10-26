package com.example.apptiendadeportiva_grupo10.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.apptiendadeportiva_grupo10.viewmodel.AuthViewModel
import com.example.apptiendadeportiva_grupo10.R

// Pantalla principal visible
@Composable
fun HomeContent(
    onNavigationLogin: (() -> Unit)? = null,
    onNavigationAdmin: () -> Unit,
    onNavigationCatalogo: () -> Unit
) {
    Box (modifier = Modifier.fillMaxSize())
    {
        //Imagen de Fondo
        Image(
            painter = painterResource(id = R.drawable.fondo_login),
            contentDescription = "Fondo de home",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        //Columna de Contenido (Botones)
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // boton Iniciar Sesion
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
                    Text(
                        text = "Iniciar Sesión / Registrarse",
                        fontSize = 16.sp
                    )
                }

                // Ver Catálogo
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
                    Text(
                        text = "Ver Catálogo sin Iniciar Sesión",
                        fontSize = 16.sp
                    )
                }

                // Acceso de Administrador
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
                    Text(
                        text = "Acceso de Administrador",
                        fontSize = 16.sp
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
    // La función HomeScreen simplemente mapea los lambdas de navegación a HomeContent
    HomeContent(
        onNavigationLogin = onNavigateToLogin,
        onNavigationAdmin = onNavigateToAdmin,
        onNavigationCatalogo = onNavigateToCatalogo
    )
}