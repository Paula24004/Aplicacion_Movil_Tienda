package com.example.apptiendadeportiva_grupo10.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
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

    // Validación RUT
    fun rutValido(rut: String): Boolean {
        val limpio = rut.replace("[^0-9Kk]".toRegex(), "")
        val cuerpo = if (limpio.length > 1) limpio.dropLast(1) else return false
        return cuerpo.matches(Regex("[0-9]+")) && limpio.length in 7..8
    }

    val rutEsValido = if (state.rut.isNotBlank()) rutValido(state.rut) else true

    val formularioListo =
        state.username.isNotBlank() &&
                state.email.isNotBlank() &&
                state.password.isNotBlank() &&
                rutEsValido

    // navegación al iniciar sesión
    LaunchedEffect(state.registrationSuccess) {
        if (state.registrationSuccess) {
            navController.navigate("login") {
                popUpTo("register") { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Registro de Usuario") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Crea tu cuenta", style = MaterialTheme.typography.headlineMedium)

            Spacer(Modifier.height(24.dp))

            // Email
            OutlinedTextField(
                value = state.email,
                onValueChange = authViewModel::updateEmail,
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Filled.Email, null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            // Usuario
            OutlinedTextField(
                value = state.username,
                onValueChange = authViewModel::updateUsername,
                label = { Text("Nombre de Usuario") },
                leadingIcon = { Icon(Icons.Filled.Person, null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            // RUT
            OutlinedTextField(
                value = state.rut,
                onValueChange = authViewModel::updateRut,
                label = { Text("RUT (sin DV)") },
                isError = !rutEsValido && state.rut.isNotBlank(),
                leadingIcon = { Icon(Icons.Filled.Person, null) },
                modifier = Modifier.fillMaxWidth()
            )
            if (!rutEsValido && state.rut.isNotBlank()) {
                Text("RUT inválido", color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(12.dp))

            // Password
            OutlinedTextField(
                value = state.password,
                onValueChange = authViewModel::updatePassword,
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = { Icon(Icons.Filled.Lock, null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            // Botón registrar
            Button(
                onClick = authViewModel::registrar,
                enabled = formularioListo,
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text("Registrarse")
            }

            // Mensaje error
            state.errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(12.dp))

            TextButton(onClick = { navController.navigate("login") }) {
                Text("¿Ya tienes cuenta? Inicia sesión")
            }
        }
    }
}
