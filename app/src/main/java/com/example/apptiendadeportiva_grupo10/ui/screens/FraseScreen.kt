package com.example.apptiendadeportiva_grupo10.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.apptiendadeportiva_grupo10.viewmodel.QuoteViewModel

@Composable
fun FraseScreen(viewModel: QuoteViewModel) {

    val frase by viewModel.quote.collectAsState()

    LaunchedEffect(true) {
        viewModel.loadQuote()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = frase, style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = { viewModel.loadQuote() }) {
            Text("Nueva Frase")
        }
    }
}
