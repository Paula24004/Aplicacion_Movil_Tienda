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

    val formularioListo =
        state.username.isNotBlank() &&
                state.email.isNotBlank() &&
                state.password.isNotBlank() &&
                state.rut.isNotBlank() &&
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
                title = {
                    Text(
                        "Registro de Usuario",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF650099)
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .background(Color(0xFFFCF9EF)) // Fondo blanco hueso
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(20.dp))

            Text(
                "Crea tu cuenta",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(Modifier.height(24.dp))

            // --------- CAMPOS ------------

            CampoRegistro(state.email, authViewModel::updateEmail, "Email")
            Spacer(Modifier.height(14.dp))

            CampoRegistro(state.username, authViewModel::updateUsername, "Nombre de Usuario")
            Spacer(Modifier.height(14.dp))

            CampoRegistro(state.rut, authViewModel::updateRut, "RUT")
            Spacer(Modifier.height(14.dp))

            CampoRegistro(state.region, authViewModel::updateRegion, "Región")
            Spacer(Modifier.height(14.dp))

            CampoRegistro(state.comuna, authViewModel::updateComuna, "Comuna")
            Spacer(Modifier.height(14.dp))

            CampoRegistro(state.direccion, authViewModel::updateDireccion, "Dirección")
            Spacer(Modifier.height(14.dp))

            // --------- CONTRASEÑA -----------

            OutlinedTextField(
                value = state.password,
                onValueChange = authViewModel::updatePassword,
                label = {
                    Text(
                        "Contraseña",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium
                ),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFF650099),
                    unfocusedIndicatorColor = Color.Gray,
                    focusedLabelColor = Color(0xFF650099),
                    cursorColor = Color(0xFF650099)
                )
            )

            Spacer(Modifier.height(24.dp))

            // --------- BOTÓN REGISTRARSE -----------

            Button(
                onClick = { authViewModel.registrar() },
                enabled = formularioListo,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF650099),
                    contentColor = Color.White
                )
            ) {
                Text("Registrarse", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            // --------- MENSAJE --------------

            if (mensaje.isNotBlank()) {
                Spacer(Modifier.height(12.dp))
                Text(
                    mensaje,
                    color = Color(0xFF006400),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(16.dp))

            // --------- VOLVER A LOGIN -----------

            TextButton(onClick = { navController.navigate("iniciar_sesion") }) {
                Text(
                    "¿Ya tienes cuenta? Inicia sesión",
                    color = Color(0xFF650099),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoRegistro(
    valor: String,
    onChange: (String) -> Unit,
    etiqueta: String
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onChange,
        label = {
            Text(
                etiqueta,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        },
        textStyle = TextStyle(
            color = Color.Black,
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium
        ),
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color(0xFF650099),
            unfocusedIndicatorColor = Color.Gray,
            focusedLabelColor = Color(0xFF650099),
            cursorColor = Color(0xFF650099)
        )
    )
}
