package com.example.apptiendadeportiva_grupo10.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.apptiendadeportiva_grupo10.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val state = authViewModel.uiState
    val mensaje by authViewModel.mensaje

    val formularioListo =
        state.username.isNotBlank() &&
                state.email.isNotBlank() &&
                state.password.isNotBlank() &&
                state.rut.isNotBlank()

    LaunchedEffect(state.registrationSuccess) {
        if (state.registrationSuccess) {
            navController.navigate("catalogo") {
                popUpTo("register") { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Registro de Usuario") }) }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Crea tu cuenta", style = MaterialTheme.typography.headlineMedium)

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = state.email,
                onValueChange = authViewModel::updateEmail,
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = state.username,
                onValueChange = authViewModel::updateUsername,
                label = { Text("Nombre de Usuario") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = state.rut,
                onValueChange = authViewModel::updateRut,
                label = { Text("RUT") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = state.password,
                onValueChange = authViewModel::updatePassword,
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { authViewModel.registrar() },
                enabled = formularioListo,
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text("Registrarse")
            }

            if (mensaje.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(mensaje, color = MaterialTheme.colorScheme.primary)
            }

            Spacer(Modifier.height(12.dp))

            TextButton(onClick = { navController.navigate("iniciar_sesion") }) {
                Text("¿Ya tienes cuenta? Inicia sesión")
            }
        }
    }
}

