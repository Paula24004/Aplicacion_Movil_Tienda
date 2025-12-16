package com.example.apptiendadeportiva_grupo10.pruebasunitarias

import android.app.Application
import com.example.apptiendadeportiva_grupo10.viewmodel.AuthViewModel
import com.example.apptiendadeportiva_grupo10.repository.ProductoRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class AuthViewModelTest {

    private lateinit var viewModel: AuthViewModel

    @Before
    fun setUp() {
        val application = mock(Application::class.java)
        val productoRepository = mock(ProductoRepository::class.java)

        viewModel = AuthViewModel(application, productoRepository)
    }

    // -----------------------------
    // RUT
    // -----------------------------

    @Test
    fun `formatearRut formatea correctamente`() {
        val result = viewModel.formatearRut("123456785")
        assertEquals("12.345.678-5", result)
    }

    @Test
    fun `validarRut valido retorna true`() {
        assertTrue(viewModel.validarRut("11.111.111-1"))
    }

    @Test
    fun `validarRut invalido retorna false`() {
        assertFalse(viewModel.validarRut("12.345.678-9"))
    }

    // -----------------------------
    // UPDATE UI STATE
    // -----------------------------

    @Test
    fun `updateEmail actualiza email`() {
        viewModel.updateEmail("test@mail.com")
        assertEquals("test@mail.com", viewModel.uiState.email)
    }

    @Test
    fun `updateUsername actualiza username`() {
        viewModel.updateUsername("usuario")
        assertEquals("usuario", viewModel.uiState.username)
    }

    @Test
    fun `updatePassword actualiza password`() {
        viewModel.updatePassword("1234")
        assertEquals("1234", viewModel.uiState.password)
    }

    @Test
    fun `updateRegion actualiza region`() {
        viewModel.updateRegion("Metropolitana")
        assertEquals("Metropolitana", viewModel.uiState.region)
    }

    @Test
    fun `updateComuna actualiza comuna`() {
        viewModel.updateComuna("Santiago")
        assertEquals("Santiago", viewModel.uiState.comuna)
    }

    @Test
    fun `updateDireccion actualiza direccion`() {
        viewModel.updateDireccion("Av Siempre Viva 123")
        assertEquals("Av Siempre Viva 123", viewModel.uiState.direccion)
    }

    // -----------------------------
    // LOGOUT GENERAL
    // -----------------------------

    @Test
    fun `logout limpia estado del usuario`() {

        // GIVEN
        viewModel.updateUsername("admin")
        viewModel.updateEmail("admin@mail.com")

        // WHEN
        viewModel.logout()

        // THEN
        assertEquals("", viewModel.uiState.username)
        assertEquals("", viewModel.uiState.email)
        assertFalse(viewModel.isLoggedIn)
    }
}
