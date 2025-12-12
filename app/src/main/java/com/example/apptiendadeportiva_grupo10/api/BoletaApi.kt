package com.example.apptiendadeportiva_grupo10.api



import com.example.apptiendadeportiva_grupo10.model.BoletaDto
import retrofit2.http.Body
import retrofit2.http.POST

interface BoletaApi {

    @POST("boletas")
    suspend fun crearBoleta(
        @Body boleta: BoletaDto
    ): BoletaDto
}
