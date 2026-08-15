package com.example.biblioteca.data.local.entities

data class Categoria(
    val id: Int,
    val nombre: String
) {
    override fun toString(): String = nombre
}