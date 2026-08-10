package com.example.biblioteca.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.biblioteca.data.local.entities.Usuario

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun registrar(usuario: Usuario): Long

    @Query("SELECT * FROM usuarios WHERE correo = :correo AND password = :password LIMIT 1")
    suspend fun login(correo: String, password: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE correo = :correo OR cedula = :cedula LIMIT 1")
    suspend fun buscarPorCorreoOCedula(correo: String, cedula: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun buscarPorCorreo(correo: String): Usuario?
}