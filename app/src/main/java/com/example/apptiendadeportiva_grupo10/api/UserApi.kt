package com.example.apptiendadeportiva_grupo10.api

import com.example.apptiendadeportiva_grupo10.model.User
import com.example.apptiendadeportiva_grupo10.model.LoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("users/register")
    suspend fun register(@Body user: User): Response<User>

    @POST("users/login")
    suspend fun login(@Body request: LoginRequest): Response<String>
}
