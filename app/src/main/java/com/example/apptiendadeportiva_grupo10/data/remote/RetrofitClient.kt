package com.example.apptiendadeportiva_grupo10.data.remote

import com.example.apptiendadeportiva_grupo10.api.ProductApiService
import com.example.apptiendadeportiva_grupo10.api.UserApi
import com.example.apptiendadeportiva_grupo10.api.BoletaApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.apptiendadeportiva_grupo10.api.GestionEnvioApi
object RetrofitClient {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    private val client = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .build()

    // ✅ UNA sola instancia Retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    // APIs
    val apiService: ProductApiService by lazy {
        retrofit.create(ProductApiService::class.java)
    }

    val userApi: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }

    val boletaApi: BoletaApi by lazy {
        retrofit.create(BoletaApi::class.java)
    }

    val gestionEnvioApi: GestionEnvioApi by lazy {
        retrofit.create(GestionEnvioApi::class.java)
    }

}
