package com.example.apptiendadeportiva_grupo10.ui.screenspackage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.apptiendadeportiva_grupo10.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val registerMessage by viewModel.mensaje


    Scaffold(
        topBar = { TopAppBar(title = { Text("Registrarse") }) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ){

            Image(
                painter = painterResource(id = R.drawable.fondo_login),
                contentDescription = "Fondo de registro",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text("Crear Cuenta", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(32.dp))

                // Campo de Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email")},
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Campo de Usuario
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Usuario") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Campo de Contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(32.dp))

                // Botón de Registrar
                Button(
                    onClick = {
                        viewModel.registrar(0,username,password, email)
                        if (viewModel.mensaje.value=="Registro exitoso"){
                            onNavigateToLogin()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = username.isNotBlank() && password.isNotBlank() && email.isNotBlank()
                ) {
                    Text("Registrar")
                }

                Spacer(modifier = Modifier.height(16.dp))

                if(registerMessage.isNotBlank()){
                    Text(
                        text = registerMessage,
                        color = Color.Red,)
                }
                LaunchedEffect(registerMessage) {
                    if (registerMessage == "Registro exitoso") {
                        onNavigateToLogin()
                    }
                }


                // Botón/Texto para volver al Login
                TextButton(onClick = onNavigateToLogin) {
                    Text("¿Ya tienes cuenta? Iniciar Sesión")
                }
            }
        }
    }
}

