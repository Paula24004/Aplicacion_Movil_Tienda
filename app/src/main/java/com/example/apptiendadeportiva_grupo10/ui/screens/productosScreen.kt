package com.example.apptiendadeportiva_grupo10.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.apptiendadeportiva_grupo10.navigation.AppNavigation

@Composable
fun productosScreen (
    navigator: NavHostController = rememberNavController(),
    viewModel: AuthViewModel
) {

    Column(
        Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 12.dp)
    ) {
        Text(
            text = "Productos",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
    }
        Card(
        ) {
            Text(
                text = "Camisa deportiva",
                fontSize = 8.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        }
    }
