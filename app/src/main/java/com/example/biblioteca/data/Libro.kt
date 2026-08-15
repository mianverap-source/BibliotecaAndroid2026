package com.example.biblioteca.data

data class Libro(
    val id: Int = 0,
    val titulo: String,
    val autor: String,
    val usuarioId: Int? = null,
    val categoriaId: Int? = null,
    val categoriaNombre: String? = null, // Para el JOIN
    val fechaPrestamo: Long = System.currentTimeMillis(),
    val fechaVencimiento: Long = System.currentTimeMillis() + (3 * 24 * 60 * 60 * 1000), // 3 días después
    val isDevuelto: Boolean = false,
    val comentario: String? = null
)