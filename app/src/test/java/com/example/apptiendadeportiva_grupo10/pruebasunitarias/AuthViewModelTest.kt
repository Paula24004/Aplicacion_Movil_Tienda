package com.example.apptiendadeportiva_grupo10.pruebasunitarias

import android.app.Application
import com.example.apptiendadeportiva_grupo10.repository.ProductoRepository
import com.example.apptiendadeportiva_grupo10.viewmodel.AuthViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class AuthViewModelTest {

    private lateinit var vm: AuthViewModel

    @Before
    fun setup() {
        val app = mock(Application::class.java)
        val repo = mock(ProductoRepository::class.java)
        vm = AuthViewModel(app, repo)
    }

    @Test
    fun `registro falla cuando hay campos vacios`() {
        vm.updateUsername("")     // vacío
        vm.updateEmail("test@test.cl")
        vm.updatePassword("1234")
        vm.updateRut("11111111K")

        vm.registrar()

        assertEquals("Todos los campos son obligatorios", vm.uiState.errorMessage)
        assertFalse(vm.uiState.registrationSuccess)
    }

    @Test
    fun `registro exitoso cuando todos los campos estan completos`() {
        vm.updateUsername("User1")
        vm.updateEmail("user1@test.cl")
        vm.updatePassword("1234")
        vm.updateRut("12345678K")

        vm.registrar()

        assertTrue(vm.uiState.registrationSuccess)
        assertNull(vm.uiState.errorMessage)
    }

    @Test
    fun `login falla con credenciales incorrectas`() {
        val result = vm.login("incorrecto@gmail.com", "wrongpass")

        assertFalse(result)
        assertEquals("Credenciales inválidas", vm.mensaje.value)
    }

    @Test
    fun `login exitoso con credenciales correctas`() {
        val result = vm.login("user@gmail.com", "1234")

        assertTrue(result)
        assertEquals("Inicio de sesión exitoso", vm.mensaje.value)
    }
}
