package com.example.apptiendadeportiva_grupo10.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://44.221.81.240:8080/"

    val api: ProductApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductApiService::class.java)
    }
}

// Nuevo cliente para frases
object QuoteRetrofitClient {
    private const val BASE_URL = "https://api.quotable.io/"  // Confirma la URL de la API
    val api: QuoteApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuoteApi::class.java)
    }
}

