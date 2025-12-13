package com.example.apptiendadeportiva_grupo10.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidoEnviadoScreen(
    navController: NavController,
    agencia: String,
    fecha: String,
    direccion: String,
    total: Double
) {
    val formato = remember {
        NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Pedido Enviado",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF673AB7)
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(28.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(28.dp),
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                "Pedido enviado por $agencia",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50)
            )

            Divider(thickness = 2.dp)

            Text(
                "Fecha de envío:",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                fecha,
                fontSize = 22.sp
            )

            Text(
                "Dirección de despacho:",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                direccion,
                fontSize = 22.sp
            )

            Divider(thickness = 2.dp)

            Text(
                "Total pagado:",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                formato.format(total),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 🛒 SEGUIR COMPRANDO
            Button(
                onClick = {
                    navController.navigate("catalogo") {
                        popUpTo("home") { inclusive = false }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8A2BE2),
                    contentColor = Color.White
                )
            ) {
                Text(
                    "SEGUIR COMPRANDO",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // 🔒 CERRAR SESIÓN
            Button(
                onClick = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                colors = ButtonDefaults.buttonColors(

                        containerColor = Color(0xFF8A2BE2),
                        contentColor = Color.White
                    )
                )
             {
                Text(
                    "CERRAR SESIÓN",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
