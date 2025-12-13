package com.example.apptiendadeportiva_grupo10.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptiendadeportiva_grupo10.model.GestionEnvioDto
import com.example.apptiendadeportiva_grupo10.repository.GestionEnvioRepository
import kotlinx.coroutines.launch

class GestionEnvioViewModel(
    private val repository: GestionEnvioRepository
) : ViewModel() {

    fun crearEnvio(
        agencia: String,
        fecha: String,
        estado: String,
        direccion: String
    ) {
        viewModelScope.launch {
            try {
                val envio = GestionEnvioDto(
                    agenciaEnvio = agencia,
                    fechaEnvio = fecha,
                    estadoEnvio = estado,
                    direccionDespacho = direccion
                )
                repository.guardarEnvio(envio)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}


