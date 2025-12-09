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
import androidx.navigation.NavHostController
import com.example.apptiendadeportiva_grupo10.R
import com.example.apptiendadeportiva_grupo10.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginAdmin(
    navController: NavHostController,
    onNavigateToRegister: () -> Unit,
    viewModel: AuthViewModel
) {
    var usernameAdmin by remember { mutableStateOf("") }
    var passwordAdmin by remember { mutableStateOf("") }

    val mensajeAdmin by viewModel.mensajeadmin
    val esAdminLogueado by viewModel.esAdminLogueado.collectAsState()

    val morado = Color(0xFF650099)
    val fondoElegante = Color(0xFFFCF9EF)

    // Navegar cuando inicie sesión correctamente
    LaunchedEffect(esAdminLogueado) {
        if (esAdminLogueado == true) {
            navController.navigate("admin_panel") {
                popUpTo("admin_iniciar") { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Iniciar Sesión como Administrador",
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

            // -----------------------
            // LOGO CIRCULAR
            // -----------------------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.fondo_login),
                    contentDescription = "Logo Admin",
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            // -----------------------
            // CAMPOS Y BOTONES
            // -----------------------
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Spacer(modifier = Modifier.height(80.dp))

                // CAMPO USUARIO ADMIN
                OutlinedTextField(
                    value = usernameAdmin,
                    onValueChange = {
                        usernameAdmin = it
                        viewModel.mensajeadmin.value = ""
                    },
                    label = {
                        Text(
                            "Usuario Administrador",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    },
                    textStyle = LocalTextStyle.current.copy(
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = morado,
                        unfocusedIndicatorColor = Color.DarkGray,
                        cursorColor = morado,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                )

                // CAMPO CONTRASEÑA ADMIN
                OutlinedTextField(
                    value = passwordAdmin,
                    onValueChange = {
                        passwordAdmin = it
                        viewModel.mensajeadmin.value = ""
                    },
                    label = {
                        Text(
                            "Contraseña",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    textStyle = LocalTextStyle.current.copy(
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = morado,
                        unfocusedIndicatorColor = Color.DarkGray,
                        cursorColor = morado,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp)
                )

                // BOTÓN INGRESAR
                Button(
                    onClick = { viewModel.loginAdminAuth(usernameAdmin, passwordAdmin) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = morado,
                        contentColor = Color.White
                    ),
                    enabled = usernameAdmin.isNotBlank() && passwordAdmin.isNotBlank()
                ) {
                    Text("Ingresar", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }

                // BOTÓN REGISTRARSE COMO ADMIN
                Button(
                    onClick = onNavigateToRegister,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = morado,
                        contentColor = Color.White
                    )
                ) {
                    Text("Registrarse como Administrador", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                // BOTÓN VOLVER
                Button(
                    onClick = { navController.navigate("home") },
                    modifier = Modifier
                        .padding(top = 60.dp)
                        .height(46.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = morado,
                        contentColor = Color.White
                    )
                ) {
                    Text("VOLVER", fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }

                // MENSAJE DE ERROR / EXITO
                if (mensajeAdmin.isNotBlank()) {
                    Text(
                        mensajeAdmin,
                        color = if (esAdminLogueado == true) Color(0xFF0B6623) else Color.Red,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 24.dp)
                    )
                }
            }
        }
    }
}
