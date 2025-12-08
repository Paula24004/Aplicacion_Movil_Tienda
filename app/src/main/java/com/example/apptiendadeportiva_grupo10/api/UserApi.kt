package com.example.apptiendadeportiva_grupo10.api



import com.example.apptiendadeportiva_grupo10.model.LoginRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.apptiendadeportiva_grupo10.model.User


interface UserApi {

    @POST("users/register")
    suspend fun register(@Body user: User): Response<User>

    // 🔥 DEVOLVER ResponseBody, NO String
    @POST("users/login")
    suspend fun login(@Body request: LoginRequest): Response<ResponseBody>
}

