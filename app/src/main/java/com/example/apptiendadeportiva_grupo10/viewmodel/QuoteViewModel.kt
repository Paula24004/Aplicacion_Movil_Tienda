package com.example.apptiendadeportiva_grupo10.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QuoteViewModel : ViewModel() {

    private val frases = listOf(
        "Cree en ti y todo será posible.",
        "Cada día es una nueva oportunidad.",
        "Nunca es tarde para empezar de nuevo.",
        "Avanza, incluso si es lento.",
        "Tu esfuerzo te llevará lejos.",
        "La disciplina vence al talento.",
        "Hazlo por ti, no por los demás.",
        "La constancia supera al miedo.",
        "Un pequeño paso hoy es un gran logro mañana.",
        "El éxito es la suma de pequeños esfuerzos repetidos día tras día.",
        "Tu único límite eres tú misma.",
        "Respira. Todo estará bien.",
        "Tu versión del futuro te necesita fuerte hoy.",
        "No te rindas, lo mejor está por venir.",
        "Cree en tu proceso, incluso cuando es lento."
    )

    private val _quote = MutableStateFlow("Presiona el botón para una frase ✨")
    val quote: StateFlow<String> = _quote

    fun loadQuote() {
        viewModelScope.launch {
            _quote.value = frases.random()
        }
    }
}
