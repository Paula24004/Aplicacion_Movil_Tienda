package com.example.apptiendadeportiva_grupo10.pruebasunitarias



import com.example.apptiendadeportiva_grupo10.api.UserApi
import com.example.apptiendadeportiva_grupo10.model.LoginRequest
import com.example.apptiendadeportiva_grupo10.model.User
import okhttp3.ResponseBody
import retrofit2.Response
import org.junit.Assert.*
import org.junit.Test

class UserApiTest {

    private val fakeApi = object : UserApi {

        override suspend fun getUserByUsername(username: String): User {
            return User(
                username = username,
                password = "1234",
                email = "test@test.cl",
                region = "RM",
                comuna = "Santiago",
                direccion = "Calle Falsa 123",
                esAdmin = false,
                active = true
            )
        }

        override suspend fun register(user: User): Response<User> {
            return Response.success(user)
        }

        override suspend fun login(request: LoginRequest): Response<ResponseBody> {
            return Response.success(ResponseBody.create(null, "OK"))
        }
    }

    @Test
    fun `getUserByUsername retorna usuario correcto`() = kotlinx.coroutines.runBlocking {

        val user = fakeApi.getUserByUsername("paula")

        assertEquals("paula", user.username)
        assertEquals("test@test.cl", user.email)
        assertEquals("RM", user.region)
        assertTrue(user.active)
    }

    @Test
    fun `login retorna respuesta exitosa`() = kotlinx.coroutines.runBlocking {

        val response = fakeApi.login(LoginRequest("paula", "1234"))

        assertTrue(response.isSuccessful)
    }
}
