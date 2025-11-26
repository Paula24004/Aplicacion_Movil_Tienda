package com.example.apptiendadeportiva_grupo10.api

import com.example.apptiendadeportiva_grupo10.model.usuarioRegisterDto
import com.example.apptiendadeportiva_grupo10.model.usuarioResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService{
    @POST("users")
    suspend fun registerUser(@Body usuarioRegisterDto: usuarioRegisterDto): Response<usuarioResponseDto>


}