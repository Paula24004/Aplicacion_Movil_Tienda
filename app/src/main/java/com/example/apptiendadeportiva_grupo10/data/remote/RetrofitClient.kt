package com.example.apptiendadeportiva_grupo10.data.remote

import com.example.apptiendadeportiva_grupo10.api.ProductApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://98.92.25.73:8080/" // Recuerda revisar bien la ruta, algunas APIs utilizan una ruta especifica}

    private val client = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY // Esto imprime la request y el response body
            }
        )
        .build()

    val apiService: ProductApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client) // Usar el cliente OkHttp configurado
            .build()
            .create(ProductApiService::class.java) // ⬅️ Crear ProductApiService
    }
}