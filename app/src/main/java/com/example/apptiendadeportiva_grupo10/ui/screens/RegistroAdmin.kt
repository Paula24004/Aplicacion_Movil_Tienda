package com.example.apptiendadeportiva_grupo10.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apptiendadeportiva_grupo10.R
import com.example.apptiendadeportiva_grupo10.viewmodel.AuthViewModel

// ---------- VALIDACIÓN RUT ----------
fun rutAdminValido(rut: String): Boolean {
    val limpio = rut.replace("[^0-9Kk]".toRegex(), "")
    val cuerpo = if (limpio.length > 1) limpio.dropLast(1) else return false
    return cuerpo.matches(Regex("[0-9]+")) && limpio.length in 7..8
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroAdmin(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {

    // Estados
    var usernameAdmin by remember { mutableStateOf("") }
    var rutAdmin by remember { mutableStateOf("") }
    var passwordAdmin by remember { mutableStateOf("") }
    var emailAdmin by remember { mutableStateOf("") }

    val mensajeAdmin by viewModel.mensajeadmin
    var rutError by remember { mutableStateOf<String?>(null) }

    val morado = Color(0xFF650099)
    val fondoElegante = Color(0xFFFCF9EF)

    val formOk = usernameAdmin.isNotBlank() &&
            passwordAdmin.isNotBlank() &&
            emailAdmin.isNotBlank() &&
            rutError == null &&
            rutAdminValido(rutAdmin)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Registro Administrador",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = morado)
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(fondoElegante)
        ) {

            // -------------------------
            // LOGO CIRCULAR
            // -------------------------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.fondo_login),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            // -------------------------
            // FORMULARIO
            // -------------------------
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Spacer(Modifier.height(80.dp))



                Spacer(Modifier.height(24.dp))

                // EMAIL
                OutlinedTextField(
                    value = emailAdmin,
                    onValueChange = { emailAdmin = it },
                    label = {
                        Text("Email administrador",
                            fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    },
                    textStyle = LocalTextStyle.current.copy(
                        color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold
                    ),
                    shape = RoundedCornerShape(14.dp),
                    colors = adminFieldColors(morado),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(14.dp))

                // USUARIO
                OutlinedTextField(
                    value = usernameAdmin,
                    onValueChange = { usernameAdmin = it },
                    label = {
                        Text("Usuario administrador",
                            fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    },
                    textStyle = LocalTextStyle.current.copy(
                        color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold
                    ),
                    shape = RoundedCornerShape(14.dp),
                    colors = adminFieldColors(morado),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(14.dp))

                // RUT
                OutlinedTextField(
                    value = rutAdmin,
                    onValueChange = {
                        rutAdmin = it
                        rutError = if (it.isNotBlank() && !rutAdminValido(it))
                            "RUT inválido (sin dígito verificador)" else null
                    },
                    label = {
                        Text("RUT (sin DV)",
                            fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    },
                    isError = rutError != null,
                    supportingText = {
                        rutError?.let { msg -> Text(msg, color = Color.Red) }
                    },
                    textStyle = LocalTextStyle.current.copy(
                        color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold
                    ),
                    shape = RoundedCornerShape(14.dp),
                    colors = adminFieldColors(morado),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(14.dp))

                // CONTRASEÑA
                OutlinedTextField(
                    value = passwordAdmin,
                    onValueChange = { passwordAdmin = it },
                    label = {
                        Text("Contraseña",
                            fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    textStyle = LocalTextStyle.current.copy(
                        color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold
                    ),
                    shape = RoundedCornerShape(14.dp),
                    colors = adminFieldColors(morado),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(24.dp))

                // BOTÓN REGISTRAR
                Button(
                    onClick = {
                        val ok = viewModel.registrarAdmin(
                            usernameAdmin, rutAdmin, passwordAdmin, emailAdmin
                        )
                        if (ok) onRegisterSuccess()
                    },
                    enabled = formOk,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = morado, contentColor = Color.White
                    )
                ) {
                    Text("Registrar Administrador", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.height(10.dp))

                if (mensajeAdmin.isNotBlank()) {
                    Text(
                        mensajeAdmin,
                        color = if (mensajeAdmin.contains("exitos")) Color(0xFF0B6623) else Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(16.dp))

                // BOTÓN VOLVER
                Button(
                    onClick = onNavigateToLogin,
                    modifier = Modifier
                        .height(46.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = morado,
                        contentColor = Color.White
                    )
                ) {
                    Text("VOLVER", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun adminFieldColors(morado: Color) = TextFieldDefaults.colors(
    focusedIndicatorColor = morado,
    unfocusedIndicatorColor = Color.DarkGray,
    cursorColor = morado,
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent
)
