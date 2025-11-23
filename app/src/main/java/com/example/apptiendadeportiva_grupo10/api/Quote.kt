package com.example.apptiendadeportiva_grupo10.api

import retrofit2.http.GET

data class Quote(
    val content: String,
    val author: String
)

// Interfaz para la API de frases
interface QuoteApi {
    @GET("random")  // Endpoint para frases aleatorias
    suspend fun getRandomQuote(): Quote  // Retorna tu Quote
}

