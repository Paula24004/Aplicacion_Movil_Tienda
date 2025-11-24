package com.example.apptiendadeportiva_grupo10

import androidx.room.Room
import com.example.apptiendadeportiva_grupo10.data.local.AppDatabase
import com.example.apptiendadeportiva_grupo10.data.local.ProductoDao
import com.example.apptiendadeportiva_grupo10.model.ProductoEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import androidx.test.core.app.ApplicationProvider

class ProductoDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: ProductoDao

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = db.productoDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertAll_y_getAll_funcionan_correctamente() = runBlocking {
        val productos = listOf(
            ProductoEntity(1, "A", "desc", 1000.0, "img", 5),
            ProductoEntity(2, "B", "desc2", 2000.0, "img2", 10)
        )

        dao.insertAll(productos)

        val resultado = dao.getAll()

        Assert.assertEquals(2, resultado.size)
    }

    @Test
    fun findById_devuelve_producto_correcto() = runBlocking {
        val producto = ProductoEntity(1, "A", "desc", 1000.0, "img", 5)

        dao.insertAll(listOf(producto))

        val resultado = dao.findById(1)

        Assert.assertEquals("A", resultado?.nombre)
    }


}