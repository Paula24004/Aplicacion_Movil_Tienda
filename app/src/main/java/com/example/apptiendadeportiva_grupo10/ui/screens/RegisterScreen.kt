package com.example.apptiendadeportiva_grupo10.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    // Obtiene el estado actual del ViewModel
    val state = authViewModel.uiState

    // Función de validación del RUT (copiada de su código anterior)
    fun rutValido(rut: String): Boolean {
        // Limpia el RUT, permitiendo solo números y K/k
        val limpio = rut.replace("[^0-9Kk]".toRegex(), "")
        // Cuerpo del RUT (todo menos el último dígito, que es el DV)
        val cuerpo = if (limpio.length > 1) limpio.dropLast(1) else return false
        // El cuerpo debe ser solo números y la longitud total debe estar entre 7 y 8
        return cuerpo.matches(Regex("[0-9]+")) && limpio.length in 7..8
    }

    // Comprueba la validez del RUT en tiempo real si ya se ha ingresado algo
    val isRutValid = if (state.rut.isNotBlank()) rutValido(state.rut) else true

    // El formulario está listo para enviarse si todos los campos están llenos y el RUT es válido
    val formIsReady = state.username.isNotBlank() && state.email.isNotBlank() &&
            state.password.isNotBlank() && isRutValid

    // Efecto para navegar después de un registro exitoso
    LaunchedEffect(state.registrationSuccess) {
        if (state.registrationSuccess) {
            // Navega a la pantalla principal/catálogo
            navController.navigate("catalogo") {
                // Limpia el back stack para que el usuario no pueda volver a registro
                popUpTo("register") { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Registro de Usuario") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Crea tu cuenta", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 32.dp))

            // Campo Email
            OutlinedTextField(
                value = state.email,
                onValueChange = authViewModel::updateEmail,
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                isError = state.errorMessage != null && state.email.isBlank()
            )

            // Campo Nombre de Usuario
            OutlinedTextField(
                value = state.username,
                onValueChange = authViewModel::updateUsername,
                label = { Text("Nombre de Usuario") },
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Usuario") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                isError = state.errorMessage != null && state.username.isBlank()
            )

            // Campo RUT
            OutlinedTextField(
                value = state.rut,
                onValueChange = authViewModel::updateRut,
                label = { Text("RUT (sin DV)") },
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "RUT") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                // Muestra error si el RUT está incorrecto Y no está vacío, o si está vacío y hay un error general
                isError = !isRutValid && state.rut.isNotBlank() || (state.errorMessage != null && state.rut.isBlank()),
                supportingText = {
                    if (!isRutValid && state.rut.isNotBlank()) {
                        Text("RUT inválido (ingrese sin dígito verificador)", color = MaterialTheme.colorScheme.error)
                    }
                }
            )

            // Campo Contraseña
            OutlinedTextField(
                value = state.password,
                onValueChange = authViewModel::updatePassword,
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                isError = state.errorMessage != null && state.password.isBlank()
            )

            // Botón de Registro
            Button(
                onClick = authViewModel::registrar, // Llama a la lógica de registro en el ViewModel
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = !state.isLoading && formIsReady // Se deshabilita mientras carga o si el formulario no está listo
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                } else {
                    Text("Registrarse")
                }
            }

            // Mostrar mensaje de error del ViewModel
            state.errorMessage?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            // Link para ir a Login
            TextButton(
                onClick = { navController.navigate("login") },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("¿Ya tienes cuenta? Inicia sesión")
            }
        }
    }
}