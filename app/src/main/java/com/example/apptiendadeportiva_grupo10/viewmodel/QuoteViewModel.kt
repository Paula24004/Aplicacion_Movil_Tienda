package com.example.apptiendadeportiva_grupo10.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptiendadeportiva_grupo10.api.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QuoteViewModel : ViewModel() {

    private val _quote = MutableStateFlow("")
    val quote: StateFlow<String> = _quote

    fun loadQuote() {
        viewModelScope.launch {
            try {
                val result = RetrofitClient.api.getRandomQuote()
                _quote.value = "\"${result.content}\" — ${result.author}"
            } catch (e: Exception) {
                _quote.value = "Error al cargar frase"
            }
        }
    }
}
