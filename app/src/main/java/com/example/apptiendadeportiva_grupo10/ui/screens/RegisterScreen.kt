package com.example.apptiendadeportiva_grupo10.ui.screens
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
    val registerMessage by viewModel.mensaje
    var rutError by remember { mutableStateOf<String?>(null) }
    val rutLimpio = rut.replace("[^0-9]".toRegex(), "")
    val isRutValid = rutValido(rut)





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

                // Campo de rut
                OutlinedTextField(
                    value = rut,
                    onValueChange = { newValue ->
                        rut = newValue
                        if (newValue.isNotBlank() && !rutValido(newValue)) {
                            rutError = "Rut inválido (ingrese rut sin digito verificador"
                        } else {
                            rutError = null
                        }
                    },
                    label = { Text("Rut") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = rutError != null,
                    supportingText = {
                        if (rutError != null) {
                            Text(text = rutError!!, color = MaterialTheme.colorScheme.error)
                        }

                    }
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
                        viewModel.registrar(0,username,rut = rut,password, email)
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
                if (registerMessage.isNotBlank()) {
                    Text(
                        text = registerMessage,
                        color = if (registerMessage == "Registro exitoso") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                }

                // Botón/Texto para volver al Login
                TextButton(onClick = onNavigateToLogin) {
                    Text("¿Ya tienes cuenta? Iniciar Sesión")
                }
            }
        }
    }
}

fun rutValido(rut: String): Boolean{
    val rutLimpio = rut.replace("[^0-9Kk]".toRegex(), "")
    val rutCuerpo = if (rutLimpio.length > 1 ) {
        rutLimpio.substring(0, rutLimpio.length - 1)
    } else {
        return false
    }

    return rutCuerpo.matches("[0-9]+".toRegex()) && rutLimpio.length in 7..8
}

