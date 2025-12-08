package com.example.apptiendadeportiva_grupo10.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = MoradoOscuro,
    secondary = MoradoMedio,
    tertiary = MoradoSuave,
    background = Color.White,
    surface = Color.White,
    onPrimary = TextoClaro,
    onSecondary = TextoClaro,
    onBackground = TextoOscuro,
    onSurface = TextoOscuro
)

private val DarkColorScheme = darkColorScheme(
    primary = MoradoMedio,
    secondary = MoradoSuave,
    tertiary = MoradoOscuro,
    background = FondoApp,
    surface = FondoCard,
    onPrimary = TextoClaro,
    onSecondary = TextoClaro,
    onBackground = TextoClaro,
    onSurface = TextoClaro
)

@Composable
fun AppTheme(
    darkTheme: Boolean = true,  // HAZ QUE TODA LA APP SEA OSCURA
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
