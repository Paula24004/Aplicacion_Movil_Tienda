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

    val loginMessageAdmin by viewModel.mensajeadmin

    // CORRECCIÓN: Se consume directamente el StateFlow del ViewModel.
    // NO se llama a la función loginAdminAuth() aquí.
    val esAdminLogueadoState = viewModel.esAdminLogueado.collectAsState()

    // --- EFECTO DE NAVEGACIÓN ---
    // Este efecto se dispara cada vez que esAdminLogueadoState.value cambia.
    LaunchedEffect(esAdminLogueadoState.value) {
        // Accedemos al valor con .value
        if (esAdminLogueadoState.value == true) {
            navController.navigate("admin_panel") {
                // Esto limpia la pila de navegación para que el usuario no pueda volver al login con el botón de retroceso.
                popUpTo("admin_iniciar") { inclusive = true }
            }
        }
    }
    // ----------------------------

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
            // Asegúrate de que R.drawable.fondo_login existe
            Image(
                painter = painterResource(id = R.drawable.fondo_login),
                contentDescription = "Fondo de login admin",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
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
                    onValueChange = { usernameAdmin = it },
                    label = { Text("Usuario Administrador") },
                    textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.Bold),
                    colors = adminFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = passwordAdmin,
                    onValueChange = { passwordAdmin = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.Bold),
                    colors = adminFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(24.dp))

                Button(
                    // CORRECCIÓN: Llamamos a loginAdminAuth() del ViewModel
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
                    Text("Registrarse como Administrador", color = Color.White, fontWeight = FontWeight.Bold)
                }

                if (loginMessageAdmin.isNotBlank()) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = loginMessageAdmin,
                        color = if (esAdminLogueadoState.value == true) // Usamos el StateFlow para la lógica de color
                            MaterialTheme.colorScheme.primary
                        else
                            Color.White
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
    disabledTextColor = Color.White.copy(alpha = 0.5f),
    errorTextColor = Color.White,

    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    disabledContainerColor = Color.Transparent,
    errorContainerColor = Color.Transparent,

    cursorColor = Color.White,
    errorCursorColor = Color.White,
    selectionColors = TextSelectionColors(
        handleColor = Color.White,
        backgroundColor = Color.White.copy(alpha = 0.3f)
    ),

    focusedBorderColor = Color.White,
    unfocusedBorderColor = Color.White.copy(alpha = 0.4f),
    disabledBorderColor = Color.White.copy(alpha = 0.2f),
    errorBorderColor = Color.Red,

    focusedLabelColor = Color.White,
    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
    disabledLabelColor = Color.White.copy(alpha = 0.5f),
    errorLabelColor = Color.Red,

    focusedPlaceholderColor = Color.White.copy(alpha = 0.6f),
    unfocusedPlaceholderColor = Color.White.copy(alpha = 0.6f),
    disabledPlaceholderColor = Color.White.copy(alpha = 0.4f),
    errorPlaceholderColor = Color.White.copy(alpha = 0.6f)
)