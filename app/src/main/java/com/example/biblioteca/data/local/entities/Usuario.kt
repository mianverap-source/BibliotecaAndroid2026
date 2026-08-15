package com.example.biblioteca.data.local.entities

data class Usuario(
    val id: Int = 0,
    val nombreCompleto: String,
    val cedula: String,
    val institucion: String,
    val telefono: String,
    val direccion: String,
    val anioIngreso: Int,
    val correo: String,
    val password: String,
    val fotoUri: String? = null
)