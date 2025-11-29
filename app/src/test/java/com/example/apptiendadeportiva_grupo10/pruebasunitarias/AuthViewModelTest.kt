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
    fun `registro falla por rut invalido`() {
        vm.updateUsername("User1")
        vm.updateEmail("user1@test.cl")
        vm.updatePassword("1234")
        vm.updateRut("123")   // RUT inválido

        vm.registrar()

        assertEquals("El RUT ingresado no es válido (sin DV, 7 a 8 dígitos).", vm.uiState.errorMessage)
        assertFalse(vm.uiState.registrationSuccess)
    }

    @Test
    fun `registro exitoso con datos unicos`() {
        vm.updateUsername("User2")
        vm.updateEmail("user2@test.cl") // email único
        vm.updatePassword("abcd")
        vm.updateRut("1234567K")  // válido

        vm.registrar()

        assertTrue(vm.uiState.registrationSuccess)
        assertNull(vm.uiState.errorMessage)
    }

    @Test
    fun `registro duplicado usando el mismo email`() {
        // Primer registro
        vm.updateUsername("User3")
        vm.updateEmail("user3@test.cl") // email nuevo
        vm.updatePassword("1234")
        vm.updateRut("1234567K")
        vm.registrar()

        // Segundo registro con el MISMO email
        vm.updateUsername("User3")
        vm.updateEmail("user3@test.cl")
        vm.updatePassword("1234")
        vm.updateRut("1234567K")

        vm.registrar()

        assertEquals("El usuario/email ya existe en la base de datos.", vm.uiState.errorMessage)
        assertFalse(vm.uiState.registrationSuccess)
    }
}
