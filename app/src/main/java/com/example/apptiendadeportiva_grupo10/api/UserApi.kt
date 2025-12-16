package com.example.apptiendadeportiva_grupo10.api



import com.example.apptiendadeportiva_grupo10.model.LoginRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.apptiendadeportiva_grupo10.model.User
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path



interface UserApi {

    @GET("users/username/{username}")
    suspend fun getUserByUsername(@Path("username") username: String): User


    @POST("users/register")
    suspend fun register(@Body user: User): Response<User>

    // 🔥 DEVOLVER ResponseBody, NO String
    @POST("users/login")
    suspend fun login(@Body request: LoginRequest): Response<ResponseBody>

    @DELETE("users/id/{id}")
    suspend fun eliminarUsuario(@Path("id") id: Int): retrofit2.Response<Void>
}

