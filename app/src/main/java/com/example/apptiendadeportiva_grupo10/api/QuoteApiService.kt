package com.example.apptiendadeportiva_grupo10.api

import retrofit2.http.GET

interface QuoteApiService {

    @GET("random")
    suspend fun getRandomQuote(): Quote
}
