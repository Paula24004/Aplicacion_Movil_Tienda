package com.example.apptiendadeportiva_grupo10.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    // Validaciones en tiempo real para habilitar el botón
    val emailValido = authViewModel.esEmailValido(state.email)
    val rutValido = authViewModel.validarRut(state.rut)
    val passwordValida = authViewModel.esPasswordSegura(state.password)

    val formularioListo = emailValido && rutValido && passwordValida &&
            state.username.isNotBlank() &&
            state.region.isNotBlank() &&
            state.comuna.isNotBlank() &&
            state.direccion.isNotBlank()

    LaunchedEffect(state.registrationSuccess) {
        if (state.registrationSuccess) {
            navController.navigate("catalogo") {
                popUpTo("register") { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de Usuario", fontWeight = FontWeight.Bold, color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF650099))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .background(Color(0xFFFCF9EF))
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(20.dp))
            Text("Crea tu cuenta", fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(24.dp))

            // --- EMAIL ---
            CampoRegistro(
                valor = state.email,
                onChange = authViewModel::updateEmail,
                etiqueta = "Email",
                isError = state.email.isNotEmpty() && !emailValido,
                errorMessage = "Correo no válido (ej: usuario@correo.com)"
            )
            Spacer(Modifier.height(14.dp))

            // --- USERNAME ---
            CampoRegistro(
                valor = state.username,
                onChange = authViewModel::updateUsername,
                etiqueta = "Nombre de Usuario"
            )
            Spacer(Modifier.height(14.dp))

            // --- RUT ---
            CampoRegistro(
                valor = state.rut,
                onChange = authViewModel::updateRut,
                etiqueta = "RUT",
                isError = state.rut.isNotEmpty() && !rutValido,
                errorMessage = "RUT inválido o mal formato"
            )
            Spacer(Modifier.height(14.dp))

            // --- UBICACIÓN ---
            CampoRegistro(state.region, authViewModel::updateRegion, "Región")
            Spacer(Modifier.height(14.dp))
            CampoRegistro(state.comuna, authViewModel::updateComuna, "Comuna")
            Spacer(Modifier.height(14.dp))
            CampoRegistro(state.direccion, authViewModel::updateDireccion, "Dirección")
            Spacer(Modifier.height(14.dp))

            // --- CONTRASEÑA ---
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = state.password,
                    onValueChange = authViewModel::updatePassword,
                    label = { Text("Contraseña", fontWeight = FontWeight.Bold) },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = state.password.isNotEmpty() && !passwordValida,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color(0xFF650099),
                        errorIndicatorColor = Color.Red
                    )
                )
                if (state.password.isNotEmpty() && !passwordValida) {
                    Text(
                        "Mínimo 6 caracteres, letras y números",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { authViewModel.registrar() },
                enabled = formularioListo,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF650099))
            ) {
                Text("Registrarse", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            // Mensaje de error general del ViewModel (backend)
            if (!state.errorMessage.isNullOrBlank()) {
                Text(state.errorMessage!!, color = Color.Red, fontWeight = FontWeight.Bold)
            }

            if (mensaje.isNotBlank()) {
                Text(mensaje, color = Color(0xFF006400), fontWeight = FontWeight.Bold)
            }

            TextButton(onClick = { navController.navigate("iniciar_sesion") }) {
                Text("¿Ya tienes cuenta? Inicia sesión", color = Color(0xFF650099), fontSize = 16.sp)
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
fun CampoRegistro(
    valor: String,
    onChange: (String) -> Unit,
    etiqueta: String,
    isError: Boolean = false,
    errorMessage: String = ""
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = valor,
            onValueChange = onChange,
            label = { Text(etiqueta, fontWeight = FontWeight.Bold, color = Color.Black) },
            isError = isError,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color(0xFF650099),
                unfocusedIndicatorColor = Color.Gray,
                errorIndicatorColor = Color.Red
            )
        )
        if (isError) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}