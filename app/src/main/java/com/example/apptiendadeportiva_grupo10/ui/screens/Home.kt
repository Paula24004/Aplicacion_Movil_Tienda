package com.example.apptiendadeportiva_grupo10.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.apptiendadeportiva_grupo10.viewmodel.AuthViewModel

@Composable
fun HomeScreenContent(
    onNavigationProductos: (() -> Unit)? = null,
    onNavigationLogin: (() -> Unit)? = null
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 12.dp)
    ) {
        Text(
            text = "Home",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
    }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick =  { onNavigationProductos?.invoke() },
            modifier = Modifier.fillMaxSize()
        ) {
            Text("Ir a Productos")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick =  { onNavigationLogin?.invoke() },
            modifier = Modifier.fillMaxSize()
        ) {
            Text("Ir a Login")
        }
}

@Composable
fun HomeScreen(navController: NavController, viewModel: AuthViewModel) {
    com.example.apptiendadeportiva_grupo10.ui.screen.HomeScreenContent(
        onNavigationProductos = { navController.navigate("productos") },
        onNavigationLogin = { navController.navigate("login") }
    )
}