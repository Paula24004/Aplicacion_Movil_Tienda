package com.example.apptiendadeportiva_grupo10.model

object FakeDatabase {
    private val usuarios = mutableListOf<Usuario>()

    fun registrar(usuario: Usuario): Boolean {
        if (usuario.email.isNullOrBlank()) return false
        if (usuarios.any { it.email == usuario.email }) return false
        usuarios.add(usuario.copy(esAdmin = false, usernameAdmin = null))
        return true
    }

    fun login(email: String, password: String): Boolean {
        return usuarios.any { it.email == email && it.password == password }
    }

    // --- ADMIN ---
    fun registrarAdmin(usernameAdmin: String, passwordAdmin: String, emailAdmin: String): Boolean {
        // Validaciones básicas
        if (usernameAdmin.isBlank() || passwordAdmin.isBlank() || emailAdmin.isBlank()) {
            return false
        }
        // Username admin único
        if (usuarios.any { it.usernameAdmin == usernameAdmin }) {
            return false
        }
        // Email único
        if (usuarios.any { it.email == emailAdmin }) {
            return false
        }

        val nuevoId = (usuarios.maxOfOrNull { it.id } ?: 0) + 1
        val admin = Usuario(
            id = nuevoId,
            nombre = null,
            password = passwordAdmin,
            email = emailAdmin,
            esAdmin = true,
            usernameAdmin = usernameAdmin
        )
        usuarios.add(admin)
        return true
    }

    fun loginAdmin(usernameAdmin: String, passwordAdmin: String): Boolean {
        return usuarios.any {
            it.esAdmin && it.usernameAdmin == usernameAdmin && it.password == passwordAdmin
        }
    }
}
