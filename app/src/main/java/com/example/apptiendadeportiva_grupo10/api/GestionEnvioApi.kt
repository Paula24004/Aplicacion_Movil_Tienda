package com.example.apptiendadeportiva_grupo10.api



import com.example.apptiendadeportiva_grupo10.model.GestionEnvioDto
import retrofit2.http.*

interface GestionEnvioApi {

    @POST("api/gestion-envio")
    suspend fun crearGestionEnvio(
        @Body envio: GestionEnvioDto
    )
}
