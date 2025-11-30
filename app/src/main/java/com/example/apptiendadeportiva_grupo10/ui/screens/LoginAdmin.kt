package com.example.apptiendadeportiva_grupo10.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.apptiendadeportiva_grupo10.R
import com.example.apptiendadeportiva_grupo10.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginAdmin(
    navController: NavHostController,
    onNavigateToRegister: () -> Unit,
    viewModel: AuthViewModel
) {
    var usernameAdmin by remember { mutableStateOf("") }
    var passwordAdmin by remember { mutableStateOf("") }

    // Mensaje del ViewModel
    val mensajeAdmin by viewModel.mensajeadmin

    // Estado booleano del login
    val esAdminLogueado by viewModel.esAdminLogueado.collectAsState()

    // EFECTO DE NAVEGACIÓN AL PANEL
    LaunchedEffect(esAdminLogueado) {
        if (esAdminLogueado == true) {
            navController.navigate("admin_panel") {
                popUpTo("admin_iniciar") { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Iniciar Sesión como Administrador",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.fondo_login),
                contentDescription = "Fondo login admin",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.45f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    "Panel Administrador",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(24.dp))

                OutlinedTextField(
                    value = usernameAdmin,
                    onValueChange = {
                        usernameAdmin = it
                        viewModel.mensajeadmin.value = "" // Limpia mensaje al escribir
                    },
                    label = { Text("Usuario Administrador") },
                    singleLine = true,
                    textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.Bold),
                    modifier = Modifier.fillMaxWidth(),
                    colors = adminFieldColors()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = passwordAdmin,
                    onValueChange = {
                        passwordAdmin = it
                        viewModel.mensajeadmin.value = ""
                    },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.Bold),
                    modifier = Modifier.fillMaxWidth(),
                    colors = adminFieldColors()
                )

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.loginAdminAuth(usernameAdmin, passwordAdmin)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    enabled = usernameAdmin.isNotBlank() && passwordAdmin.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                ) {
                    Text("Ingresar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.height(12.dp))

                TextButton(onClick = onNavigateToRegister) {
                    Text("Registrarse como Administrador", color = Color.White)
                }

                if (mensajeAdmin.isNotBlank()) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        mensajeAdmin,
                        color = if (esAdminLogueado == true) Color.Green else Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun adminFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    cursorColor = Color.White,
    focusedBorderColor = Color.White,
    unfocusedBorderColor = Color.White.copy(alpha = 0.4f),
    focusedLabelColor = Color.White,
    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
    selectionColors = TextSelectionColors(
        handleColor = Color.White,
        backgroundColor = Color.White.copy(alpha = 0.3f)
    ),
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent
)
