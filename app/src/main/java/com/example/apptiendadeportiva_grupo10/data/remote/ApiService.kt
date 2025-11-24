package com.example.apptiendadeportiva_grupo10.data.remote

import com.example.apptiendadeportiva_grupo10.model.ProductoDto
import retrofit2.http.GET

interface ApiService {
    @GET("api/products")
    suspend fun getProducts(): List<ProductoDto>
}
