package com.example.apptiendadeportiva_grupo10.api

import com.example.apptiendadeportiva_grupo10.model.ProductoDto
import retrofit2.Response
import retrofit2.http.*

interface ProductApiService {

    @GET("api/products")
    suspend fun getProducts(): List<ProductoDto>

    @POST("api/products")
    suspend fun createProduct(@Body producto: ProductoDto): Response<ProductoDto>

    @DELETE("api/products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Response<Unit>

    @PUT("api/products/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body producto: ProductoDto): Response<ProductoDto>

    @GET("api/currency/convert")
    suspend fun convertir(
        @Query("amount") amount: Int,
        @Query("to") to: String
    ): Double
}