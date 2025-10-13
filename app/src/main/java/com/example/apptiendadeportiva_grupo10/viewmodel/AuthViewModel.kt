package com.example.apptiendadeportiva_grupo10.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.apptiendadeportiva_grupo10.model.FakeDatabase
import com.example.apptiendadeportiva_grupo10.model.Usuario

class AuthViewModel: ViewModel() {
    var mensaje = mutableStateOf("")
    var usuarioActual = mutableStateOf<String>("")

    fun registrar(nombre: String, email: String, password: String){
        val nuevo = Usuario(nombre, email, password)
        if (FakeDatabase.registrar(nuevo)) {
            mensaje.value = "Registro exitoso"
        } else {
            mensaje.value = "el usuario ya existe"
        }
    }


    fun login(email: String, password: String): Boolean {
        return if (FakeDatabase.login(email, password)){
            usuarioActual.value = email
            mensaje.value = "inicio de sesion exitoso"
            true
        } else {
            mensaje.value = "credenciales invalidas"
            false
        }
    }

}