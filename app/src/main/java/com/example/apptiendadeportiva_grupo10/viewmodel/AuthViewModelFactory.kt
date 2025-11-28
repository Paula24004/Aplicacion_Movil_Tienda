package com.example.apptiendadeportiva_grupo10.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.apptiendadeportiva_grupo10.repository.ProductoRepository

class AuthViewModelFactory(
    private val application: Application,
    private val productoRepository: ProductoRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(application, productoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}