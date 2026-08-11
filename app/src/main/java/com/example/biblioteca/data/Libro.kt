package com.example.biblioteca.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.biblioteca.data.local.entities.Usuario

@Entity(
    tableName = "libros",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class Libro(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val autor: String,
    val usuarioId: Int? = null,
    val portadaResId: Int? = null,
    val fechaPrestamo: Long = System.currentTimeMillis(),
    val isDevuelto: Boolean = false
)