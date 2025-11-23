package com.example.apptiendadeportiva_grupo10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.apptiendadeportiva_grupo10.ui.screens.CatalogoScreen
import com.example.apptiendadeportiva_grupo10.ui.screens.DetalleProductoScreen
import com.example.apptiendadeportiva_grupo10.viewmodel.CatalogoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val viewModel = remember { CatalogoViewModel() } // para un curso está bien
            Surface {
                NavHost(navController = navController, startDestination = "catalogo") {
                    composable("catalogo") {
                        CatalogoScreen(navController = navController, viewModel = viewModel)
                    }
                    composable("detalle/{productoId}") { back ->
                        val id = back.arguments?.getString("productoId")?.toIntOrNull() ?: -1
                        DetalleProductoScreen(productoId = id, viewModel = viewModel)
                    }
                }
            }
        }
    }
}
