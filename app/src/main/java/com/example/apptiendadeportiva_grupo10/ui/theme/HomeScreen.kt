package com.example.apptiendadeportiva_grupo10.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.apptiendadeportiva_grupo10.R
// Se eliminó la importación duplicada de Scaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Tienda Deportiva") })
        }
    ) { innerPadding ->
        // Column es ahora el único hijo del Scaffold
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally // Centra los hijos horizontalmente
        ) {
            // Todos los elementos de la UI están dentro de la Columna
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo de la tienda",
                modifier = Modifier
                    .fillMaxWidth() // Usa fillMaxWidth en lugar de fillMaxSize
                    .height(150.dp),
                contentScale = ContentScale.Fit
            )

            Text("Bienvenido a la tienda deportiva")

            Button(onClick = { /* TODO */ }) {
                Text("presiona aqui")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}