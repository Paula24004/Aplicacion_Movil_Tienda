package com.example.apptiendadeportiva_grupo10.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.apptiendadeportiva_grupo10.R
import com.example.apptiendadeportiva_grupo10.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginAdmin(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: AuthViewModel
) {
    var usernameAdmin by remember { mutableStateOf("") }
    var passwordAdmin by remember { mutableStateOf("") }

    val loginMessageAdmin by viewModel.mensajeadmin

    Scaffold(
        topBar = { TopAppBar(title = { Text("Iniciar Sesión como Administrador") }) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.fondo_login),
                contentDescription = "Fondo de login admin",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Panel Administrador", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = usernameAdmin,
                    onValueChange = { usernameAdmin = it },
                    label = { Text("Usuario Administrador") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = passwordAdmin,
                    onValueChange = { passwordAdmin = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        val loginExitoso = viewModel.loginAdmin(usernameAdmin, passwordAdmin)
                        if (loginExitoso) {
                            onLoginSuccess()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = usernameAdmin.isNotBlank() && passwordAdmin.isNotBlank()
                ) {
                    Text("Ingresar")
                }

                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = onNavigateToRegister) {
                    Text("Registrarse como Administrador")
                }

                Spacer(modifier = Modifier.height(12.dp))
                if (loginMessageAdmin.isNotBlank()) {
                    Text(
                        text = loginMessageAdmin,
                        color = if (loginMessageAdmin.contains("exitos", ignoreCase = true))
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
