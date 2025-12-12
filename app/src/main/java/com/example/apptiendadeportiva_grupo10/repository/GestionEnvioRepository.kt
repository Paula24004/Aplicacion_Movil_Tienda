package com.example.apptiendadeportiva_grupo10.repository

import com.example.apptiendadeportiva_grupo10.api.GestionEnvioApi
import com.example.apptiendadeportiva_grupo10.model.GestionEnvioDto

class GestionEnvioRepository(
    private val api: GestionEnvioApi
) {
    suspend fun guardarEnvio(envio: GestionEnvioDto) {
        api.crearGestionEnvio(envio)
    }
}


