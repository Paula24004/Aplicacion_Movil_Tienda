package com.example.apptiendadeportiva_grupo10.repository



import com.example.apptiendadeportiva_grupo10.api.BoletaApi
import com.example.apptiendadeportiva_grupo10.model.BoletaDto

class BoletaRepository(
    private val api: BoletaApi
) {
    suspend fun crearBoleta(boleta: BoletaDto) =
        api.crearBoleta(boleta)
}
