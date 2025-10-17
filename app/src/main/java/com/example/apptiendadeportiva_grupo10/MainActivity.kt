package com.example.apptiendadeportiva_grupo10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.apptiendadeportiva_grupo10.navigation.AppNavigation
import com.example.apptiendadeportiva_grupo10.ui.theme.AppTiendaDeportiva_Grupo10Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTiendaDeportiva_Grupo10Theme {
                AppNavigation()
            }
        }
    }
}