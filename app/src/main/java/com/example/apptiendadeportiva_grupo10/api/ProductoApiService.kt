package com.example.apptiendadeportiva_grupo10.api

import com.example.apptiendadeportiva_grupo10.model.ProductoDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body

interface ProductApiService {

    @GET("api/products")
    suspend fun getProducts(): List<ProductoDto>

    @POST("api/products")
    suspend fun createProduct(@Body producto: ProductoDto): Response<ProductoDto>

}

