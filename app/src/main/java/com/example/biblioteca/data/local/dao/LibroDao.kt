package com.example.biblioteca.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.biblioteca.data.Libro

@Dao
interface LibroDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun prestamo(libro: Libro)

    @Query("SELECT * FROM libros WHERE usuarioId = :usuarioId")
    suspend fun getLibrosPorUsuario(usuarioId: Int): List<Libro>
    
    @Query("SELECT * FROM libros WHERE titulo = :titulo AND usuarioId = :usuarioId LIMIT 1")
    suspend fun buscarPrestamo(titulo: String, usuarioId: Int): Libro?
}