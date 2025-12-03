package com.example.apptiendadeportiva_grupo10.repository

import com.example.apptiendadeportiva_grupo10.api.UserApi
import com.example.apptiendadeportiva_grupo10.data.remote.RetrofitClient
import com.example.apptiendadeportiva_grupo10.model.User

class UserRepository {

    private val userApi: UserApi = RetrofitClient.userApi

    suspend fun register(user: User) = userApi.register(user)

    suspend fun login(username: String, password: String): Boolean {
        val user = User(username = username, password = password)
        val response = userApi.login(user)
        return response.isSuccessful
    }
}
