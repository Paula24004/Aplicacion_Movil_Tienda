package com.example.apptiendadeportiva_grupo10.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.apptiendadeportiva_grupo10.model.ProductoEntity

@Dao
interface ProductoDao {
    @Query("SELECT * FROM productos")
    suspend fun getAll(): List<ProductoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(productos: List<ProductoEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(producto: ProductoEntity)

    @Query("SELECT * FROM productos WHERE id = :id LIMIT 1")
    suspend fun findById(id: Int): ProductoEntity?

    @Query("DELETE FROM productos WHERE id = :id")
    suspend fun deleteById(id: Int)
}