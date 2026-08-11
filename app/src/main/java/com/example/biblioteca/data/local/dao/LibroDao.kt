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

    @Query("SELECT * FROM libros WHERE usuarioId = :usuarioId AND isDevuelto = 0")
    suspend fun getLibrosActivosPorUsuario(usuarioId: Int): List<Libro>

    @Query("UPDATE libros SET isDevuelto = 1 WHERE id = :libroId")
    suspend fun devolverLibro(libroId: Int)

    @Query("SELECT * FROM libros WHERE titulo = :titulo AND usuarioId = :usuarioId AND isDevuelto = 0 LIMIT 1")
    suspend fun buscarPrestamo(titulo: String, usuarioId: Int): Libro?
}