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
fun RegistroAdmin(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    // Estados locales (admin)
    var usernameAdmin by remember { mutableStateOf("") }
    var passwordAdmin by remember { mutableStateOf("") }
    var emailAdmin by remember { mutableStateOf("") }

    // Mensaje desde el ViewModel (admin)
    val mensajeAdmin by viewModel.mensajeadmin

    Scaffold(
        topBar = { TopAppBar(title = { Text("Registro Administrador") }) }
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.fondo_login),
                contentDescription = null,
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
                Text("Crear Cuenta de Administrador", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(32.dp))

                // Email
                OutlinedTextField(
                    value = emailAdmin,
                    onValueChange = { emailAdmin = it },
                    label = { Text("Email administrador") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Usuario administrador
                OutlinedTextField(
                    value = usernameAdmin,
                    onValueChange = { usernameAdmin = it },
                    label = { Text("Usuario administrador") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Contraseña
                OutlinedTextField(
                    value = passwordAdmin,
                    onValueChange = { passwordAdmin = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(32.dp))

                // registrar admin en el authviewmodel
                Button(
                    onClick = {
                        viewModel.registrarAdmin(usernameAdmin, passwordAdmin, emailAdmin)
                        if (viewModel.mensajeadmin.value == "Registro exitoso") {
                            onRegisterSuccess()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = usernameAdmin.isNotBlank() &&
                            passwordAdmin.isNotBlank() &&
                            emailAdmin.isNotBlank()
                ) {
                    Text("Registrar Administrador")
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (mensajeAdmin.isNotBlank()) {
                    Text(
                        text = mensajeAdmin,
                        color = if (mensajeAdmin == "Registro exitoso")
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = onNavigateToLogin) {
                    Text("¿Ya tienes cuenta? Iniciar sesión")
                }
            }
        }
    }
}
