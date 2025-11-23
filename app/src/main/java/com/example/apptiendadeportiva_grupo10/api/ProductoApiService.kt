package com.example.apptiendadeportiva_grupo10.api

import com.example.apptiendadeportiva_grupo10.model.ProductoDto
import retrofit2.http.GET

interface ProductApiService {

    @GET("api/products")
    suspend fun getProducts(): List<ProductoDto>

}

