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
import com.example.apptiendadeportiva_grupo10.R
import com.example.apptiendadeportiva_grupo10.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var rutError by remember { mutableStateOf<String?>(null) }

    val registerMessage by viewModel.mensaje
    val isRutValid = rutValido(rut)
    val formOk = username.isNotBlank() && password.isNotBlank() && email.isNotBlank() && isRutValid

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Registrarse",
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
                contentDescription = "Fondo de registro",
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
                    "Crear Cuenta",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(24.dp))

                // Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.Bold),
                    colors = registerFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                // Usuario
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Usuario") },
                    textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.Bold),
                    colors = registerFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                // RUT (sin dígito verificador, según tu validación)
                OutlinedTextField(
                    value = rut,
                    onValueChange = { newValue ->
                        rut = newValue
                        rutError = if (newValue.isNotBlank() && !rutValido(newValue)) {
                            "RUT inválido (ingrese sin dígito verificador)"
                        } else null
                    },
                    label = { Text("RUT (sin DV)") },
                    isError = rutError != null,
                    supportingText = {
                        rutError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                    },
                    textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.Bold),
                    colors = registerFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                // Contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.Bold),
                    colors = registerFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.registrar(
                            id = 0,
                            nombre = username,
                            password = password,
                            email = email,
                            rut = rut
                        )
                        if (viewModel.mensaje.value == "Registro exitoso") {
                            onRegisterSuccess()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    enabled = formOk,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                ) {
                    Text("Registrar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.height(12.dp))

                if (registerMessage.isNotBlank()) {
                    Text(
                        text = registerMessage,
                        color = if (registerMessage == "Registro exitoso")
                            MaterialTheme.colorScheme.primary
                        else
                            Color.White
                    )
                }

                TextButton(onClick = onNavigateToLogin) {
                    Text("¿Ya tienes cuenta? Iniciar sesión", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun registerFieldColors() = OutlinedTextFieldDefaults.colors(
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

fun rutValido(rut: String): Boolean {
    val limpio = rut.replace("[^0-9Kk]".toRegex(), "")
    val cuerpo = if (limpio.length > 1) limpio.dropLast(1) else return false
    return cuerpo.matches(Regex("[0-9]+")) && limpio.length in 7..8
}